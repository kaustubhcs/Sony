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
package com.example.sony.smarteyeglass.extension.hellostandbymode;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sony.smarteyeglass.SmartEyeglassControl.Intents;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sony.smarteyeglass.extension.util.SmartEyeglassEventListener;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

/**
 * Demonstrates how to request standby mode and how to listen
 * for changes in the standby status.
 */
public final class HelloStandbyModeControl extends ControlExtension {

    /** Standby permission state. */
    private boolean permission;

    /** Standby status intention, {@code false} if requesting standby mode. */
    private boolean standby;

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** The application context. */
    private final Context context;

    /**
     * Creates an instance of this control class.
     *
     * @param context               The context.
     * @param hostAppPackageName    Package name of host application.
     */
    public HelloStandbyModeControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        // Initialize status variables
        permission = true;
        standby = false;
        // Create an event listener for standby-mode events.
        final SmartEyeglassEventListener listener =
                new SmartEyeglassEventListener() {
            // Handle confirmation of successful entry to standby mode
            @Override
            public boolean onConfirmationEnterStandby() {
                return permission;
            }
            // Handle notification of a change in standby status
            @Override
            public void onStandbyStatus(final int status) {
                Log.d(Constants.LOG_TAG,
                        "onStandbyStatus() " + status);
                handleStatusChange(status);
            }
        };
        utils = new SmartEyeglassControlUtils(hostAppPackageName, listener);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        utils.deactivate();
    };

    /**
     * Handle change of standby status by displaying current status.
     *
     * @param status The standby mode value.
     */
    private void handleStatusChange(final int status) {
        switch (status) {
        case Intents.STANDBY_MODE_OFF:
            Log.d(Constants.LOG_TAG, "STANDBY_MODE_OFF");
            updateDisplay(R.string.text_return_standby_mode);
            break;
        case Intents.STANDBY_MODE_ON:
            Log.d(Constants.LOG_TAG, "STANDBY_MODE_ON");
            break;
        default:
            break;
        }
    }

    // Update the display when app becomes visible, using the
    // current status and instructions for how to use the app
    @Override
    public void onResume() {
        super.onResume();
        showLayout(R.layout.layout_text, null);
        Resources res = context.getResources();
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(context);
        permission = pref.getBoolean(
                res.getString(R.string.preference_key_standby_permission),
                true);
        updateDisplay(R.string.text_how_to_use);
    }

    // Respond to a tap on the touch pad by toggling
    // the standby mode.
    @Override
    public void onTap(final int action, final long timeStamp) {
        if (action != Control.TapActions.SINGLE_TAP) {
            return;
        }
        standby = !standby;
        if (standby) {
            updateDisplay(R.string.text_enter_standby_mode);
        } else {
            utils.requestEnterStandbyMode();
        }
    }

    /**
     * Update the display text.
     *
     * @param textId The ID of a text resource.
     */
    private void updateDisplay(final int textId) {
        Resources res = context.getResources();
        sendText(R.id.control_string, res.getString(textId));
    }
}
