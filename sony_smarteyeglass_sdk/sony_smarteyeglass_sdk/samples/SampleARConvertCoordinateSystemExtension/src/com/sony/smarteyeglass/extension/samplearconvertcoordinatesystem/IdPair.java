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

import java.util.Collection;

import android.util.SparseArray;

/**
 * The pair of a resource ID and the value associated with the ID.
 *
 * @param <T>
 *            The type of the value associated with the ID.
 */
public final class IdPair<T> {

    /** The resource ID. */
    private int id;

    /** The value associated with the ID. */
    private T value;

    /**
     * Creates a new instance.
     *
     * @param id
     *            The resource ID.
     * @param value
     *            The value associated with the ID.
     */
    private IdPair(final int id, final T value) {
        this.id = id;
        this.value = value;
    }

    /**
     * Creates a new instance.
     *
     * @param id
     *            The resource ID.
     * @param value
     *            The value associated with the ID.
     * @return The new {@code IdPair} object.
     * @param <T>
     *            The type of the value associated with the ID.
     */
    public static <T> IdPair<T> of(final int id, final T value) {
        return new IdPair<T>(id, value);
    }

    /**
     * Returns the resource ID.
     *
     * @return the resource ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the value associated with the resource ID.
     * @return The value associated with the resource ID.
     */
    public T getValue() {
        return value;
    }

    /**
     * Creates the map from the resource ID to the value with the specified
     * collection of {@code IdPair} objects.
     *
     * @param all
     *            The collection of {@code IdPair} objects.
     * @return The map from the resource ID to the value.
     * @param <T>
     *            The type of the value associated with the ID.
     */
    public static <T> SparseArray<T> toSparseArray(
            final Collection<IdPair<T>> all) {
        SparseArray<T> a = new SparseArray<T>(all.size());
        for (IdPair<T> p : all) {
            a.put(p.id, p.value);
        }
        return a;
    }
}
