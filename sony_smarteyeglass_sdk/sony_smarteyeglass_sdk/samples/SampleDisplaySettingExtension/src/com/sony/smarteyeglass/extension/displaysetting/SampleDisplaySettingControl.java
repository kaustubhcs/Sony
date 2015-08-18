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
package com.sony.smarteyeglass.extension.displaysetting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.control.Control.Intents;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

/**
 * Shows how to set screen depth and enable safe display mode.
 */
public final class SampleDisplaySettingControl extends ControlExtension {

    /** The minimum depth. */
    private static final int MIN_DEPTH = -4;

    /** The maximum depth. */
    private static final int MAX_DEPTH = 6;

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** The screen size. */
    private final ScreenSize size;

    /** The current depth. */
    private int depth;

    /** The current safe-display state, {@code true} when mode is enabled. */
    private boolean safeDisplayMode;

    /** The application context. */
    private final Context context;

    /** Mapping the swipe direction to the {@code Runnable} object. */
    private final SparseArray<Runnable> actionMap = new SparseArray<Runnable>();

    /**
     * Creates an instance of this control class.
     *
     * @param context The context.
     * @param hostAppPackageName Package name of host application.
     */
    public SampleDisplaySettingControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        size = new ScreenSize(context);
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);

        /*
         * Swipe right to increases the depth,
         * if it has not yet reached maximum
         */
        actionMap.put(Intents.SWIPE_DIRECTION_RIGHT, new Runnable() {
            @Override
            public void run() {
                if (depth == MAX_DEPTH) {
                    return;
                }
                ++depth;
            };
        });

        /*
         * Swipe left to decreases the depth,
         * if it has not yet reached minimum
         */
        actionMap.put(Intents.SWIPE_DIRECTION_LEFT, new Runnable() {
            @Override
            public void run() {
                if (depth == MIN_DEPTH) {
                    return;
                }
                --depth;
            }
        });
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        utils.deactivate();
    };

    // Reset state and update display when screen becomes visible.
    @Override
    public void onResume() {
        depth = 0;
        setDisplayMode(false);
    }

    // Respond to tap on controller's touch pad by
    // toggling safe-display mode
    @Override
    public void onTap(final int action, final long timeStamp) {
        Log.d(Constants.LOG_TAG,
                "onTap() " + Integer.toString(action));
        if (action != Control.TapActions.SINGLE_TAP) {
            return;
        }
        setDisplayMode(!safeDisplayMode);
    }

    /**
     * Turns safe-display mode on or off.
     *
     * @param b New mode state to set
     *            {@code true} to enable safe display
     *            {@code false} to disable safe display
     */
    private void setDisplayMode(final boolean b) {
        safeDisplayMode = b;
        if (b) {
            // Enable safe-display mode, show only lower half of the display
            utils.enableSafeDisplayMode();
        } else {
            // Disable safe-display mode, switch back to full screen
            utils.disableSafeDisplayMode();
        }
        // Update screen in current mode
        updateDisplay();
    }

    // Respond to swipe on controller's touch pad by
    // changing apparent depth of display plane
    @Override
    public void onSwipe(final int direction) {
        Log.d(Constants.LOG_TAG, "onSwipe() " + Integer.toString(direction));

        // Increase or decrease depth, according to swipe direction
        Runnable r = actionMap.get(direction);
        if (r == null) {
            return;
        }
        r.run();

        // Sets screen depth to new value determined by runnable
        utils.setScreenDepth(depth);
        // Update screen at current depth
        updateDisplay();
    }

    /**
     * Update the display.
     */
    private void updateDisplay() {
        // Display string includes current settings
        String safeDisplay = "SafeDisplayMode:"
                + ((safeDisplayMode) ? "On" : "Off");
        String depthString = "Depth: " + String.format("%+d", depth);

        // Prepare a layout of display data
        final int width = size.getWidth();
        final int height = size.getHeight();
        LinearLayout params = new LinearLayout(context);
        params.setLayoutParams(new LayoutParams(width, height));
        LinearLayout layout = (LinearLayout) LinearLayout.inflate(
                context, R.layout.sample_control, params);
        // Update strings in layout's TextView
        ((TextView) layout.findViewById(R.id.sample_control_text1))
                .setText(safeDisplay);
        ((TextView) layout.findViewById(R.id.sample_control_text2))
                .setText(depthString);
        ((TextView) layout.findViewById(R.id.sample_control_text3))
                .setText(safeDisplay);
        ((TextView) layout.findViewById(R.id.sample_control_text4))
                .setText(depthString);
        ((ImageView) layout.findViewById(R.id.image_view))
                .setImageResource(R.drawable.icon);

        layout.measure(width, height);
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());

        Bitmap screen =
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        screen.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        Canvas canvas = new Canvas(screen);
        // Convert layout to bitmap for display
        layout.draw(canvas);
        // Display bitmap
        utils.showBitmap(screen);
        screen.recycle();
    }
}
