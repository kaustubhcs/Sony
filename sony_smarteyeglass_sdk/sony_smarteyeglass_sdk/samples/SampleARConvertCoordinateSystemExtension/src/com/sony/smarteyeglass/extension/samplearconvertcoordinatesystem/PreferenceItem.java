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
package com.sony.smarteyeglass.extension.samplearconvertcoordinatesystem;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * The preference item.
 */
public abstract class PreferenceItem {

    /**
     * The listener that closes a dialog when the button on the dialog is
     * clicked.
     */
    protected static final OnClickListener DISMISS = new OnClickListener() {
        @Override
        public void onClick(final DialogInterface dialog, final int which) {
            dialog.cancel();
        }
    };

    /** The resource ID of the text. */
    private final int textId;

    /** Whether this item handles the click or not. */
    private final boolean handlesClick;

    /**
     * Create a new instance.
     *
     * @param textId The resource ID of the text.
     * @param handlesClick {@code true} if this item handles the click.
     */
    public PreferenceItem(final int textId, final boolean handlesClick) {
        this.textId = textId;
        this.handlesClick = handlesClick;
    }

    /**
     * Create a new instance that doesn't handle the click.
     *
     * @param textId The resource ID of the text.
     */
    public PreferenceItem(final int textId) {
        this(textId, false);
    }

    /**
     * Returns the resource ID of the text.
     *
     * @return The resource ID of the text.
     */
    public final int getTextId() {
        return textId;
    }
    /**
     * Returns whether this item handles the click or not.
     *
     * @return {@code true} if this item handles the click.
     */
    public final boolean handlesClick() {
        return handlesClick;
    }

    /**
     * Creates a new {@code Dialog} object.
     *
     * @param context
     *            The application context.
     * @return A new {@code Dialog} object.
     */
    public abstract Dialog create(final Context context);
}
