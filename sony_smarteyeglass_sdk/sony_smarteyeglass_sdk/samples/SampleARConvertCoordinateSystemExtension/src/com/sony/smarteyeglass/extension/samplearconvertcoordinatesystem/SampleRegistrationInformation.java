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

import android.content.ContentValues;
import android.content.Context;

import com.sonyericsson.extras.liveware.aef.registration.Registration.ExtensionColumns;
import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;
import com.sonyericsson.extras.liveware.extension.util.Dbg;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;

/**
 * Provides information needed during extension registration
 */
public final class SampleRegistrationInformation
        extends RegistrationInformation {

    /** The application context. */
    private final Context context;

    /**
     * Creates control registration object
     *
     * @param context The application context.
     */
    public SampleRegistrationInformation(final Context context) {
        this.context = context;
    }

    @Override
    public int getRequiredControlApiVersion() {
        return 1;
    }

    @Override
    public int getRequiredSensorApiVersion() {
        return 1;
    }

    @Override
    public int getRequiredNotificationApiVersion() {
        return RegistrationInformation.API_NOT_REQUIRED;
    }

    @Override
    public int getRequiredWidgetApiVersion() {
        return RegistrationInformation.API_NOT_REQUIRED;
    }

    @Override
    public ContentValues getExtensionRegistrationConfiguration() {
        String iconHostapp =
                ExtensionUtils.getUriString(context, R.drawable.icon);
        String iconExtension =
                ExtensionUtils.getUriString(context, R.drawable.icon_extension);

        ContentValues values = new ContentValues();
        values.put(ExtensionColumns.CONFIGURATION_ACTIVITY,
                SampleARConvertCoordinatesPreferenceActivity.class.getName());
        values.put(ExtensionColumns.CONFIGURATION_TEXT,
                context.getString(R.string.configuration_text));
        values.put(ExtensionColumns.NAME,
                context.getString(R.string.extension_name));
        values.put(ExtensionColumns.EXTENSION_KEY, Constants.EXTENSION_KEY);
        values.put(ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
        values.put(ExtensionColumns.EXTENSION_ICON_URI, iconExtension);
        values.put(ExtensionColumns.NOTIFICATION_API_VERSION,
                getRequiredNotificationApiVersion());
        values.put(ExtensionColumns.PACKAGE_NAME, context.getPackageName());
        return values;
    }

    @Override
    public boolean isDisplaySizeSupported(final int width, final int height) {
        ScreenSize size = new ScreenSize(context);
        return size.equals(width, height);
    }

    @Override
    public boolean isSensorSupported(final AccessorySensor sensor) {
        String name = sensor.getType().getName();
        Dbg.w("isSensorSupported: " + name);
        return name.equals(SensorTypeValue.MAGNETIC_FIELD);
    }
}
