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

import java.io.File;

import android.os.Environment;
import android.text.format.Time;

/**
 * The default delegate to save data to SD card.
 */
public class DefaultSaver implements Saver {

    /** The folder to create files in. */
    private final File folder;

    /** The prefix of the file name. */
    private final String prefix;

    /** The index number of the file name. */
    private int saveFileIndex = 0;

    /**
     * Creates a new instance.
     */
    public DefaultSaver() {
        folder = new File(Environment.getExternalStorageDirectory(),
                "SampleCameraExtension");
        folder.mkdir();
        prefix = createSaveFilePrefix();
    }

    @Override
    public void save(final byte[] data) {
    }

    @Override
    public final int getIndex() {
        return saveFileIndex;
    }

    @Override
    public final File createJpegFile() {
        return createFile(".jpg");
    }

    /**
     * Creates the file with the specified extension.
     *
     * @param ext
     *            The extension of the file name.
     * @return The file.
     */
    private File createFile(final String ext) {
        String fileName = prefix
                + String.format("%04d", saveFileIndex) + ext;
        ++saveFileIndex;
        return new File(folder, fileName);
    }

    /**
     * Creates the prefix of the file name.
     *
     * @return The prefix of the file name.
     */
    private static String createSaveFilePrefix() {
        Time now = new Time();
        now.setToNow();
        return "samplecamera_" + now.format2445() + "_";
    }
}
