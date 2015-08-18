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
package com.example.sony.smarteyeglass.extension.hellonotification;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides the data of the notification event.
 */
public final class Event {

    /** The name of the event. */
    private final String name;

    /** The message of the event. */
    private final String message;

    /**
     * Creates a new instance with the specified name and message.
     *
     * @param name
     *            The name of the event.
     * @param message
     *            The message of the event.
     */
    public Event(final String name, final String message) {
        this.name = name;
        this.message = message;
    }

    /**
     * Returns the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the message.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the array of all events.
     *
     * @return The array of all events.
     */
    public static Event[] createAllEvents() {
        List<Event> list = new ArrayList<Event>();
        list.add(new Event("Name A", "Message 1"));
        list.add(new Event("Name B", "Message 2"));
        list.add(new Event("Name C", "Message 3"));
        list.add(new Event("Name D", "Message 4"));
        list.add(new Event("Name E", "Message 5"));
        list.add(new Event("Name F", "Message 6"));
        return list.toArray(new Event[list.size()]);
    }
}
