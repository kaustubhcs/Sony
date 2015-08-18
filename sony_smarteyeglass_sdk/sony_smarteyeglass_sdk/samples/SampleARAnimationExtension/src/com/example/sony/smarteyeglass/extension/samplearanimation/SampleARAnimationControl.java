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
package com.example.sony.smarteyeglass.extension.samplearanimation;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;

import com.sony.smarteyeglass.SmartEyeglassControl;
import com.sony.smarteyeglass.SmartEyeglassControl.Intents;
import com.sony.smarteyeglass.extension.sampleARAnimation.R;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sony.smarteyeglass.extension.util.SmartEyeglassEventListener;
import com.sony.smarteyeglass.extension.util.ar.CylindricalRenderObject;
import com.sony.smarteyeglass.extension.util.ar.RenderObject;
import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;
import com.sonyericsson.extras.liveware.aef.sensor.Sensor;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensor;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEvent;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEventListener;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorException;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorManager;

/**
* Demonstrates how to register a set of images to be rendered as an animation
* by the AR engine, using the cylindrical coordinate system.
*/
public final class SampleARAnimationControl extends ControlExtension {

    /** */
    private final Context context;

    /**
     * Set constants
     * Constant for 3Pi
     */
    private static final float PI_3 = (float) (Math.PI * 3.0);

    /** Degrees of a full rotation */
    private static final int FULL_ROTATION_DEGREE = 360;

    /** Degrees of a half rotation */
    private static final int HALF_ROTATION_DEGREE = FULL_ROTATION_DEGREE / 2;

    /** The entire angle of the vertical range to consider */
    private static final float VER_RANGE = 6.0f;

    /** */
    private int baseDeg;

    /** */
    private boolean isFirst = true;

    /** */
    private int count = 0;

    /** */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 2;

    /** */
    private static final int OBJECT_ID = 1;
    /** */
    private CylindricalRenderObject renderObj;

    /** Sensor management objects*/
    private AccessorySensor sensor = null;

    /** */
    private AccessorySensorManager sensorManager;

    /** Animation interval time */
    private static final int ANIMATION_INTERVAL_TIME = 66;

    /** Animation bitmap sequence */
    private SparseArray<Bitmap> imageMap = new SparseArray<Bitmap>();

    /** Animation starts disabled */
    private boolean isAnimation = false;

    /** Animation frame counter */
    private int frameCount = 0;

    /** Animation timer */
    private Timer timer;

    /** Handlers for AR events */
    private final SmartEyeglassEventListener listener =
            new SmartEyeglassEventListener() {
        // Log successful registrations of an AR object
        @Override
        public void onARRegistrationResult(
                final int result, final int objectId) {
            Log.d(Constants.LOG_TAG,
                    "onARRegistrationResult() result=" + result
                    + " objectId=" + objectId);
            if (result != SmartEyeglassControl.Intents.AR_RESULT_OK) {
                Log.d(Constants.LOG_TAG,
                        "AR registre object failed! errorcode = " + result);
                return;
            }
        }
        // Find bitmap to render when requested by AR engine
        @Override
        public void onARObjectRequest(final int objectId) {
            Log.d(Constants.LOG_TAG,
                    "onLocalRenderingObjectRequest() "
                    + " objectId=" + objectId);
            // send bitmap
            utils.sendARObjectResponse(renderObj, 0);
        }
    };

    /**
     * Creates an instance of this control class.
     *      @param context              The context.
     *      @param hostAppPackageName   Package name of host application.
     */
    public SampleARAnimationControl(
        final Context context, final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        sensorManager = new AccessorySensorManager(context, hostAppPackageName);
        utils = new SmartEyeglassControlUtils(hostAppPackageName, listener);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
        setAnimationResource();
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        utils.deactivate();
    }

    /** Start the rendering operation. */
    private void renderStart() {
        utils.setRenderMode(Intents.MODE_AR);
        utils.changeARCylindricalVerticalRange(VER_RANGE);
        loadResource();
        startARAnimation();
    }

    /**
     * Create render object to place at cylindrical-coordinate position.
     * and register it with the AR engine.
     */
    private void loadResource() {
        final float v = 0.0f;
        float h = baseDeg;

        renderObj = new CylindricalRenderObject(OBJECT_ID,
                getBitmapResource(R.drawable.sample_00), 0,
                SmartEyeglassControl.Intents.AR_OBJECT_TYPE_ANIMATED_IMAGE,
                h, v);
        registerObject(renderObj);
    }

    /**
     * Load the bitmap image for the current animation frame.
     */
    private void setAnimationResource() {
        int[] id = AnimationResources.ID_RESOURCE_LIST;
        for (int frame = 0; frame < AnimationResources.MAX_FRAME; frame++) {
            imageMap.put(frame, getBitmapResource(id[frame]));
        }
    }

