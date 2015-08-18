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

import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.util.SparseArray;

import com.sonyericsson.extras.liveware.aef.notification.Notification.EventColumns;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.notification.NotificationUtil;

import java.util.Random;

/**
 * This preference activity lets the user send notifications. It also allows the
 * user to clear all notifications associated with this extension.
 */
public final class HelloNotificationPreferenceActivity
        extends PreferenceActivity {

    /** The all events. */
    private static final Event[] EVENTS = Event.createAllEvents();

    /** The map from the ID of dialog to the {@code DialogType} constant. */
    private final SparseArray<DialogType> dialogMap =
            new SparseArray<DialogType>();

    /** Creates a new instance. */
    public HelloNotificationPreferenceActivity() {
        for (DialogType t : DialogType.values()) {
            dialogMap.put(t.ordinal(), t);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads the preferences from an XML resource.
        addPreferencesFromResource(R.xml.preferences);

        // Shows the Read Me dialogue.
        Preference pref =
                findPreference(getText(R.string.preference_key_read_me));
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference pref) {
                showDialog(DialogType.READ_ME.ordinal());
                return true;
            }
        });

        // Sends a notification.
        pref = findPreference(getString(R.string.preference_key_send));
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference pref) {
                addData();
                return true;
            }
        });

        // Shows the Clear notifications dialogue.
        pref = findPreference(getString(R.string.preference_key_clear));
        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(final Preference pref) {
                showDialog(DialogType.CLEAR.ordinal());
                return true;
            }
        });

        // Removes preferences that are not supported by the accessory.
        if (!ExtensionUtils.supportsHistory(getIntent())) {
            pref = findPreference(getString(R.string.preference_key_clear));
            getPreferenceScreen().removePreference(pref);
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        DialogType t = dialogMap.get(id);
        if (t == null) {
            Log.w(Constants.LOG_TAG, "Not a valid dialogue id: " + id);
            return null;
        }
        return t.createDialog(this);
    }

    /**
     * This method sets randomly generated data that will be connected to a
     * notification.
     */
    private void addData() {
        Random rand = new Random();
        int index = rand.nextInt(EVENTS.length);
        Event ev = EVENTS[index];
        long time = System.currentTimeMillis();
        long sourceId = NotificationUtil.getSourceId(this,
                Constants.EXTENSION_SPECIFIC_ID);
        if (sourceId == NotificationUtil.INVALID_ID) {
            Log.e(Constants.LOG_TAG, "Failed to insert data");
            return;
        }
        String profileImage = ExtensionUtils.getUriString(this,
                R.drawable.widget_default_userpic_bg);

        // Builds the notification.
        ContentValues values = new ContentValues();
        values.put(EventColumns.EVENT_READ_STATUS, false);
        values.put(EventColumns.DISPLAY_NAME, ev.getName());
        values.put(EventColumns.MESSAGE, ev.getMessage());
        values.put(EventColumns.PERSONAL, 1);
        values.put(EventColumns.PROFILE_IMAGE_URI, profileImage);
        values.put(EventColumns.PUBLISHED_TIME, time);
        values.put(EventColumns.SOURCE_ID, sourceId);
        NotificationUtil.addEvent(this, values);
    }
}
