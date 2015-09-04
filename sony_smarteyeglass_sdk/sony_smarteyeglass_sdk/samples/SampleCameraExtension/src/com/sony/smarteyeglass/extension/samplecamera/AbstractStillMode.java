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
package com.sony.smarteyeglass.extension.samplecamera;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.sony.smarteyeglass.extension.util.CameraEvent;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;

/**
 * Base class stores mode information that applies to
 * still-image recording modes.
 */
public abstract class AbstractStillMode extends AbstractCameraMode {

    /** The message list to display. */
    private static final List<String> MESSAGE_LIST =
            Collections.singletonList("Tap to capture.");

    /**
     * Creates an instance of this class.
     *
     * @param context   The application context.
     */
    protected AbstractStillMode(final Context context) {
        super(context);
    }

    @Override
    protected final void willHandleEvent(final CameraEvent ev) {
        Log.d("KTB2 CALL" , "willHandleEvent");

        Log.d(Constants.LOG_TAG, "Camera Event coming: " + ev);
    }

    @Override
    public final int getPreferenceId() {
        Log.d("KTB2 CALL" , "getPreferenceId");

        return R.string.preference_key_resolution_still;
    }

    @Override
    protected final List<String> getMessageList() {

        Log.d("KTB2 CALL" , "getMessageList");

        return MESSAGE_LIST;
    }

    @Override
    public final void toggleState(final SmartEyeglassControlUtils utils) {
        Log.d("KTB2 CALL" , "ToggleState");

        if (isCameraClosed()) {
            openCamera(utils);
        }
        Log.d(Constants.LOG_TAG, "Select button pressed -> cameraCapture()");

        utils.requestCameraCapture();
    }
}
