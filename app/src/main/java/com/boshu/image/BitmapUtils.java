package com.boshu.image;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;


public class BitmapUtils {
	public static Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,
			int reqHeight)
	{
		Options opts=new Options();
		opts.inJustDecodeBounds=true;//取回来的bitmap中只有它的宽和高
		BitmapFactory.decodeResource(res, resId,opts);//得到的bitmap只有宽和高度
		int inSampleSize=cacluate(opts, reqWidth, reqHeight);
		opts.inSampleSize=inSampleSize;//节约内存，得到缩略图所需的bitmap（我的理解）
		opts.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeResource(res, resId,opts);
		
		return bitmap;


}
	public static Bitmap decodeSampledBitmapFromSDCard(String path,int reqWidth,int reqHeight){
	
		Options opts=new Options();
		opts.inJustDecodeBounds=true;//取回来的bitmap中只有它的宽和高
		BitmapFactory.decodeFile(path, opts);//得到的bitmap只有宽和高度
		int inSampleSize=cacluate(opts, reqWidth, reqHeight);
		opts.inSampleSize=inSampleSize;//节约内存，得到缩略图所需的bitmap（我的理解）
		opts.inJustDecodeBounds=false;
		Bitmap bitmap=BitmapFactory.decodeFile(path, opts);;
		return bitmap;
	
	}
	public static Bitmap decodeSampledBitmapFromSDCard(String path){
	    {
	        Options opts=new Options();
	        opts.inJustDecodeBounds=true;//取回来的bitmap中只有它的宽和高
	        BitmapFactory.decodeFile(path, opts);//得到的bitmap只有宽和高度
	        int inSampleSize=2;
	        opts.inSampleSize=inSampleSize;//节约内存，得到缩略图所需的bitmap（我的理解）
	        opts.inJustDecodeBounds=false;
	        Bitmap bitmap=BitmapFactory.decodeFile(path, opts);;
	        return bitmap;
	    }
		
		
	

}
	public static Bitmap decodeSampleBitmapFromByteArray(byte[] data,int reqWidth,int reqHeight){
		Options opts=new Options();
		opts.inJustDecodeBounds=true;//取回的bitmap中只有宽和高
		BitmapFactory.decodeByteArray(data, 0, data.length,opts);
		int inSampleSize=cacluate(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds=false;//恢复injustDecodeBounds=false,取回的bitmap是完整的
		opts.inSampleSize=inSampleSize;//
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length,opts);
		return bitmap;
	}
	public static int cacluate(Options opts,int reqWith,int reqHeight){
		if(opts==null)
			return 1;
			int inSampleSize=1;
			int realWidth=opts.outWidth;//得到原始图片的宽度
			int realHeight=opts.outHeight;//得到原始图片的高度
			if(realHeight>reqHeight||realWidth>reqWith){
				int heightRatio=realHeight/reqHeight;
				int widthRation=realWidth/reqWith;
				inSampleSize=(heightRatio>widthRation)?widthRation:heightRatio;
				//A>B? result1:result2;     如果A大于B，结果是result1，否则是result2
			
			}
			
		
		return inSampleSize;
	}
}