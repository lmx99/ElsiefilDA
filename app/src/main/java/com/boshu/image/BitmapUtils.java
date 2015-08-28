package com.boshu.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import java.io.ByteArrayOutputStream;


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
	public static Bitmap decodeSampledBitmapFromSDCard(String path) {
		{

			Options opts = new Options();
			opts.inJustDecodeBounds = true;//取回来的bitmap中只有它的宽和高
			BitmapFactory.decodeFile(path, opts);//得到的bitmap只有宽和高度
			opts.inSampleSize =cacluate(opts,480,480);//节约内存，得到缩略图所需的bitmap（我的理解）
			opts.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
			Bitmap bit=decodeSampleBitmapFromBitmap(bitmap);
			if(!bitmap.isRecycled()){
				bitmap.recycle();
			}
			return bit;
		}
	}
		/*public static Bitmap decodeSampledBitmapBitmap(Bitmap image) {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
				baos.reset();//重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
			}
		*/




	public static Bitmap decodeSampleBitmapFromByteArray(byte[] data,int reqWidth,int reqHeight){
		Options opts=new Options();
		opts.inJustDecodeBounds=true;//取回的bitmap中只有宽和高
		BitmapFactory.decodeByteArray(data, 0, data.length,opts);
		int inSampleSize=cacluate(opts, reqWidth, reqHeight);
		opts.inJustDecodeBounds=false;//恢复injustDecodeBounds=false,取回的bitmap是完整的
		opts.inSampleSize=inSampleSize;//
		Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length,opts);
		Bitmap bit=decodeSampleBitmapFromBitmap(bitmap);
		if(!bitmap.isRecycled()){
			bitmap.recycle();
		}
		return bit;
	}
	public static Bitmap decodeSampleBitmapFromBitmap(Bitmap bitmap){
		//byte[] data=com.boshu.utils.BitmapUtils.getBitmapByte(bitmap);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int percent=700*1024*100/baos.toByteArray().length;
		if( baos.toByteArray().length / 1024>700) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
			baos.reset();//重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG,percent, baos);//这里压缩50%，把压缩后的数据存放到baos中
		}
		byte[] data=baos.toByteArray();
		System.out.println(baos.toByteArray().length+"----------888888888");
		baos.reset();
		Options opts=new Options();
		opts.inJustDecodeBounds=true;//取回的bitmap中只有宽和高
		BitmapFactory.decodeByteArray(data, 0, data.length,opts);
		int inSampleSize=cacluate(opts,480,480);
		opts.inJustDecodeBounds=false;//恢复injustDecodeBounds=false,取回的bitmap是完整的
		opts.inSampleSize=inSampleSize;//
		Bitmap bitmap1=BitmapFactory.decodeByteArray(data, 0, data.length,opts);
		return bitmap1;
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