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
package com.example.sony.smarteyeglass.extension.hellowidget;

import android.content.Context;
import android.util.Log;

import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

/**
 * This control shows the app-specific home screen when the user starts
 * the app by tapping the entry card defined by the HelloWidget
 * (or when the app resumes after a pause or returns to the home page).
 */
public final class HelloWidgetControl extends ControlExtension {

    /** The application context. */
    private Context context;

    /**
     * Creates an instance of this control class.
     *
     * @param context
     *            The application context.
     * @param hostAppPackageName
     *            Package name of host application.
     */
    public HelloWidgetControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
    }

    /*
     * Shows the layout for the home page when this app becomes visible.
     */
    @Override
    public void onResume() {
        Log.d(Constants.LOG_TAG, "Control On Resume");
        super.onResume();
        String text = context.getString(R.string.text_control);
        showLayout(R.layout.layout_control, null);
        sendText(R.id.control_string, text);
    }
}
