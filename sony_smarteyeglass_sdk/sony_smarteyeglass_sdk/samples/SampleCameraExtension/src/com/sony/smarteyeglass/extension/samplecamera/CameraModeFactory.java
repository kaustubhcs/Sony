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
import android.util.Log;

/**
 * Constants to create the camera mode.
 */
public enum CameraModeFactory {

    /** The Still mode. */
    STILL {
        @Override
        public AbstractCameraMode of(final Context context)
        {
            Log.d("KTB2 CALL", "Camera Mode Factory -- AbstractCameraMode");

            return new StillMode(context);
        }
    },

    /** The Still to File mode. */
    STILL_TO_FILE {
        @Override
        public AbstractCameraMode of(final Context context) {
            return new StillToFileMode(context);
        }
    },

    /** The JpegStream mode. (low rate) */
    LOW_JPEG_STREAM {
        @Override
        public AbstractCameraMode of(final Context context) {
            return new JpegStreamMode(context, false);
        }
    },

    /** The JpegStream mode. (high rate) */
    HIGH_JPEG_STREAM {
        @Override
        public AbstractCameraMode of(final Context context) {
            return new JpegStreamMode(context, true);
        }
    };

    /**
     * Creates a new camera mode.
     *
     * @param context
     *            The application context.
     * @return The camera mode.
     */
    public abstract AbstractCameraMode of(Context context);
}
