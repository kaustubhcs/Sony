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
package com.example.sony.smarteyeglass.extension.hellolayouts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates how to render an XML-based layout on the SmartEyeglass display.
 * While the display data is supplied in an Android layout, you can convert
 * it to a bitmap for rendering, if your app must be compatible with a device
 * such as SmartWatch that does not support layouts, or for special customized
 * displays.
 * This sample demonstrates both rendering methods.
 *
 * The resources for this sample include two display data files, bitmap.xml
 * and layout.xml, which demonstrate all standard UI components that can
 * be used on SmartEyeglass, except Gallery.
 *
 * The updateLayout() method uses the layout.xml resource to display a screen
 * and three lines of text: LAYOUT in a large font, and smaller text lines
 * "Swipe left to change to bitmap" and "Tap to update". The handlers for these
 * actions call updateBitmap(), which uses the bitmap render method to show the
 * corresponding BITMAP screen.
 */
public final class HelloLayoutsControl extends ControlExtension {

    /** Quality parameter for encoding PNG data. */
    private static final int PNG_QUALITY = 100;

    /** Size of initial buffer for encoding PNG data. */
    private static final int PNG_DEFAULT_CAPACITY = 256;

    /** Renders display data to the SmartEyeglass screen using
     *  the layout method.
     */
    private final Renderer layoutRenderer = new Renderer() {
        // Render layout when an update is needed
        @Override
        public void init() {
            updateLayout();
        }
        // Executes the rendering of a layout onto the display
        // one view at a time.
        @Override
        public void update() {
            // Counter tracks which screen is to be shown
            int count = state.getCount();

            // Update the text for this screen
            // (rather than the entire layout).
            sendText(R.id.btn_update_this, getCaption(count));

            // Update the image of an ImageView in the screen
            sendImage(R.id.image, state.getImageId());
        }
        // Retrieve the name of the Renderer
        @Override
        public String toString() {
            return "LAYOUT";
        }
    };

    /** Renders display data to the SmartEyeglass screen using
     *  the bitmap method.
     */
    private final Renderer bitmapRenderer = new Renderer() {
        // Render layout when an update is needed
        @Override
        public void init() {
            updateBitmap();
        }
        // Render display data using bitmap method
        @Override
        public void update() {
            updateBitmap();
        }
        // Retrieve the name of the Renderer
        @Override
        public String toString() {
            return "BITMAP";
        }
    };

    /** Map swipe direction to rendering method. */
    private final SparseArray<Renderer> rendererMap =
            new SparseArray<Renderer>();

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** The application context. */
    private final Context context;

    /** Manages the counter and the icon image */
    private final State state;

    /** The chosen renderer object, for either layout or bitmap method. */
    private Renderer renderer;

