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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.sonyericsson.extras.liveware.aef.registration.Registration.SensorTypeValue;

/**
 * The utility class for sensors.
 */
public final class Sensors {

    /** This class is not designed to be instantiated. */
    private Sensors() {
    }

    /**
     * An immutable set of all {@code SensorTypeValue}s.
     */
    public static final Set<String> ALL_SET = create();

    /**
     * Creates an immutable set of all {@code SensorTypeValue}s.
     *
     * @return An immutable set of all {@code SensorTypeValue}s.
     */
    private static Set<String> create() {
        final String[] all = {
                SensorTypeValue.ACCELEROMETER,
                SensorTypeValue.ROTATION_VECTOR,
                SensorTypeValue.GYROSCOPE,
                SensorTypeValue.MAGNETIC_FIELD,
                SensorTypeValue.LIGHT};
        final Set<String> set = new HashSet<String>(Arrays.asList(all));
        return Collections.unmodifiableSet(set);
    }
}
