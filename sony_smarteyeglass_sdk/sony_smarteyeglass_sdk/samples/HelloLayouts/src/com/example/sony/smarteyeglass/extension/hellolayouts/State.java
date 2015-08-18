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

/**
 * The state of the application.
 */
public final class State {

    /** Contains the counter value shown in the UI. */
    private int count;

    /** Used to toggle if an icon will be visible in the bitmap UI. */
    private boolean iconImage;

    /**
     * Creates a new instance.
     */
    public State() {
        reset();
    }

    /**
     * Resets the state.
     */
    public void reset() {
        count = 0;
        iconImage = true;
    }

    /**
     * Updates the state. This method increments the counter and toggles the
     * icon.
     */
    public void update() {
        ++count;
        iconImage = !iconImage;
    }

    /**
     * Returns the value of the counter.
     *
     * @return The counter.
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns the resource ID of the icon.
     *
     * @return The resource ID of the icon.
     */
    public int getImageId() {
        return (iconImage) ? R.drawable.icon_extension48
                           : R.drawable.actions_view_in_phone;
    }
}
