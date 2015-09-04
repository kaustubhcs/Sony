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

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sony.smarteyeglass.SmartEyeglassControl.Intents;
import com.sony.smarteyeglass.extension.util.ControlCameraException;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;

/**
 * The implementation of Still mode.
 */
public final class StillMode extends AbstractStillMode {

    /**
     * Creates a new instance.
     *
     * @param context
     *            The application context.
     */
    public StillMode(final Context context) {
        super(context);
    }

    @Override
    public int getRecordingMode() {

        Log.d("KTB2", "Called getRecordingMode in Still Mode");
        return Intents.CAMERA_MODE_STILL;
    }

    @Override
    protected void handlePictureData(final BitmapDisplay d, final byte[] data) {
        Log.d("KTB2", "Called handlePictureData in Still Mode");
        d.displayBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
    }

    @Override
    protected void willOpenCamera(final SmartEyeglassControlUtils utils)
            throws ControlCameraException {
        Log.d("KTB2", "Called willOpenCamera in Still Mode");
        Log.d(Constants.LOG_TAG, "startCamera ");
        utils.startCamera();
    }
}
