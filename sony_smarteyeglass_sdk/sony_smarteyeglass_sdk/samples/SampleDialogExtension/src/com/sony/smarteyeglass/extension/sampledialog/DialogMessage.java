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
package com.sony.smarteyeglass.extension.sampledialog;

import com.sony.smarteyeglass.SmartEyeglassControl.Intents;

/**
 * The constants of the dialog messages.
 */
public enum DialogMessage {

    /** The time-out message. */
    TIMEOUT(R.string.time_out_dialog_message, Intents.DIALOG_MODE_TIMEOUT),

    /** The message shown when the OK button is pressed. */
    OK_BUTTON(R.string.only_ok_button_dialog_message, Intents.DIALOG_MODE_OK);

    /** The resource ID of the text. */
    private int id;

    /** The dialog mode. */
    private int mode;

    /**
     * Creates a new instance.
     *
     * @param id
     *            The resource ID of the text.
     * @param mode
     *            The dialog mode.
     */
    private DialogMessage(final int id, final int mode) {
        this.id = id;
        this.mode = mode;
    }

    /**
     * Returns the resource ID of the text.
     *
     * @return The resource ID of the text.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the dialog mode
     *
     * @return The dialog mode.
     */
    public int getMode() {
        return mode;
    }
}
