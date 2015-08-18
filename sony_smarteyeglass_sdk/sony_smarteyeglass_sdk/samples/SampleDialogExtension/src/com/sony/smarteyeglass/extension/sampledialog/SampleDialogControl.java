/*
Copyright (c) 2011, Sony Mobile Communications Inc.
Copyright (c) 2014, Sony Corporation

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Mobile Communications Inc.
 nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.sony.smarteyeglass.extension.sampledialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;

import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sony.smarteyeglass.extension.util.SmartEyeglassEventListener;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.control.Control.Intents;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

/**
 * Demonstrates three types of dialogs:
 * -- Simple message dialog, a pop-up with a timeout
 * -- Message dialog with OK button
 * -- Menu dialog with multiple action buttons
 * For the menu type, an event handler shows how to get the users choice,
 * the button used to dismiss the dialog.
 *
 * The user can switch between the two types of message dialog
 * by swiping left or right on the touch pad, and invoke the menu
 * dialog with a tap.
 */
public final class SampleDialogControl extends ControlExtension {

    /** Strings for the dialog buttons. */
    private static final String[] BUTTONS = {"button1", "button2", "button3"};

    /** Resource IDs for the text messages. */
    private static final int[] TEXT_ID_ARRAY = {
        R.string.how_to_use_Title_text,
        R.string.how_to_use_Tap_text,
        R.string.how_to_use_Left_text,
        R.string.how_to_use_Right_text};

    /** The text size. */
    private static final int TEXT_SIZE = 16;

    /** The application context. */
    private final Context context;

    /** The screen size. */
    private final ScreenSize size;

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** Map of swipe directions to dialog messages. */
    private final SparseArray<DialogMessage> messageMap =
            new SparseArray<DialogMessage>();

    /** The dialog status, {@code true} if the dialog is closed. */
    private boolean dialogClosed;

    /**
     * Creates an instance of this control class.
     *
     * @param context
     *            The application context.
     * @param hostAppPackageName
     *            The package name;
     */
    public SampleDialogControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        // Listen for the onDialogClosed event
        final SmartEyeglassEventListener listener =
                new SmartEyeglassEventListener() {
            // Respond by displaying the result
            @Override
            public void onDialogClosed(final int code) {
                Log.d(Constants.LOG_TAG, "onDialogClosed() : " + code);
                dialogClosed = true;
                updateDisplay(code);
            }
        };
        utils = new SmartEyeglassControlUtils(hostAppPackageName, listener);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
        size = new ScreenSize(context);
        /*
         * Initialize map of swipe events to different dialog message modes for
         *  the demo
         */
        messageMap.put(Intents.SWIPE_DIRECTION_LEFT, DialogMessage.TIMEOUT);
        messageMap.put(Intents.SWIPE_DIRECTION_RIGHT, DialogMessage.OK_BUTTON);
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        utils.deactivate();
    };

    // Reset state and update display when screen becomes visible.
    @Override
    public void onResume() {
        super.onResume();
        dialogClosed = false;
        updateDisplay(0);
    }

    /**
     * Shows which button was used to close the last dialog.
     *
     * @param code
     *            The button index returned to the event handler.
     */
    private void updateDisplay(final int code) {
        final int width = size.getWidth();
        final int height = size.getHeight();
        final Bitmap textBitmap =
                Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        textBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        final Canvas canvas = new Canvas(textBitmap);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(TEXT_SIZE);
        paint.setColor(Color.WHITE);

        final Resources res = context.getResources();
        final int x = res.getInteger(R.integer.X_point);
        final int pointY = res.getInteger(R.integer.Y_point);
        int y = pointY;
        for (int id : TEXT_ID_ARRAY) {
            canvas.drawText(res.getString(id), x, y, paint);
            y += pointY;
        }
        // Includes the button index
        if (dialogClosed) {
            canvas.drawText(res.getString(R.string.dialog_Closed_text) + code,
                    x, y, paint);
        }
        // Redraws the screen
        showBitmap(textBitmap);
    }

    // Respond to tap on controller's touch pad by showing the menu dialog
    @Override
    public void onTap(final int action, final long timeStamp) {
        Log.d(Constants.LOG_TAG, "tap = " + action);
        if (action != Control.TapActions.SINGLE_TAP) {
            return;
        }
        Resources res = context.getResources();
        String title = res.getString(R.string.dialog_title);
        String message = res.getString(R.string.tap_dialog_message);
        utils.showDialogMessage(title, message, BUTTONS);
    }

    // Respond to swipe on controller's touch pad by showing one of
    // the simple message dialogs
    @Override
    public void onSwipe(final int direction) {
        Log.d(Constants.LOG_TAG, "direction = " + direction);
        DialogMessage m = messageMap.get(direction);
        if (m == null) {
            return;
        }
        Resources res = context.getResources();
        utils.showDialogMessage(res.getString(m.getId()), m.getMode());
    }
}
