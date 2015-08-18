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
package com.sony.smarteyeglass.extension.soundeffectsetting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;

/**
 * Shows how to enable or disable the sound effect that provides
 * audio feedback for button-press actions on the SmartEyeglass controller.
 * This effect is enabled by default.
 */
public final class SampleSoundEffectSettingControl extends ControlExtension {

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** The screen size. */
    private final ScreenSize size;

    /** An application context. */
    private final Context context;

    /** The current setting. */
    private boolean soundEffectSetting;

    /**
     * Creates an instance of this control class.
     *
     * @param context               The context.
     * @param hostAppPackageName    Package name of host application.
     */
    public SampleSoundEffectSettingControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        size = new ScreenSize(context);
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "SampleControlSmartEyeglass onDestroy");
        utils.deactivate();
    };

    //Restore default (sound effect on) when this app becomes visible
    @Override
    public void onResume() {
        setSetting(true);
    }

    @Override
    public void onTouch(final ControlTouchEvent event) {
    }

    // Respond to tap on touch pad by toggling sound effect on or off
    @Override
    public void onTap(final int action, final long timeStamp) {
        Log.d(Constants.LOG_TAG, "onTap() " + action);
        if (action != Control.TapActions.SINGLE_TAP) {
            return;
        }
        setSetting(!soundEffectSetting);
    }

    /**
     * Set sound effect on or off, and show current state
     *
     * @param b The new setting, true=on.
     */
    private void setSetting(final boolean b) {
        soundEffectSetting = b;
        if (b) {
            utils.enableSoundEffect();
            updateScreen("On");
        } else {
            utils.disableSoundEffect();
            updateScreen("Off");
        }
    }

    /**
     * Display the current sound-effect state.
     *
     * @param text  The current state string.
     */
    private void updateScreen(final String text) {
        String t = "SoundEffect:" + text;
        final int width = size.getWidth();
        final int height = size.getHeight();

        // Prepare a layout with display data
        LinearLayout params = new LinearLayout(context);
        params.setLayoutParams(new LayoutParams(width, height));
        LinearLayout layout = (LinearLayout) LinearLayout.inflate(
                context, R.layout.sample_control, params);
         // Update the string in the layout's TextView
        ((TextView) layout.findViewById(R.id.display_text)).setText(t);

        layout.measure(width, height);
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());

        Bitmap screen =
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        screen.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        Canvas canvas = new Canvas(screen);
        // Convert the layout to a bitmap for display
        layout.draw(canvas);
        // Display bitmap
        utils.showBitmap(screen);
        screen.recycle();
    }
}
