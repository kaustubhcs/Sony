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
package com.example.sony.smarteyeglass.extension.hellosensors;

import android.content.ContentValues;
import android.content.Context;

import com.sonyericsson.extras.liveware.aef.registration.Registration.ExtensionColumns;
import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.HostApplicationInfo;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;

/**
 * Provides information needed during extension registration.
 */
public final class HelloSensorsRegistrationInformation
        extends RegistrationInformation {

    /** The application context. */
    private final Context context;

    /** Uses control API version*/
    private static final int CONTROL_API_VERSION = 4;

    /**
     * Creates a Sensor registration object.
     *
     * @param context The application context.
     */
    public HelloSensorsRegistrationInformation(final Context context) {
        this.context = context;
    }

    @Override
    public int getRequiredControlApiVersion() {
        return CONTROL_API_VERSION;
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
                HelloSensorsPreferenceActivity.class.getName());
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
        return SensorTypeValue.ACCELEROMETER.equals(sensor.getType().getName());
    }

    @Override
    public boolean isSupportedSensorAvailable(
            final Context context, final HostApplicationInfo hostApplication) {
        // Both control and sensor needs to be supported to register as sensor.
        return super.isSupportedSensorAvailable(context, hostApplication)
                && super.isSupportedControlAvailable(context, hostApplication);
    }

    @Override
    public boolean isSupportedControlAvailable(
            final Context context, final HostApplicationInfo hostApplication) {
        // Both control and sensor needs to be supported to register as control.
        return super.isSupportedSensorAvailable(context, hostApplication)
                && super.isSupportedControlAvailable(context, hostApplication);
    }
}
