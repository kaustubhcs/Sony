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
 this software without specific prior written permission.

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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.sony.smarteyeglass.SmartEyeglassControl;
import com.sony.smarteyeglass.SmartEyeglassControl.Intents;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils.PointInWorldCoordinate;
import com.sony.smarteyeglass.extension.util.SmartEyeglassEventListener;
import com.sony.smarteyeglass.extension.util.ar.CylindricalRenderObject;
import com.sony.smarteyeglass.extension.util.ar.RenderObject;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;

/**
 * Shows how to use AR rendering to display objects whose
 * position is defined by real-world coordinates (latitude, longitude).
 * Converts these coordinates to the cylindrical coordinate system,
 * based on the user's current position.
 */
public final class SampleARConvertCoordinateSystemControl
        extends ControlExtension {

    /** The application context. */
    private final Context context;
    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 2;

    /** The render object for the icon to display. */
    private CylindricalRenderObject renderObj = null;
    /** The Object ID. */
    private static final int OBJECT_ID = 1;

    /** Handlers for AR events */
    private final SmartEyeglassEventListener listener =
            new SmartEyeglassEventListener() {

        // Log the event when AR object registration is completed
        @Override
        public void onARRegistrationResult(
                final int result, final int objectId) {
            Log.d(Constants.LOG_TAG,
                    "onARRegistrationResult() result=" + result
                    + " objectId=" + objectId);
            if (result != SmartEyeglassControl.Intents.AR_RESULT_OK) {
                Log.d(Constants.LOG_TAG,
                        "AR object registration failed! errorcode = " + result);
                return;
            }
        }

        // Send a registered AR object to the AR rendering engine
        // when the registered position is in view
        @Override
        public void onARObjectRequest(final int objectId) {
            Log.d(Constants.LOG_TAG,
                    "onLocalRenderingObjectRequest() "
                    + " objectId=" + objectId);
            // Send the render object
            utils.sendARObjectResponse(renderObj, 0);
        }
    };

    /**
     * Creates an instance of this control class.
     *      @param context              The context.
     *      @param hostAppPackageName   Package name of host application.
     */
    public SampleARConvertCoordinateSystemControl(
            final Context context, final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        utils = new SmartEyeglassControlUtils(hostAppPackageName, listener);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);

        // Create an object to be rendered using the Cylindrical Coordinate
        // System
        renderObj = new CylindricalRenderObject(OBJECT_ID,
                getBitmapResource(R.drawable.goal_pin), 0,
                SmartEyeglassControl.Intents.AR_OBJECT_TYPE_STATIC_IMAGE,
                0.0f, 0.0f);
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        utils.deactivate();
    }

    /**
     *  When app starts or restarts, start rendering process,
     *  set current real-world position, and convert it to
     *  Cylindrical Coordinate system.
     */
    @Override
    public void onResume() {
        final SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        // start or restart the AR rendering engine
        renderStart();

        //Get current real-world position of user
        final float hereLatitude = getFloat(prefs,
                R.string.preference_key_here_latitude);
        final float hereLongitude = getFloat(prefs,
                R.string.preference_key_here_longitude);
        // Get real-world location of AR object
        final float destinationLatitude = getFloat(prefs,
                R.string.preference_key_destination_latitude);
        final float destinationLongitude = getFloat(prefs,
                R.string.preference_key_destination_longitude);
        Log.d(Constants.LOG_TAG,
                "hereLatitude = " + hereLatitude
                + " hereLongitude = " + hereLongitude
                + " destinationLatitude = " + destinationLatitude
                + " destinationLongitude = " + destinationLongitude);

        final PointInWorldCoordinate viewingLocation =
                createCoordinate(hereLatitude, hereLongitude);
        final PointInWorldCoordinate targetLocation =
                createCoordinate(destinationLatitude, destinationLongitude);

        // Convert current viewing position and target position in world space
        // to a position in the cylindrical coordinate space where the display
        // object can be rendered in order to appear to be overlaid on the
        // object of interest.
        final PointF point = SmartEyeglassControlUtils
                .convertCoordinateSystemFromWorldToCylindrical(
                        viewingLocation, targetLocation);
        Log.d(Constants.LOG_TAG,
                "point.x = " + point.x
                + " point.y = " + point.y);

        renderObj.setPosition(point);
        // Register the display object with the resulting render position.
        registerObject(renderObj);
    }

    /**
     * Create a real-world position object with specific coordinates.
     *      @param latitude     The latitude of the real object.
     *      @param longitude    The longitude of the real object.
     *      @return An object that encodes this point in the World Coordinates
     *              system.
     */
    private PointInWorldCoordinate createCoordinate(
            final float latitude, final float longitude) {
        PointInWorldCoordinate c = utils.new PointInWorldCoordinate();
        c.latitude = latitude;
        c.longitude = longitude;
        // We are ignoring the altitude parameter for this demonstration
        c.altitude = 0;
        return c;
    }

    /**
     * Retrieves the preferences mapped to a resource ID.
     *      @param prefs    The shared preferences.
     *      @param id       The resource ID for the name of the preference.
     *      @return         A float value representing the value of the
     *                      preference.
     */
    private float getFloat(final SharedPreferences prefs,
                           final int id) {
        final String defaultValue = Constants.ID_TO_DEFAULT_MAP.get(id);
        String s = prefs.getString(context.getString(id), defaultValue);
        return Float.parseFloat(s);
    }

    /**
     * Starts local rendering.
     */
    private void renderStart() {
        utils.setRenderMode(Intents.MODE_AR);
    }

    /**
     * Registers a render object with the AR rendering engine
     *      @param obj The render object
     */
    private void registerObject(final RenderObject obj) {
        Log.d(Constants.LOG_TAG, "registerObject " + obj);
        utils.registerARObject(obj);
    }

    /**
     * Retrieves the display bitmap mapped to a resource ID.
     *      @param id   The resource ID.
     *      @return     The bitmap.
     */
    private Bitmap getBitmapResource(final int id) {
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), id);
        b.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return b;
    };
}
