package com.dealwala.main.dealwala.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

public class CircleTransform implements Transformation {

    public String key() {
        return "circle";
    }

    public Bitmap transform(Bitmap paramBitmap) {
        int i = Math.min(paramBitmap.getWidth(), paramBitmap.getHeight());
        Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, (paramBitmap.getWidth() - i) / 2, (paramBitmap.getHeight() - i) / 2, i, i);
        if (localBitmap != paramBitmap) {
            paramBitmap.recycle();
        }
        paramBitmap = Bitmap.createBitmap(i, i, paramBitmap.getConfig());
        Canvas localCanvas = new Canvas(paramBitmap);
        Paint localPaint = new Paint();
        localPaint.setShader(new BitmapShader(localBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        localPaint.setAntiAlias(true);
        float f = i / 2.0F;
        localCanvas.drawCircle(f, f, f, localPaint);
        localBitmap.recycle();
        return paramBitmap;
    }
}