    /**
     * Instantiates a control object, initializing the rendering-method map
     * so that a swipe-left action renders a bitmap, and the swipe-right
     * action renders a layout.
     *
     * @param context            The context.
     * @param hostAppPackageName Package name of SmartEyeglass host application.
     */
    public HelloLayoutsControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
        state = new State();
        rendererMap.put(Control.Intents.SWIPE_DIRECTION_LEFT, bitmapRenderer);
        rendererMap.put(Control.Intents.SWIPE_DIRECTION_RIGHT, layoutRenderer);
    }

    // Reset state object and assign initial renderer object
    @Override
    public void onStart() {
        state.reset();
        renderer = layoutRenderer;
    }

    // Update the display when app becomes visible, using the
    // current render method.
    @Override
    public void onResume() {
        // Send a UI when the extension becomes visible.
        renderer.init();
        super.onResume();
    }

    // Clean up data structures on termination.
    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy: HelloLayoutsControl");
        utils.deactivate();
    };

    // Respond to touch on controller by updating the display
    // using the current rendering method.
    @Override
    public void onTouch(final ControlTouchEvent event) {
        super.onTouch(event);

        Log.d(Constants.LOG_TAG,
                "onTouch: HelloLayoutsControl " + renderer
                + " - " + event.getX()
                + ", " + event.getY());

        if (event.getAction() != Control.Intents.TOUCH_ACTION_RELEASE) {
            return;
        }

        // Switch the state of icon and update the display using current
        // render method.
        state.update();
        renderer.update();
    }

    // Respond to swipe on controller by setting
    // the rendering method.
    @Override
    public void onSwipe(final int direction) {
        Renderer next = rendererMap.get(direction);
        if (next == null) {
            return;
        }
        renderer = next;

        // Reset state object after change of rendering method
        state.reset();
        // Initialize and update display using the render method
        renderer.init();
    }

    /**
     * Renders a bitmap to the display.
     * Populates a layout to compose the image and text,
     * then converts it to a bitmap for display with showBitmap().
     */
    private void updateBitmap() {
        // Initialize layout display parameters
        RelativeLayout root = new RelativeLayout(context);
        root.setLayoutParams(new LayoutParams(
                R.dimen.smarteyeglass_control_width,
                R.dimen.smarteyeglass_control_height));

        // Set dimensions and properties of the bitmap to fit the screen.
        final ScreenSize size = new ScreenSize(context);
        final int width = size.getWidth();
        final int height = size.getHeight();
        Bitmap bitmap =
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.compress(CompressFormat.PNG, PNG_QUALITY,
                new ByteArrayOutputStream(PNG_DEFAULT_CAPACITY));
        bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        // Use the bitmap.xml layout resource as a base.
        RelativeLayout layout = (RelativeLayout) RelativeLayout.inflate(context,
                R.layout.bitmap, root);
        // Sets dimensions of the layout to those of the bitmap.
        layout.measure(height, width);
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());

        // Adds 1 to counter value and retrieves it
        int count = state.getCount();
        // Set the caption text for this view in the layout's TextView element
        if (count > 0) {
            TextView textView =
                    (TextView) layout.findViewById(R.id.btn_update_this);
            textView.setText(getCaption(count));
        }

        // Set the icon to add to the layout's ImageView element.
        ImageView imageView = (ImageView) layout.findViewById(R.id.image);
        imageView.setImageResource(state.getImageId());

        // Convert the entire layout to a bitmap using a canvas.
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        // Update the screen to display the bitmap
        utils.showBitmap(bitmap);
    }

    /**
     * Renders a layout to the SmartEyeglass display.
     * Displays an ImageView (a button) and a TextView (a caption).
     * For each view you would like to customize, pass a layout reference
     * ID and a data bundle to showLayout().
     *
     * When you use the layout method for displaying TextView
     * elements, they are rendered using an optimized SST font.
     *
     * @see Control.Intents#EXTRA_DATA_XML_LAYOUT
     * @see Registration.LayoutSupport
     */
    private void updateLayout() {
        // retrieve the caption string
        String caption = context.getString(R.string.text_tap_to_update);

        List<Bundle> list = new ArrayList<Bundle>();

        // Prepare a bundle to update the TextView for the button caption.
        Bundle textBundle = new Bundle();
        textBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE,
                R.id.btn_update_this);
        textBundle.putString(Control.Intents.EXTRA_TEXT, caption);
        list.add(textBundle);

        // Prepare a bundle to update the ImageView for the button icon.
        Bundle imageBundle = new Bundle();
        imageBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.image);
        imageBundle.putString(Control.Intents.EXTRA_DATA_URI,
                getUriString(R.drawable.icon_extension48));
        list.add(imageBundle);

        // Display the view elements from layout.xml
        showLayout(R.layout.layout, list.toArray(new Bundle[list.size()]));
    }

    /**
     * Extracts a display string for the current screen from resources.
     *
     * @param count The current screen.
     * @return      The display string.
     */
    private String getCaption(final int count) {
        return context.getString(R.string.text_tap_to_update) + " : " + count;
    }

    /**
     * Retrieves the URI string corresponding to a resource ID.
     *
     * @param id The resource ID.
     * @return   The URI string.
     */
    private String getUriString(final int id) {
        return ExtensionUtils.getUriString(context, id);
    }
}
