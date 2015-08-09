package com.boshu.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;

public class BitmapUtils {
    public static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

}
