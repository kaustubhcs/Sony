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
package com.sony.smarteyeglass.extension.samplecamera;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sony.smarteyeglass.extension.util.CameraEvent;
import com.sony.smarteyeglass.extension.util.ControlCameraException;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;

/**
 * Base class stores all camera mode parameters and helper objects.
 * Further specialized in base classes for still and movie
 * recording modes (AbstractStillMode and AbstractMovieMode).
 */
public abstract class AbstractCameraMode {




    /** The delegate to save data. */
    private final Saver saver;

    /** Whether to save more data to SD card. */
    private final boolean saves;

    /** The delegate to paint a bitmap to display. */
    private final DisplayPainter painter;

    /** Whether the camera is currently open. */
    public boolean cameraOpened = false;

    /**
     * Creates a an instance of this class.
     * Creates a an instance of this class.
     *
     * @param context  The application context.
     */
    protected AbstractCameraMode(final Context context) {

        Log.d("KTB2 CALL" , "AbstractCameraMode");
        painter = new DisplayPainter(context);
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        saves = prefs.getBoolean(context.getString(
                R.string.preference_key_save_to_sdcard), true);
        saver = (saves)
                ? new AggressiveSaver()
                : new DefaultSaver();
    }

    /**
     * Retrieves the current recording mode. There are four modes.
     *
     * @return The recording mode constant.
     */
    public abstract int getRecordingMode();

    /**
     * Retrieves the preference ID for the resolution of this recording mode.
     *
     * @return The preference ID.
     */
    public abstract int getPreferenceId();

    /**
     * An action to take before handling the camera event. Default does nothing.
     * A subclass can override and customize the behavior.
     *
     * @param ev    The camera event.
     */
    protected void willHandleEvent(final CameraEvent ev) {
        Log.d("KTB2 CALL" , "willHandleEvent");

//        Log.d("KTB2", ev.toString());
    }

    /**
     * An action take when a JPEG data is received. Default does nothing.
     * A subclass can override and customize the behavior to display the data
     * using the delegate.
     *
     * @param delegate  The delegate to display a bitmap.
     * @param data      The JPEG data.
     */
    protected void handlePictureData(final BitmapDisplay delegate, final byte[] data)

    {

        Log.d("KTB2 CALL" , "handlePictureData");

    }

    /**
     * An action to take after handling the JPEG data. Default does nothing.
     * A subclass can override and customize the behavior.
     */
    protected void didHandlePictureEvent() {
        Log.d("KTB2 CALL" , "didHandlePictureEvent");

    }

    /**
     * Gets the list of strings to display.
     *
     * @return The list of strings to display.
     */
    protected abstract List<String> getMessageList();

    /**
     * Toggles the state of this camera mode.
     *
     * @param utils     The instance of the Control Utility class.
     */
    public abstract void toggleState(SmartEyeglassControlUtils utils);

    /**
     * An action to take before opening the camera.
     *
     * @param utils     The instance of the Control Utility class.
     * @throws ControlCameraException if failed to open the camera.
     */
    protected abstract void willOpenCamera(SmartEyeglassControlUtils utils)
            throws ControlCameraException;

    /**
     * An action to take after closing the camera. Default does nothing.
     * A subclass can override and customize the behavior.
     */
    protected void willCloseCamera() {
        Log.d("KTB2 CALL" , "willCloseCamera");

    }

    /**
     * Sets the delegate to use to display a bitmap.
     *
     * @param display   The new delegate object.
     */
    public final void setBitmapDisplay(final BitmapDisplay display) {
        Log.d("KTB2 CALL" , "setBitmapDisplay");

        painter.setBitmapDisplay(display);
    }

    /**
     * Reports whether more data should be saved to SD card.
     *
     * @return {@code true} if data should be saved.
     */
    protected final boolean saves() {
        return saves;
    }

    /**
     * Retrieves the delegate used to save data.
     *
     * @return The delegate object.
     */
    protected final Saver getSaver() {
        return saver;
    }

    /**
     * Handles the picture data in a camera event.
     * Displays the data, and saves it if that option is enabled.
     *
     * @param ev    The camera event.
     */

    byte[] global_img_data;

    private void handlePictureEvent(final CameraEvent ev) {
        Log.d("KTB2 CALL" , "handlePictureEvent");
        byte[] data = ev.getData();
        global_img_data = data;



                //BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        Log.d("KTB2", "Inside Abstract Camera Mode" + ev.toString());
        if (data == null || data.length == 0) {
            return;
        }
        saver.save(data);
        handlePictureData(painter, data);

        Log.d("KTB2 CALL", "handlePictureEvent // CameraEvent To String" + ev.toString());

        painter.paint(getMessageList(), ev, cameraOpened);
    }

    /**
     * Handles a camera event. The default logs the error or handles
     * the returned picture data.
     *
     * @param ev    The camera event.
     */
    public final void handleCameraEvent(final CameraEvent ev) {
             Log.d("KTB2 CALL" , "handleCameraEvent");

        willHandleEvent(ev);
        if (ev.getErrorStatus() != 0) {
            Log.d(Constants.LOG_TAG, "error code = " + ev.getErrorStatus());
            return;
        }
        handlePictureEvent(ev);
        didHandlePictureEvent();
    }

    /**
     * Updates the display on the device.
     */
    public final void updateDisplay() {
        Log.d("KTB2 CALL" , "updateDisplay");


        // painter.setBitmapDisplay(BitmapDisplay display);

      //  painter.paint(getMessageList() , global_img_data , cameraOpened);
    }

    /**
     * Opens the camera.
     *
     * @param utils     The instance of the Control Utility class.
     */
    public int ktb_opc = 0;
    protected final void openCamera(final SmartEyeglassControlUtils utils) {
        try {
            Log.d("KTB2 CALL" , "openCamera");
            Log.d("KTB2 CALL" , "openCamera // " + cameraOpened );
            Log.d("KTB2 CALL" , "openCamera // " + cameraOpened );
            ktb_opc = 1;

            willOpenCamera(utils);
        } catch (ControlCameraException e) {
            Log.d(Constants.LOG_TAG, "Failed to register listener", e);
        }
        Log.d(Constants.LOG_TAG, "onResume: Registered listener");
        cameraOpened = true;
    }
    public int ktb_camera_check()
    {

        return ktb_opc;
    }

    /**
     * Closes the camera.
     *
     * @param utils     The instance of the Control Utility class.
     */
    public final void closeCamera(final SmartEyeglassControlUtils utils) {
        Log.d("KTB2 CALL" , "closeCamera");

        if (!cameraOpened) {
            return;
        }
        willCloseCamera();
        utils.stopCamera();
        cameraOpened = false;
    }

    /**
     * Reports whether the camera is currently closed.
     *
     * @return {@code true} if the camera is closed.
     */
    protected final boolean isCameraClosed() {
        return !cameraOpened;
    }
}
