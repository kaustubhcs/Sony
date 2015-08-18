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
package com.sony.smarteyeglass.extension.powermodesample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sony.smarteyeglass.SmartEyeglassControl.Intents;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sony.smarteyeglass.extension.util.SmartEyeglassEventListener;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

/**
 * Shows how to set the two SmartEyeglass power modes.
 * User taps on touch pad to toggle between high-performance
 * power mode, which enables a WiFi connection, and normal power mode,
 * which uses only the Bluetooth connection.
 */
public final class SamplePowerModeControl extends ControlExtension {

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** An application context. */
    private final Context context;

    /** The screen size. */
    private final ScreenSize size;

    /** The current power mode. */
    private int currentPowerMode;

    /** Power mode status, {@code true} if the power mode is changing. */
    private boolean isChanging = false;

    /** Map power mode to a descriptive display string. */
    private SparseArray<String> modeTextMap = new SparseArray<String>();

    /**
     * Creates an instance of this control class.
     *
     * @param context               The context.
     * @param hostAppPackageName    Package name of host application.
     */
    public SamplePowerModeControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        size = new ScreenSize(context);
        // Map power-mode constants to descriptive strings
        modeTextMap.append(Intents.POWER_MODE_HIGH, "HIGH");
        modeTextMap.append(Intents.POWER_MODE_NORMAL, "NORMAL");
        // Define event handler
        SmartEyeglassEventListener listener = new SmartEyeglassEventListener() {
            @Override
            // Respond to change of power mode by displaying current mode
            public void onChangePowerMode(final int powerMode) {
                currentPowerMode = powerMode;
                isChanging = false;
                String status = modeTextMap.get(powerMode);
                if (status == null) {
                    status = "UNKNOWN";
                }
                updateScreen("Power mode is " + status);
            }
        };
        utils = new SmartEyeglassControlUtils(hostAppPackageName, listener);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "SamplePowerModeControl onDestroy");
        utils.deactivate();
    };

    // Set power mode to NORMAL when app becomes visible.
    @Override
    public void onResume() {
        changePowerMode(Intents.POWER_MODE_NORMAL);
    }

    @Override
    public void onPause() {
    }

    // Repond to tap on touch pad by toggling power mode
    @Override
    public void onTap(final int action, final long timeStamp) {
        Log.d(Constants.LOG_TAG,
                "onTap() " + Integer.toString(action));
        if (action != Control.TapActions.SINGLE_TAP) {
            return;
        }
        // Don't try to change mode if switch operation is currently in progress
        if (isChanging) {
            return;
        }
       // Switch between NORMAL and HIGH power modes
        changePowerMode((currentPowerMode == Intents.POWER_MODE_NORMAL)
                ? Intents.POWER_MODE_HIGH : Intents.POWER_MODE_NORMAL);
    }

    /**
     * Sets the power mode.
     *
     * @param mode The new power mode constant, one of:
     *            {@link SmartEyeglassControl.Intents.POWER_MODE_HIGH}, or
     *            {@link SmartEyeglassControl.Intents.POWER_MODE_NORMAL}
     */
    private void changePowerMode(final int mode) {
        isChanging = true;
        updateScreen("Power mode changes now...");
        utils.setPowerMode(mode);
    }

    /**
     * Updates the screen.
     *
     * @param status The descriptive string for the current power mode.
     */
    private void updateScreen(final String status) {
        final int width = size.getWidth();
        final int height = size.getHeight();
        LinearLayout root = new LinearLayout(context);
        root.setLayoutParams(new LayoutParams(width, height));
        LinearLayout layout =
                (LinearLayout) LinearLayout.inflate(context,
                        R.layout.sample_control, root);
         // Set the text in the layout's TextView to the power-mode string
        ((TextView) layout.findViewById(R.id.sample_control_text1))
                .setText(status);
        layout.measure(width, height);
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());

        Bitmap screen =
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        screen.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        Canvas canvas = new Canvas(screen);
        // Convert the layout to a bitmap for display
        layout.draw(canvas);
        // Display the bitmap
        utils.showBitmap(screen);
        screen.recycle();
    }
}
