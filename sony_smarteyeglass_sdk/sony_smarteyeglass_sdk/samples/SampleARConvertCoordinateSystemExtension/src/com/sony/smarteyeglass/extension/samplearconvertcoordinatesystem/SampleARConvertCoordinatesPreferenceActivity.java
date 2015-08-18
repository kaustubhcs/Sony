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

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * The SampleARConvertCoordinatesPreferenceActivity handles the preferences for
 * the SampleARConvertCoordinateSystemControl.
 */
public final class SampleARConvertCoordinatesPreferenceActivity
        extends PreferenceActivity {

    /** The list of the {@code PreferenceItem} object. */
    private List<PreferenceItem> itemList;

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads the preferences from an XML resource.
        addPreferencesFromResource(R.xml.preference);

        itemList = new ArrayList<PreferenceItem>();
        itemList.add(new ReadMeItem());
        itemList.add(new ClearSettingItem() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                clearSettings();
                dialog.cancel();
                finish();
            }
        });
        itemList.add(new SettingLocationItem(
                R.string.preference_key_here_point,
                R.string.preference_key_here_latitude,
                R.string.preference_key_here_longitude,
                "Origin"));
        itemList.add(new SettingLocationItem(
                R.string.preference_key_destination_point,
                R.string.preference_key_destination_latitude,
                R.string.preference_key_destination_longitude,
                "Destination"));

        final int size = itemList.size();
        for (int k = 0; k < size; ++k) {
            PreferenceItem item = itemList.get(k);
            final int id = k;
            final boolean handlesClick = item.handlesClick();
            Preference pref = findPreference(getText(item.getTextId()));
            pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(final Preference pref) {
                    showDialog(id);
                    return handlesClick;
                }
            });
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        if (id < 0 || id >= itemList.size()) {
            return null;
        }
        PreferenceItem item = itemList.get(id);
        return item.create(this);
    }

    /**
     * Clears all settings.
     */
    private void clearSettings() {
        Editor e = PreferenceManager.getDefaultSharedPreferences(this).edit();
        for (IdPair<String> item : Constants.ID_DEFAULT_PAIR_LIST) {
            e.putString(getString(item.getId()), item.getValue());
        }
        e.commit();
    }
}