    /**
     * Register a render object with the AR engine.
     *      @param obj The render object.
     */
    private void registerObject(final RenderObject obj) {
        Log.d(Constants.LOG_TAG, "registerObject " + obj);
        this.utils.registerARObject(obj);
    }

    /**
     * Convert sensor data to an orientation.
     *      @param x The horizontal coordinate.
     *      @param y The vertical coordinate.
     *      @return The heading value.
     */
    private static int getHeading(final float x, final float y) {
        double heading = 0;
        if (x == 0 && y < 0) {
            heading = Math.PI / 2.0;
        }
        if (x == 0 && y > 0) {
            heading = PI_3 / 2.0;
        }
        if (x < 0) {
            heading = Math.PI - Math.atan(y / x);
        }
        if (x > 0 && y < 0) {
            heading = -Math.atan(y / x);
        }
        if (x > 0 && y > 0) {
            heading = 2.0 * Math.PI - Math.atan(y / x);
        }
        int d = (int) (heading * HALF_ROTATION_DEGREE / Math.PI);
        if (d < 0) {
            d += FULL_ROTATION_DEGREE;
        }
        return d;
    }

    /**
     * Retrieve a registered bitmap by its resource ID.
     *      @param id   The resource ID.
     *      @return     The bitmap.
     */
    private Bitmap getBitmapResource(final int id) {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id);
        b.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return b;
    }

    /**
     * Start the sensors.
     */
    private void sensorStart() {
        count = 0;
        sensor = sensorManager.getSensor(SensorTypeValue.MAGNETIC_FIELD);
        isFirst = true;
        // Start listening for sensor updates.
        if (sensor == null) {
            Log.d(Constants.LOG_TAG, "No such sensor type: "
                    + SensorTypeValue.MAGNETIC_FIELD);
            return;
        }
        try {
            sensor.registerListener(new AccessorySensorEventListener() {
                @Override
                public void onSensorEvent(final AccessorySensorEvent ev) {
                    updateDirection(ev);
                }
            }, Sensor.SensorRates.SENSOR_DELAY_NORMAL, 0);
        } catch (AccessorySensorException e) {
            Log.d(Constants.LOG_TAG, "Failed to register listener");
        }
    }

    /**
     * Update the heading direction from sensor data.
     * @param ev The sensor event.
     */
    private void updateDirection(final AccessorySensorEvent ev) {
        float[] v = ev.getSensorValues();
        int headDirection = getHeading(v[0], v[1]);
        ++count;
        if (count > 2 && isFirst) {
            baseDeg = headDirection;
            renderStart();
            isFirst = false;
        }
    }

    // Reset sensor reading when app becomes visible
    @Override
    public void onResume() {
        baseDeg = 0;
        sensorStart();
    }

    // Stop showing animation and listening for sensor data
    // when app is paused
    @Override
    public void onPause() {
        // Stop animation
        stopARAnimation();

        // Stop sensors
        if (sensor == null) {
            return;
        }
        // Stop listening for sensor data
        sensor.unregisterListener();
        sensor = null;
    }

    @Override
    public void onTouch(final ControlTouchEvent ev) {
        super.onTouch(ev);
    }

    @Override
    public void onStop() {
        super.onStop();
        imageMap.clear();
    }

    /**
     * Get next frame of the animation and send it with same object ID
     */
    private void updateAnimationimage() {
        if (!isAnimation) {
            return;
        }

        Bitmap bitmap = imageMap.get(frameCount);
        if (bitmap != null) {
            utils.sendARAnimationObject(OBJECT_ID, bitmap);
            Log.d(Constants.LOG_TAG, "update animation image: " + frameCount);
            frameCount++;
        }
        if (frameCount >= AnimationResources.MAX_FRAME) {
            frameCount = 0;
        }
    }

    /** Start animation rendering operation */
    private void startARAnimation() {
        utils.enableARAnimationRequest();

        // Clear timer variable, if it is already holding a Timer object
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        // Create a new instance of Timer and set it to run with interval
        // time value
        timer = new Timer();
        timer.schedule(new TimerTask() {
            private Handler mHandler = new Handler();
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        updateAnimationimage();
                    }
                });
            }
        }, 0, ANIMATION_INTERVAL_TIME);

        Log.d(Constants.LOG_TAG, "start animation");
        isAnimation = true;
    }

    /** Stop animation rendering operation */
    private void stopARAnimation() {
        // stop timer
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        utils.disableARAnimationRequest();

        Log.d(Constants.LOG_TAG, "stop animation");
        isAnimation = true;
    }
}
