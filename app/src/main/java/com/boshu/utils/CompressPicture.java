package com.boshu.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.SoftReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CompressPicture {
    public static  Bitmap compressImage(Bitmap image) {  
        Bitmap bitmap = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 40, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
        int options = 100;  
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
            baos.reset();//重置baos即清空baos  
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
            options -= 10;//每次都减少10  
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
        bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
        
        return bitmap;  
        
    }
    public static void setSoftBitmap(Bitmap pBitmap){
        SoftReference<Bitmap> bitmap;
        bitmap = new SoftReference<Bitmap>(pBitmap);
        if(bitmap != null){
        if(bitmap.get() != null && !bitmap.get().isRecycled()){
        bitmap.get().recycle();
        bitmap = null;
        }
        }
    }

}
