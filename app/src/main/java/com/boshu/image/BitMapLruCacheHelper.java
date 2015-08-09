package com.boshu.image;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
//判断内存中是否有图片
public class BitMapLruCacheHelper {
	int a;
	private static final String TAG=null;
	private static BitMapLruCacheHelper instance=new BitMapLruCacheHelper();
	private LruCache<String,Bitmap> cache=null;
	private BitMapLruCacheHelper(){
		
		//开辟内存的8分之1作为缓存的内存
		int maxSize=(int) (Runtime.getRuntime().maxMemory()/8);
		cache=new LruCache<String,Bitmap>(maxSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				return a=value.getRowBytes()*value.getHeight();
			   
			}
			
			
		};
	}
	//将图片加入到手机内存中
	public void addBitmapToMemCache(String key,Bitmap value){
		if(key==null||value==null){
			return;
		}
		if(cache!=null&& getBitmapFromMemCache(key)==null){
			cache.put(key, value);
			Log.i(TAG, "put to lrucache success");
		}
	}
	//得到内存中的图片
	public Bitmap getBitmapFromMemCache(String key) {
		// TODO Auto-generated method stub
		if(key==null){
			return null;
		}
		Bitmap bitmap=cache.get(key);
		Log.i(TAG, "from lrucache,bitmap="+bitmap);
		return bitmap;
	}
	public static BitMapLruCacheHelper getInstance(){
		return instance;
	}
	
	  

}
