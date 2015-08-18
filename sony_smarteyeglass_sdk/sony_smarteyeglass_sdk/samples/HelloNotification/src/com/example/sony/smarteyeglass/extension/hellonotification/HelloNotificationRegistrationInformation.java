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

import android.content.ContentValues;
import android.content.Context;

import com.sonyericsson.extras.liveware.aef.notification.Notification.SourceColumns;
import com.sonyericsson.extras.liveware.aef.registration.Registration.ExtensionColumns;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides information needed during extension registration.
 */
public final class HelloNotificationRegistrationInformation
        extends RegistrationInformation {

    /** The application context. */
    private final Context context;

    /**
     * Creates a notification registration object.
     *
     * @param context
     *            The context.
     */
    public HelloNotificationRegistrationInformation(final Context context) {
        this.context = context;
    }

    @Override
    public int getRequiredNotificationApiVersion() {
        return 1;
    }

    @Override
    public int getRequiredWidgetApiVersion() {
        return API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredControlApiVersion() {
        return API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredSensorApiVersion() {
        return API_NOT_REQUIRED;
    }

    @Override
    public ContentValues getExtensionRegistrationConfiguration() {
        String extensionIcon = getUriString(R.drawable.icon_extension);
        String iconHostapp = getUriString(R.drawable.icon);
        String extensionIcon48 = getUriString(R.drawable.icon_extension_48);
        String configurationText = getString(R.string.configuration_text);
        String extensionName = getString(R.string.extension_name);

        ContentValues values = new ContentValues();
        values.put(ExtensionColumns.CONFIGURATION_ACTIVITY,
                HelloNotificationPreferenceActivity.class.getName());
        values.put(ExtensionColumns.CONFIGURATION_TEXT, configurationText);
        values.put(ExtensionColumns.EXTENSION_ICON_URI, extensionIcon);
        values.put(ExtensionColumns.EXTENSION_48PX_ICON_URI, extensionIcon48);
        values.put(ExtensionColumns.EXTENSION_KEY, Constants.EXTENSION_KEY);
        values.put(ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
        values.put(ExtensionColumns.NAME, extensionName);
        values.put(ExtensionColumns.NOTIFICATION_API_VERSION,
                getRequiredNotificationApiVersion());
        values.put(ExtensionColumns.PACKAGE_NAME, context.getPackageName());
        return values;
    }

    @Override
    public ContentValues[] getSourceRegistrationConfigurations() {
        // This sample only adds one source but it is possible to add more
        // sources if needed.
        List<ContentValues> list = new ArrayList<ContentValues>();
        list.add(getSourceConfiguration(Constants.EXTENSION_SPECIFIC_ID));
        return list.toArray(new ContentValues[list.size()]);
    }

    /**
     * Returns the properties of a source.
     *
     * @param extensionId
     *            The ID of the extension to associate the source with.
     * @return The source configuration.
     */
    private ContentValues getSourceConfiguration(final String extensionId) {
        String iconSource1 = getUriString(
                R.drawable.icn_30x30_message_notification);
        String iconSource2 = getUriString(
                R.drawable.icn_18x18_message_notification);
        String textToSpeech = getString(R.string.text_to_speech);

        ContentValues values = new ContentValues();
        values.put(SourceColumns.ENABLED, true);
        values.put(SourceColumns.ICON_URI_1, iconSource1);
        values.put(SourceColumns.ICON_URI_2, iconSource2);
        values.put(SourceColumns.UPDATE_TIME, System.currentTimeMillis());
        values.put(SourceColumns.NAME, getString(R.string.source_name));
        values.put(SourceColumns.EXTENSION_SPECIFIC_ID, extensionId);
        values.put(SourceColumns.PACKAGE_NAME, context.getPackageName());
        values.put(SourceColumns.TEXT_TO_SPEECH, textToSpeech);
        // It is possible to connect actions to notifications. These will be
        // accessible when tapping the options menu on the SmartEyeglass.
        values.put(SourceColumns.ACTION_1,
                getString(R.string.action_event_1));
        values.put(SourceColumns.ACTION_2,
                getString(R.string.action_event_2));
        values.put(SourceColumns.ACTION_3,
                getString(R.string.action_event_3));
        values.put(SourceColumns.ACTION_ICON_1,
                getUriString(R.drawable.actions_1));
        values.put(SourceColumns.ACTION_ICON_2,
                getUriString(R.drawable.actions_2));
        values.put(SourceColumns.ACTION_ICON_3,
                getUriString(R.drawable.actions_3));
        return values;
    }

    /**
     * Returns the localized string corresponding to the specified resource ID.
     *
     * @param id
     *            The resource ID.
     * @return The localized string.
     */
    private String getString(final int id) {
        return context.getString(id);
    }

    /**
     * Returns the URI string corresponding to the specified resource ID.
     *
     * @param id
     *            The resource ID.
     * @return The URI string.
     */
    private String getUriString(final int id) {
        return ExtensionUtils.getUriString(context, id);
    }
}
