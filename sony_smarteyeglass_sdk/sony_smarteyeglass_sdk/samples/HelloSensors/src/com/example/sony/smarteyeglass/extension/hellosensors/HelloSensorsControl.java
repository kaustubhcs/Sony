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

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;
import com.sonyericsson.extras.liveware.aef.sensor.Sensor;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEvent;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEventListener;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorException;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstrates how to collect data from all available sensors
 * built into the SmartEyeglass device:
 * accelerometer, gyroscope (attitude sensor),
 * magnetic field sensor (compass), light meter and rotation vector.
 *
 * Displays sensor data from one sensor at a time, continuously
 * updating the display with current data. The user switches to the
 * next sensor by touching the touch pad on the controller.
 */
public final class HelloSensorsControl extends ControlExtension {

    /** The array of resource IDs for the generic sensor. */
    private static final int[] SENSOR_GENERIC = new int[] {
        R.id.sensor_value_x,
        R.id.sensor_value_y,
        R.id.sensor_value_z};

    /** The array of resource IDs for sensors that return 3 values. */
    private static final int[] SENSOR_3 = new int[] {
        R.id.sensor_value_1,
        R.id.sensor_value_2,
        R.id.sensor_value_3};

    /** The array of resource IDs for sensors that return 4 values. */
    private static final int[] SENSOR_4 = new int[] {
        R.id.sensor_value_1,
        R.id.sensor_value_2,
        R.id.sensor_value_3,
        R.id.sensor_value_4};

    /** The array of arrays of resource IDs for the rotation vector sensor. */
    private static final int[][] SENSOR_ROTATION_VECTOR = new int[][] {
            SENSOR_3, SENSOR_4};

    /** The array of resource IDs for the light sensor. */
    private static final int[] SENSOR_LIGHT = new int[] {R.id.light_value};

    /** The screen size. */
    private final ScreenSize size;

    /**
     * A map from the name of a sensor to the corresponding
     * {@code SensorPainter} object.
     */
    private final Map<String, SensorPainter> painterMap =
            new HashMap<String, SensorPainter>();

    /** The index of the current sensor in {@code sensors}. */
    private int currentSensor = 0;

    /** The list of all supported sensors. */
    private List<AccessorySensor> sensors = new ArrayList<AccessorySensor>();

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** The listener notified when a sensor's value changes. */
    private final AccessorySensorEventListener listener =
            new AccessorySensorEventListener() {
        // Respond to change by updating the screen to
        // display the current sensor data
        @Override
        public void onSensorEvent(final AccessorySensorEvent sensorEvent) {
            Log.d(Constants.LOG_TAG, "Listener: OnSensorEvent");
            updateCurrentDisplay(sensorEvent);
        }
    };

    /**
     * Creates an instance of this control class,
     * initializing the sensor map.
     *
     * @param context                 The context.
     * @param hostAppPackageName     Package name of host application.
     */
    public HelloSensorsControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
        // Create a sensor manager object
        AccessorySensorManager manager =
                new AccessorySensorManager(context, hostAppPackageName);
        // Initialize sensor map
        sensors = new ArrayList<AccessorySensor>();
        for (String type : Sensors.ALL_SET) {
            if (!DeviceInfoHelper.isSensorSupported(context, hostAppPackageName,
                    type)) {
                continue;
            }
            AccessorySensor s = manager.getSensor(type);
            if (s == null) {
                continue;
            }
            sensors.add(s);
        }
        size = new ScreenSize(context);

        painterMap.put(SensorTypeValue.ROTATION_VECTOR,
                new SensorPainter(R.layout.rotationvector_sensor_values,
                                  SENSOR_ROTATION_VECTOR));
        painterMap.put(SensorTypeValue.LIGHT,
                new SensorPainter(R.layout.lightsensor_values,
                                  SENSOR_LIGHT));
    }

    // Update the display when app becomes visible, using the
    // current sensor data.
    @Override
    public void onResume() {
        Log.d(Constants.LOG_TAG, "Starting control");

        // Keep the screen on for this demonstration.
        // Don't do this in a real app, it will drain the battery.
        setScreenState(Control.Intents.SCREEN_STATE_ON);

        // Start listening for sensor updates.
        register();
        // Refresh the screen
        updateCurrentDisplay(null);
    }
    // Stop listening for sensor data when the app is paused.
    @Override
    public void onPause() {
         unregister();
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        utils.deactivate();
        sensors.clear();
        sensors = null;
   }

    // Respond to a touch on the touch pad by switching
    // to the next sensor and updating the screen.
    @Override
    public void onTouch(final ControlTouchEvent event) {
        super.onTouch(event);
        if (event.getAction() != Control.Intents.TOUCH_ACTION_RELEASE) {
            return;
        }
        // Unregisters the current sensor
        unregister();

        // Toggles sensor type
        nextSensor();

        // Registers the new sensor
        register();

        // Updates the screen
        updateCurrentDisplay(null);
    }

    /**
     * Retrieve the sensor currently being listened to.
     *
     * @return The current sensor.
     */
    private AccessorySensor getCurrentSensor() {
        return sensors.get(currentSensor);
    }

    /**
     * Start listening for events from the current sensor.
     */
    private void register() {
        Log.d(Constants.LOG_TAG, "Register listener");

        AccessorySensor sensor = getCurrentSensor();
        try {
            sensor.registerFixedRateListener(listener,
                Sensor.SensorRates.SENSOR_DELAY_UI);
        } catch (AccessorySensorException e) {
            Log.d(Constants.LOG_TAG, "Failed to register listener", e);
        }
    }

    /**
     * Stop listening for events from the current sensor.
     */
    private void unregister() {
        AccessorySensor sensor = getCurrentSensor();
        sensor.unregisterListener();
    }

    /**
     * Set the current sensor to the next one in the map.
     */
    private void nextSensor() {
        if (++currentSensor == sensors.size()) {
            currentSensor = 0;
        }
    }

    /**
     * Update the display with the latest data from the current sensor.
     *
     * @param ev The sensor event.
     */
    private void updateCurrentDisplay(final AccessorySensorEvent ev) {
        // Get the current sensor
        AccessorySensor sensor = getCurrentSensor();
        // Retrieve the sensor type
        final String name = sensor.getType().getName();
        // Use the correct display method for the sensor type
        SensorPainter p = painterMap.get(name);
        if (p == null) {
            p = createGenericSensorPainter(name);
        }
        // Update the display
        utils.showBitmap(p.createBitmap(size, ev));
    }

    /**
     * Create a new display object that creates display
     * data in a way appropriate to the current sensor type.
     *
     * @param name     The name of the sensor.
     * @return The new  display object.
     */
    private SensorPainter createGenericSensorPainter(final String name) {
        return new SensorPainter(R.layout.generic_sensor_values,
                                 SENSOR_GENERIC) {
            // Adjust the text in the layout to match the sensor type
            @Override
            protected void doExtraLayout(final LinearLayout layout) {
                TextView title
                    = (TextView) layout.findViewById(R.id.sensor_title);
                title.setText(name);
            }
        };
    }
}
