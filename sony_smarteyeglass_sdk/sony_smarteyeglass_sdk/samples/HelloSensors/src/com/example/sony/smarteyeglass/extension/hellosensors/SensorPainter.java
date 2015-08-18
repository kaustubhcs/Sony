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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sonyericsson.extras.liveware.aef.sensor.Sensor.SensorAccuracy;
import com.sonyericsson.extras.liveware.extension.util.sensor.AccessorySensorEvent;

/**
 * Creates a bitmap with values from the sensors, corresponding to the type of
 * the sensor.
 */
public class SensorPainter {

    /** The map from an accuracy integer to the resource ID of the string. */
    private static final SparseIntArray ACCURACY_TEXT_MAP;

    static {
        SparseIntArray map = new SparseIntArray();
        map.put(SensorAccuracy.SENSOR_STATUS_UNRELIABLE,
                R.string.accuracy_unreliable);
        map.put(SensorAccuracy.SENSOR_STATUS_ACCURACY_LOW,
                R.string.accuracy_low);
        map.put(SensorAccuracy.SENSOR_STATUS_ACCURACY_MEDIUM,
                R.string.accuracy_medium);
        map.put(SensorAccuracy.SENSOR_STATUS_ACCURACY_HIGH,
                R.string.accuracy_high);
        ACCURACY_TEXT_MAP = map;
    }

    /** The ratio from nanoseconds to milliseconds. */
    private static final long NANO_TO_MILLI = 1000000L;

    /** The layout ID. */
    private final int layoutId;

    /**
     * A map from the number of sensor values to the array of resource IDs.
     */
    private final SparseArray<int[]> idArrayMap = new SparseArray<int[]>();

    /**
     * Creates a new instance with the specified layout ID and the specified
     * array of the resource IDs.
     *
     * @param layoutId
     *            The layout ID.
     * @param idArray
     *            An array of resource IDs.
     */
    public SensorPainter(final int layoutId, final int[] idArray) {
        this.layoutId = layoutId;
        idArrayMap.append(idArray.length, idArray);
    }

    /**
     * Creates a new instance with the specified layout ID and the specified
     * array of an array of the resource IDs.
     *
     * @param layoutId
     *            The layout ID.
     * @param all
     *            The all arrays of an array of the resource IDs.
     */
    public SensorPainter(final int layoutId, final int[][] all) {
        this.layoutId = layoutId;
        for (int[] idArray : all) {
            idArrayMap.append(idArray.length, idArray);
        }
    }

    /**
     * The hook method to do nothing. The subclass can override this method
     * and customize the layout.
     *
     * @param layout
     *            The layout created by the base class.
     */
    protected void doExtraLayout(final LinearLayout layout) {
    }

    /**
     * Sets values to the corresponding text views.
     *
     * @param layout
     *            The layout.
     * @param values
     *            The array of {@code float} values from the sensor.
     */
    private void updateSensorValues(final LinearLayout layout,
                                    final float[] values) {
        if (values == null) {
            return;
        }
        int[] idArray = idArrayMap.get(values.length);
        if (idArray == null) {
            return;
        }
        final int n = values.length;
        for (int k = 0; k < n; ++k) {
            int id = idArray[k];
            TextView textView = (TextView) layout.findViewById(id);
            // Shows values with one decimal.
            textView.setText(String.format("%.1f", values[k]));
        }
    }

    /**
     * Sets values in the specified event to the text views.
     *
     * @param context
     *            The application context.
     * @param layout
     *            The layout.
     * @param ev
     *            The {@code AccessorySensorEvent} object.
     */
    private void updateEventValues(final Context context,
            final LinearLayout layout, final AccessorySensorEvent ev) {
        if (ev == null) {
            return;
        }
        float[] values = ev.getSensorValues();
        updateSensorValues(layout, values);

        // Shows time stamp in milliseconds. (Reading is in nanoseconds.)
        TextView timeStampView = (TextView) layout.findViewById(
                R.id.sensor_value_timestamp);
        timeStampView.setText(String.format("%d",
                (long) (ev.getTimestamp() / NANO_TO_MILLI)));

        // Shows sensor accuracy.
        TextView accuracyView = (TextView) layout.findViewById(
                R.id.sensor_value_accuracy);
        accuracyView.setText(getAccuracyText(context, ev.getAccuracy()));
    }

    /**
     * Creates a {@code Bitmap} object to describe the specified event.
     *
     * @param size
     *            The screen size.
     * @param ev
     *            The {@code AccessorySensorEvent} object.
     * @return The {@code Bitmap} object.
     */
    public final Bitmap createBitmap(
            final ScreenSize size, final AccessorySensorEvent ev) {
        final Context context = size.getContext();
        final int width = size.getWidth();
        final int height = size.getHeight();

        LinearLayout root = new LinearLayout(context);
        root.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        root.setGravity(Gravity.CENTER);

        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout =
                (LinearLayout) inflater.inflate(layoutId, root, true);
        doExtraLayout(layout);
        updateEventValues(context, layout, ev);

        root.measure(width, height);
        root.layout(0, 0, width, height);

        // Creates bitmap to draw in.
        Bitmap bitmap =
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        // Sets default density to avoid scaling.
        bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        return bitmap;
    }

    /**
     * Converts an accuracy value to a text.
     *
     * @param context
     *            The application context.
     * @param accuracy
     *            The accuracy value.
     * @return The text.
     */
    @SuppressLint("DefaultLocale")
    private String getAccuracyText(final Context context, final int accuracy) {
        SparseIntArray map = ACCURACY_TEXT_MAP;
        int index = map.indexOfKey(accuracy);
        return ((index < 0)
                ? String.format("%d", accuracy)
                : context.getString(map.valueAt(index)));
    }
}
