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
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The preference item to input the here/destination location.
 */
public final class SettingLocationItem extends PreferenceItem {

    /** The map from resource ID to the resource name. */
    private static final SparseArray<String> ID_TO_STRING_MAP;

    static {
        SparseArray<String> map = new SparseArray<String>();
        map.put(R.string.preference_key_here_latitude,
                "preference_key_here_latitude");
        map.put(R.string.preference_key_here_longitude,
                "preference_key_here_longitude");
        map.put(R.string.preference_key_destination_latitude,
                "preference_key_destination_latitude");
        map.put(R.string.preference_key_destination_longitude,
                "preference_key_destination_longitude");
        ID_TO_STRING_MAP = map;
    }

    /** The resource ID of the EditView for the latitude. */
    private int latitudeId;

    /** The resource ID of the EditView for the longitude. */
    private int longitudeId;

    /** The title of the dialog. */
    private String label;

    /**
     * Creates a new instance.
     *
     * @param id
     *            The resource ID of the text.
     * @param latitudeId
     *            The resource ID of the EditView for the latitude.
     * @param longitudeId
     *            The resource ID of the EditView for the longitude.
     * @param label
     *            The title of the dialog.
     */
    public SettingLocationItem(final int id, final int latitudeId,
            final int longitudeId, final String label) {
        super(id);
        this.latitudeId = latitudeId;
        this.longitudeId = longitudeId;
        this.label = label;
    }

    /**
     * Creates a new {@code LinearLayout} object including TextView and
     * EditView.
     *
     * @param context
     *            The application context.
     * @param text
     *            The text to display with TextView.
     * @param key
     *            The resource ID of the EditView.
     * @return The {@code LinearLayout} object.
     */
   private LinearLayout createLayout(final Context context,
           final String text, final int key) {
       final SharedPreferences prefs =
               PreferenceManager.getDefaultSharedPreferences(context);
       LinearLayout layout = new LinearLayout(context);
       layout.setOrientation(LinearLayout.VERTICAL);

       TextView textView = new TextView(context);
       textView.setText(text);

       EditText editText = new EditText(context);
       editText.findViewById(key);
       editText.setInputType(InputType.TYPE_CLASS_NUMBER
               | InputType.TYPE_NUMBER_FLAG_DECIMAL);
       String inputNum = prefs.getString(context.getString(key), "0.0");
       if (inputNum.equals("0.0")) {
            inputNum = Constants.ID_TO_DEFAULT_MAP.get(key);
       }
       editText.setText(inputNum);
       editText.addTextChangedListener(new TextWatcher() {
           @Override
           public void onTextChanged(
                   final CharSequence stringNo, final int start,
                   final int before, final int count) {
           }

           @Override
           public void beforeTextChanged(
                   final CharSequence stringNo, final int start,
                   final int count, final int after) {
           }

           @Override
           public void afterTextChanged(final Editable stringNo) {
               Editor e = prefs.edit();
               String keyString = ID_TO_STRING_MAP.get(key);
               Log.d(Constants.LOG_TAG,
                       "keyString = " + keyString
                       + " afterTextChanged stringNo = " + stringNo);
               e.putString(keyString, String.valueOf(stringNo));
               e.commit();
           }
       });

       layout.addView(textView);
       layout.addView(editText);
       return layout;
   }

   @Override
   public Dialog create(final Context context) {
       LinearLayout layout = new LinearLayout(context);
       layout.setOrientation(LinearLayout.VERTICAL);
       layout.addView(createLayout(context, "Latitude", latitudeId));
       layout.addView(createLayout(context, "Longitude", longitudeId));
       return new Builder(context)
               .setTitle(label + " Point")
               .setView(layout)
               .create();
   }
}
