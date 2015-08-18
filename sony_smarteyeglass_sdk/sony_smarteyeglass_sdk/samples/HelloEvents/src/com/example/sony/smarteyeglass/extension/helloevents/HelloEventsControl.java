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
package com.example.sony.smarteyeglass.extension.helloevents;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;

import com.example.sonymobile.smartextension.helloevents.R;
import com.sonyericsson.extras.liveware.aef.control.Control.KeyCodes;
import com.sonyericsson.extras.liveware.aef.control.Control.Intents;
import com.sonyericsson.extras.liveware.aef.control.Control.TapActions;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;

/**
 * Demonstrates how to handle user-interaction events
 * such as touch, tap, swipe and hardware key presses.
 */
public final class HelloEventsControl extends ControlExtension {

    /**
     * Constants that describe the basic type of an event that triggers
     *  notification.
     */
    private enum EventType {

        /** A touch on the touch sensor triggers the onTouch callback. */
        TOUCH,

        /** A swipe motion on the touch sensor triggers the onSwipe callback. */
        SWIPE,

        /** A specific key-press action returned to the onKey calback. */
        KEY_ACTION,

        /** The key identifier returned to the onKey calback. */
        KEYCODE,

        /** A tap on the touch sensor triggers the onTap callback. */
        TAP;
    }

    /**
     * A map that associated events with the resource IDs of their
     * description strings
     */
    private final Map<EventType, SparseIntArray> eventsMap =
            new HashMap<EventType, SparseIntArray>();

    /**
     * Creates an instance of this control class, initializing the event map
     * with specific user actions that result in event notifications.
     *
     * @param hostAppPackageName Package name of the SmartEyeglass host
     *        application.
     * @param context            The context.
     */
    public HelloEventsControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);

        // Map string resource IDs to specific user interaction events.

        // Map TOUCH events: press, press-and-hold, release
        SparseIntArray touchMap = new SparseIntArray();
        touchMap.put(Intents.TOUCH_ACTION_PRESS, R.string.touch_action_press);
        touchMap.put(Intents.TOUCH_ACTION_LONGPRESS,
                R.string.touch_action_longpress);
        touchMap.put(Intents.TOUCH_ACTION_RELEASE,
                R.string.touch_action_release);
        eventsMap.put(EventType.TOUCH, touchMap);

        // Map SWIPE events: swipe left, swipe right
        SparseIntArray swipeMap = new SparseIntArray();
        swipeMap.put(Intents.SWIPE_DIRECTION_LEFT,
                R.string.swipe_direction_left);
        swipeMap.put(Intents.SWIPE_DIRECTION_RIGHT,
                R.string.swipe_direction_right);
        eventsMap.put(EventType.SWIPE, swipeMap);

        // Map KEY_ACTION events: press, release
        SparseIntArray keyActionMap = new SparseIntArray();
        keyActionMap.put(Intents.KEY_ACTION_PRESS, R.string.key_action_press);
        keyActionMap.put(Intents.KEY_ACTION_RELEASE,
                R.string.key_action_release);
        eventsMap.put(EventType.KEY_ACTION, keyActionMap);

        // Map KEYCODE events: Back key, Talk key
        SparseIntArray keyCodeMap = new SparseIntArray();
        keyCodeMap.put(KeyCodes.KEYCODE_BACK, R.string.keycode_back);
        keyCodeMap.put(KeyCodes.KEYCODE_PTT, R.string.keycode_ptt);
        eventsMap.put(EventType.KEYCODE, keyCodeMap);

        // Map TAP events: only the single tap is supported
        SparseIntArray tapMap = new SparseIntArray();
        tapMap.put(TapActions.SINGLE_TAP, R.string.tap_action_single_tap);
        eventsMap.put(EventType.TAP, tapMap);
    }

    // Display the Hello screen when this app becomes active.
    @Override
    public void onResume() {
        // Send a UI when the extension becomes visible.
        showLayout(R.layout.hello_events_control, null);
    }

    // Respond to a TOUCH event by showing the string associated
    // with the specific user action
    @Override
    public void onTouch(final ControlTouchEvent event) {
        Log.d(Constants.LOG_TAG, "onTouch() " + event.getAction());
        updateText(getTextResource(EventType.TOUCH, event.getAction()),
            R.string.empty);
    }

    // Respond to a TAP event by showing the string associated
    // with the specific user action
    @Override
    public void onTap(final int action, final long timeStamp) {
        Log.d(Constants.LOG_TAG, "onTap() " + action);
        updateText(getTextResource(EventType.TAP, action), R.string.empty);
    }

    // Respond to a key-press event by showing the strings associated
    // with the specific user action on a specific key
    @Override
    public void onKey(final int action, final int keyCode,
            final long timeStamp) {
        Log.d(Constants.LOG_TAG, "onKey()");
        updateText(getTextResource(EventType.KEYCODE, keyCode),
                getTextResource(EventType.KEY_ACTION, action));
        if (action == Intents.KEY_ACTION_RELEASE
                && keyCode == KeyCodes.KEYCODE_BACK) {
            stopRequest();
        }
    }

    // Respond to a SWIPE event by showing the string associated
    // with the specific user action
    @Override
    public void onSwipe(final int direction) {
        Log.d(Constants.LOG_TAG, "onSwipe()");
        updateText(getTextResource(EventType.SWIPE, direction), R.string.empty);
    }

    /**
     * Retrieves the resource ID of the description string that was mapped to
     *  a specific event.
     *
     * @param type     The type constant returned in the Event object.
     * @param eventId  The unique event ID returned in the Event object.
     * @return         The ID of text resource.
     */
    private int getTextResource(final EventType type, final int eventId) {
        SparseIntArray map = eventsMap.get(type);
        int index = map.indexOfKey(eventId);
        return ((index < 0) ? R.string.unknown : map.valueAt(index));
    }

    /**
     * Updates the text to display in response to an event notification.
     *
     * @param eventId The ID of the text resource associated with the event.
     * @param keyId   Optional, the ID of the text resource for the key code.
     *                Available only for key-press events; otherwise,
     *                pass the empty string.
     */
    private void updateText(final int eventId, final int keyId) {
        sendText(R.id.control_event, mContext.getString(eventId));
        sendText(R.id.control_event_key, mContext.getString(keyId));
    }
}
