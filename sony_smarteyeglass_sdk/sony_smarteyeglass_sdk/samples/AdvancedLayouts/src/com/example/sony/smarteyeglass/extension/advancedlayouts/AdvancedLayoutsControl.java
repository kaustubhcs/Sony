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
package com.example.sony.smarteyeglass.extension.advancedlayouts;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.aef.control.Control.Intents;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * AdvancedLayouts displays a swipe-able gallery, based on a string array.
 */
public final class AdvancedLayoutsControl extends ControlExtension {

    /** String array with dummy data to be displayed in gallery. */
    private static final String[] GALLERY_CONTENT = {
            "Detail Item 1",
            "Detail Item 2",
            "Detail Item 3",
            "Detail Item 4",
            "Detail Item 5",
            "Detail Item 6",
            "Detail Item 7",
            "Detail Item 8",
            "Detail Item 9",
            "Detail Item 10"};

    /** Dummy message. */
    private static final String DETAIL_MESSAGE =
            "Advanced Layouts Sample extension : %s ";

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** Uses SmartEyeglass API version*/
    private static final int SMARTEYEGLASS_API_VERSION = 1;

    /** The position of the displayed item inside the array/database. */
    private int lastPosition = 0;

    /** Flag for list view or detailed view. */
    private boolean showingDetail;

    /**
     * Creates Advanced Layout control.
     *
     * @param context
     *            The application context.
     * @param hostAppPackageName
     *            The package name;
     */
    public AdvancedLayoutsControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.setRequiredApiVersion(SMARTEYEGLASS_API_VERSION);
        utils.activate(context);
    }

    @Override
    public void onDestroy() {
        utils.deactivate();
    }

    @Override
    public void onResume() {
        int startPosition = 0;
        showingDetail = false;

        // Set the layout.
        showLayout(R.layout.smarteyeglass_layout_test_gallery, null);

        // Layout sets the list.
        sendListCount(R.id.gallery, GALLERY_CONTENT.length);

        // If requested, move to the correct position in the list.
        lastPosition = startPosition;

        // Set location of the beginning.
        sendListPosition(R.id.gallery, startPosition);

        // For scrollable text view.
        utils.sendTextViewLayoutId(R.id.body);
    }

    @Override
    public void onRequestListItem(
            final int layoutReference, final int listItemPosition) {
        Log.d(Constants.LOG_TAG,
                "onRequestListItem() - position " + listItemPosition);
        if (layoutReference == -1 || layoutReference != R.id.gallery) {
            return;
        }
        if (listItemPosition == -1) {
            return;
        }
        ControlListItem item = createControlListItem(listItemPosition);
        if (item == null) {
            return;
        }
        sendListItem(item);
    }

    @Override
    public void onListItemSelected(final ControlListItem listItem) {
        super.onListItemSelected(listItem);
        // We save the last "selected" position, this is the current visible
        // list item index. The position can later be used on resume
        lastPosition = listItem.listItemPosition;
    }

    @Override
    public void onListItemClick(
            final ControlListItem listItem,
            final int clickType,
            final int itemLayoutReference) {
        Log.d(Constants.LOG_TAG,
                "Item clicked. Position " + listItem.listItemPosition
                + ", itemLayoutReference " + itemLayoutReference
                + ". Type was: "
                + (clickType == Intents.CLICK_TYPE_SHORT ? "SHORT" : "LONG"));
        lastPosition = listItem.listItemPosition;

        if (clickType == Intents.CLICK_TYPE_LONG) {
            return;
        }

        // Dummy message.
        Bundle messageBundle = new Bundle();
        messageBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.message);
        messageBundle.putString(Intents.EXTRA_TEXT,
                "No:" + Integer.toString(listItem.listItemPosition + 1));

        List<Bundle> list = Collections.singletonList(messageBundle);
        utils.moveLowerLayer(R.layout.smarteyeglass_layout_test_detail,
                list.toArray(new Bundle[list.size()]));
        showingDetail = true;
    }

    @Override
    public void onKey(
            final int action, final int keyCode, final long timeStamp) {
        if (action != Intents.KEY_ACTION_RELEASE
                || keyCode != Control.KeyCodes.KEYCODE_BACK) {
            return;
        }
        Log.d(Constants.LOG_TAG, "onKey() - back button intercepted.");
        if (!showingDetail) {
            stopRequest();
            return;
        }
        showingDetail = false;
        ControlListItem item = createControlListItem(lastPosition);
        utils.moveUpperLayer(item.dataXmlLayout, item.layoutData);
        showLayout(R.layout.smarteyeglass_layout_test_gallery, null);
        sendListCount(R.id.gallery, GALLERY_CONTENT.length);
        sendListPosition(R.id.gallery, lastPosition);
        utils.sendTextViewLayoutId(R.id.body);
        sendListItem(item);
    }

    @Override
    public void onTap(final int action, final long timeStamp) {
        if (action != Control.TapActions.SINGLE_TAP) {
            return;
        }
        Log.d(Constants.LOG_TAG, "tapactions:" + action);
        if (!showingDetail) {
            return;
        }
        // Show detail view
        utils.moveLowerLayer(R.layout.smarteyeglass_layout_test_gallery, null);
        sendListCount(R.id.gallery, GALLERY_CONTENT.length);

        // If requested, move to the correct position in the list.
        sendListPosition(R.id.gallery, lastPosition);

        // For scrollable text view
        utils.sendTextViewLayoutId(R.id.body);

        showingDetail = false;
    }

    /**
     * Creates {@code ControlListItem} object for the specified position.
     *
     * @param position
     *            The position.
     * @return The {@code ControlListItem} object.
     */
    private ControlListItem createControlListItem(final int position) {
        Log.d(Constants.LOG_TAG, "position = " + position);

        // Creates the repeating dummy text.
        String text = String.format(DETAIL_MESSAGE, GALLERY_CONTENT[position]);
        StringBuilder b = new StringBuilder(text);
        for (int i = 0; i < position; ++i) {
            b.append(b);
        }

        ControlListItem item = new ControlListItem();
        item.layoutReference = R.id.gallery;
        item.dataXmlLayout = R.layout.smarteyeglass_item_gallery;
        item.listItemId = position;
        item.listItemPosition = position;

        List<Bundle> list = new ArrayList<Bundle>();

        // Header data.
        Bundle headerBundle = new Bundle();
        headerBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.title);
        headerBundle.putString(Intents.EXTRA_TEXT, GALLERY_CONTENT[position]);
        list.add(headerBundle);

        // Body data.
        Bundle bodyBundle = new Bundle();
        bodyBundle.putInt(Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
        bodyBundle.putString(Intents.EXTRA_TEXT, b.toString());
        list.add(bodyBundle);

        item.layoutData = list.toArray(new Bundle[list.size()]);
        return item;
    }
}
