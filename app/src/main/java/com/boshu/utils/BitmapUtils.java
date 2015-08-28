package com.boshu.utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class BitmapUtils {
    public static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public static void write(byte[] bs,String path) {
        try {
        File file=new File(path);
        FileOutputStream out=new FileOutputStream(file);
        out.write(bs);
        out.flush();
        out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
