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

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.sony.smarteyeglass.SmartEyeglassControl.Intents;
import com.sony.smarteyeglass.extension.util.CameraEvent;
import com.sony.smarteyeglass.extension.util.ControlCameraException;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;

/**
 * The implementation of JpegStream mode.
 */
public final class JpegStreamMode extends AbstractMovieMode {

    /** The recording mode. */
    private final int mode;

    /**
     * Creates a new instance.
     *
     * @param context
     *            The application context.
     * @param isHighRate
     *            {@code true} if the rate is high.
     */
    public JpegStreamMode(final Context context, final boolean isHighRate) {
        super(context);
        mode = (isHighRate)
                ? Intents.CAMERA_MODE_JPG_STREAM_HIGH_RATE
                : Intents.CAMERA_MODE_JPG_STREAM_LOW_RATE;
    }

    @Override
    protected void willHandleEvent(final CameraEvent ev) {
        Log.d("KTB2 CALL", "JpegStreamMode // willHandleEvent");

        Log.d(Constants.LOG_TAG, "Stream Event coming: " + ev);

    }

    @Override
    public int getRecordingMode()
    {
        Log.d("KTB2 CALL", "JpegStreamMode // getRecordingMode");
        Log.d("KTB2 CALL", "The current mode is = " + mode);

        return mode;
    }

    @Override
    protected void addMessages(final List<String> list) {
        Log.d("KTB2 CALL", "addMessages");

        list.add("Rec: " + getSaver().getIndex());
    }

    @Override
    protected void willOpenCamera(final SmartEyeglassControlUtils utils)
            throws ControlCameraException {

        Log.d(Constants.LOG_TAG, "startCamera ");

        Log.d("KTB2 CALL", "willOpenCamera");


        utils.startCamera();
        if (!saves()) {
            return;
        }
    }
}
