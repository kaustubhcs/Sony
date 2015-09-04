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

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

/**
 * Displays a bitmap with provided text information.
 */
public final class DisplayPainter implements BitmapDisplay {

    /** The default text size. */
    private static final int TEXT_SIZE = 16;

    /** The Y point to start to display messages.*/
    private final int pointY;

    /** The X point to start to display messages.*/
    private final int pointX;

    /** The rectangle of the screen size. */
    private final Rect rect;

    /** The delegate to display a bitmap. */
    private BitmapDisplay display;

    /**
     * Creates a new instance.
     *
     * @param context
     *            The application context.
     */
    public DisplayPainter(final Context context) {
        ScreenSize size = new ScreenSize(context);
        Resources res = context.getResources();
        pointX = res.getInteger(R.integer.POINT_X);
        pointY = res.getInteger(R.integer.POINT_Y);
        rect = new Rect(0, 0, size.getWidth(), size.getHeight());
    }

    /**
     * Sets the delegate to display a bitmap.
     *
     * @param display
     *            The delegate to display a bitmap.
     */
    public void setBitmapDisplay(final BitmapDisplay display) {
        this.display = display;
    }

    /**
     * Displays the operation comment on the device.
     *
     * @param list
     *            The list of messages.
     */
    public void paint(final List<String> list) {
        final Bitmap textBitmap = createBitmap();
        final Canvas canvas = new Canvas(textBitmap);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(TEXT_SIZE);
        paint.setColor(Color.WHITE);

        final int x = pointX;
        int y = pointY;
        for (String m : list) {
            canvas.drawText(m, x, y, paint);
            y += pointY;
        }
        display.displayBitmap(textBitmap);
    }

    /**
     * Displays the specified bitmap.
     *
     * @param bitmap
     *            The bitmap.
     */
    @Override
    public void displayBitmap(final Bitmap bitmap) {
        final Bitmap captureBitmap = createBitmap();
        final Canvas canvas = new Canvas(captureBitmap);
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        canvas.drawBitmap(bitmap, rect, rect, null);
        display.displayBitmap(captureBitmap);
    }

    /**
     * Creates a bitmap of the ARGB_8888 format.
     *
     * @return A bitmap.
     */
    private Bitmap createBitmap() {
        final int width = rect.width();
        final int height = rect.height();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        b.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return b;
    }
}
