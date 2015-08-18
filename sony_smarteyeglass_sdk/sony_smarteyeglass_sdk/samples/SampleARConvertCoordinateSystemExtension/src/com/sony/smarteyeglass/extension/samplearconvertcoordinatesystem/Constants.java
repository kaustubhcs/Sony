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
package com.sony.smarteyeglass.extension.samplearconvertcoordinatesystem;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

/**
 * Provides constants.
 */
public final class Constants {

    /** The extension key. */
    public static final String EXTENSION_KEY =
            "com.sony.smarteyeglass.e1.extension."
                    + "samplearconvertcoordinatesystem";

    /** The log tag. */
    public static final String LOG_TAG = "SampleARConvertCoordinateSystem";

    /**
     * The map from the resource ID of the preference key to the default value.
     */
    public static final SparseArray<String> ID_TO_DEFAULT_MAP;

    /**
     * The list of the {@code IdPair} objects that maps the resource ID of the
     * string to the default value.
     */
    static final List<IdPair<String>> ID_DEFAULT_PAIR_LIST;

    static {
        List<IdPair<String>> list = new ArrayList<IdPair<String>>();
        list.add(IdPair.of(R.string.preference_key_here_latitude,
                           "35.681074"));
        list.add(IdPair.of(R.string.preference_key_here_longitude,
                           "139.766285"));
        list.add(IdPair.of(R.string.preference_key_destination_latitude,
                           "35.658534"));
        list.add(IdPair.of(R.string.preference_key_destination_longitude,
                           "139.745471"));
        ID_DEFAULT_PAIR_LIST = list;
        ID_TO_DEFAULT_MAP = IdPair.toSparseArray(list);
    }

    /** Hides the default constructor. */
    private Constants() {
    }
}
