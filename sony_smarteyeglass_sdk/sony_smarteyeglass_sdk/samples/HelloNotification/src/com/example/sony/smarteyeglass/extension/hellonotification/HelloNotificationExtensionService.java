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

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.widget.Toast;

import com.sonyericsson.extras.liveware.aef.notification.Notification;
import com.sonyericsson.extras.liveware.aef.notification.Notification.Event;
import com.sonyericsson.extras.liveware.aef.notification.Notification.EventColumns;
import com.sonyericsson.extras.liveware.aef.notification.Notification.SourceColumns;
import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

/**
 * The Hello Notification extension service handles extension registration and
 * inserts data into the notification database.
 */
public final class HelloNotificationExtensionService extends ExtensionService {

    /** Creates a new instance. */
    public HelloNotificationExtensionService() {
        super(Constants.EXTENSION_KEY);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.LOG_TAG, "onCreate: HelloNotificationExtensionService");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.LOG_TAG,
                "onDestroy: HelloNotificationExtensionService");
    }

    @Override
    protected void onViewEvent(final Intent intent) {
        String action = intent.getStringExtra(
                Notification.Intents.EXTRA_ACTION);
        // Determines what item a user tapped in the options menu and take
        // appropriate action.
        int eventId =
                intent.getIntExtra(Notification.Intents.EXTRA_EVENT_ID, -1);
        if (SourceColumns.ACTION_1.equals(action)) {
            doAction1(eventId);
        } else if (SourceColumns.ACTION_2.equals(action)) {
            Toast.makeText(this, "Action 2", Toast.LENGTH_LONG).show();
        } else if (SourceColumns.ACTION_3.equals(action)) {
            Toast.makeText(this, "Action 3", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRefreshRequest() {
        // Does nothing. Only relevant for polling extensions.
    }

    /**
     * Shows a toast on the phone with the information associated with an
     * event.
     *
     * @param eventId The event id.
     */
    public void doAction1(final int eventId) {
        Log.d(Constants.LOG_TAG, "doAction1 event id: " + eventId);
        Cursor cursor = null;
        try {
            String name = "";
            String message = "";
            cursor = getContentResolver().query(Event.URI, null,
                    EventColumns._ID + " = " + eventId, null, null);
           if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(
                        EventColumns.DISPLAY_NAME));
                message = cursor.getString(cursor.getColumnIndex(
                        EventColumns.MESSAGE));
            }
            String toastMessage = getText(R.string.action_event_1)
                    + ", Event: " + eventId
                    + ", Name: " + name
                    + ", Message: " + message;
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Log.e(Constants.LOG_TAG, "Failed to query event", e);
        } catch (SecurityException e) {
            Log.e(Constants.LOG_TAG, "Failed to query event", e);
        } catch (IllegalArgumentException e) {
            Log.e(Constants.LOG_TAG, "Failed to query event", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected RegistrationInformation getRegistrationInformation() {
        return new HelloNotificationRegistrationInformation(this);
    }

    @Override
    protected boolean keepRunningWhenConnected() {
        return false;
    }
}
