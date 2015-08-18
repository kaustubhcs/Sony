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
package com.example.sony.smarteyeglass.extension.hellonotification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * The constants for the dialog.
 */
public enum DialogType {

    /**
     * The Read Me dialog.
     */
    READ_ME {
        @Override
        public Dialog createDialog(final Context context) {
            OnClickListener listener = new OnClickListener() {
                @Override
                public void onClick(final DialogInterface i, final int which) {
                    i.cancel();
                }
            };
            return new AlertDialog.Builder(context)
                    .setMessage(R.string.preference_option_read_me_txt)
                    .setTitle(R.string.preference_option_read_me)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(android.R.string.ok, listener)
                    .create();
        }
    },

    /**
     * The Clear events dialog.
     */
    CLEAR {
        @Override
        public Dialog createDialog(final Context context) {
            OnClickListener ok = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface i, final int id) {
                    (new ClearEventsTask(context)).execute();
                }
            };
            OnClickListener cancel = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface i, final int id) {
                    i.cancel();
                }
            };
            return new AlertDialog.Builder(context)
                    .setMessage(R.string.preference_option_clear_txt)
                    .setTitle(R.string.preference_option_clear)
                    .setIcon(android.R.drawable.ic_input_delete)
                    .setPositiveButton(android.R.string.yes, ok)
                    .setNegativeButton(android.R.string.no, cancel)
                    .create();
        }
    };

    /**
     * Creates the dialog.
     *
     * @param context
     *            The application context.
     * @return The dialog.
     */
    public abstract Dialog createDialog(Context context);
}
