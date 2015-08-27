package com.boshu.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.boshu.utils.Model;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDowloader {
	private ExecutorService mImageThreadPool=null;
	private FileCacheUtils fileCacheUtils=null;
	private static final int THREAD_NUM=2;
	private static int REQ_WIDTH;
	private static int REQ_HEIGHT;
	protected static final int DOWNLOAD=10;
	private Context context;
	public ImageDowloader(Context context){
		this.context=context;
		fileCacheUtils=new FileCacheUtils(context);
	}
	public Bitmap downloadImage(int width,int height,final String url,final OnImageDownloadListener listener){
		final String subUrl=url.replaceAll("[^\\w]", "");//
		Bitmap bitmap=showCacheBitmap(subUrl);//
		this.REQ_HEIGHT=height;
		this.REQ_WIDTH=width;
		if(bitmap!=null){
			listener.onImageDownload(url,bitmap);
			return bitmap;
		}else{
			
			new AsyncTask<String, Void,Bitmap>(){//

				@Override
				protected Bitmap doInBackground(String... params) {
					// TODO Auto-generated method stub
					Bitmap bitmap=getImageFromUrl(url);
					fileCacheUtils.addBitmapToFile(subUrl, bitmap);
					BitMapLruCacheHelper.getInstance().addBitmapToMemCache(subUrl, bitmap);//
					return bitmap;
				}
				protected void onPostExecute(Bitmap result){
					listener.onImageDownload(url,result);
				}
				
			}.execute(url);
			final Handler handler=new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					if(msg.what==DOWNLOAD){
						listener.onImageDownload(url,(Bitmap)msg.obj);
					}
					
				}
				
			};
			this.getThreadPooll().execute(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Bitmap bitmap=getImageFromUrl(url);
					Message msg=Message.obtain(handler,DOWNLOAD,bitmap);
					msg.sendToTarget();
					 fileCacheUtils.addBitmapToFile(subUrl, bitmap);
					BitMapLruCacheHelper.getInstance().addBitmapToMemCache(subUrl, bitmap);
				}
			});
		}
		return null;
	}
	public Bitmap downloadImageNew(int width,int height,final String url,final OnImageDownloadListener listener){
		final String subUrl=url.replaceAll("[^\\w]", "");//
		Bitmap bitmap=showCacheBitmap(subUrl);//
		this.REQ_HEIGHT=height;
		this.REQ_WIDTH=width;

		final String urlNew=url.substring(0, url.length()- Model.APPNAME.length());
		System.out.println("新的路径"+urlNew);
		if(bitmap!=null){
			listener.onImageDownload(url,bitmap);
			return bitmap;
		}else{

			new AsyncTask<String, Void,Bitmap>(){//

				@Override
				protected Bitmap doInBackground(String... params) {
					// TODO Auto-generated method stub

					Bitmap bitmap=getImageFromUrl(urlNew);
					fileCacheUtils.addBitmapToFile(subUrl, bitmap);
					BitMapLruCacheHelper.getInstance().addBitmapToMemCache(subUrl, bitmap);//
					return bitmap;
				}
				protected void onPostExecute(Bitmap result){
					listener.onImageDownload(url,result);
				}

			}.execute(urlNew);
			final Handler handler=new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					if(msg.what==DOWNLOAD){
						listener.onImageDownload(url,(Bitmap)msg.obj);
					}

				}

			};
			this.getThreadPooll().execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Bitmap bitmap=getImageFromUrl(urlNew);
					Message msg=Message.obtain(handler,DOWNLOAD,bitmap);
					msg.sendToTarget();
					fileCacheUtils.addBitmapToFile(subUrl, bitmap);
					BitMapLruCacheHelper.getInstance().addBitmapToMemCache(subUrl, bitmap);
				}
			});
		}
		return null;
	}
	public Bitmap showCacheBitmap(String url){
		Bitmap bitmap=BitMapLruCacheHelper.getInstance().getBitmapFromMemCache(url);
		if(bitmap!=null){
			return bitmap;
		}else{
			bitmap=fileCacheUtils.getBitmapFromFile(url);
			if(bitmap!=null){
				BitMapLruCacheHelper.getInstance().addBitmapToMemCache(url, bitmap);
				return bitmap;
			}
		}
		return null;
	}
	public ExecutorService getThreadPooll(){//
		if(mImageThreadPool==null){
			synchronized (ExecutorService.class) {
				if(mImageThreadPool==null){
					mImageThreadPool=Executors.newFixedThreadPool(THREAD_NUM);//
				}
			}
		}
		return mImageThreadPool;
	}
	public Bitmap getImageFromUrl(String url){
		HttpURLConnection conn=null;
		try {
			URL target=new URL(url);
			conn=(HttpURLConnection) target.openConnection();
			conn.setReadTimeout(3000);
			conn.setConnectTimeout(10*1000);
			conn.setDoInput(true);
			if(conn.getResponseCode()==200){
				InputStream is=conn.getInputStream();
				ByteArrayOutputStream bout=new ByteArrayOutputStream();
				int len=0;
				byte[] buf=new byte[1024];
				while((len=is.read(buf))!=-1){
					bout.write(buf,0,len);
				}
				is.close();
				byte[] data=bout.toByteArray();
				return BitmapUtils.decodeSampleBitmapFromByteArray(data,
						REQ_WIDTH, REQ_HEIGHT);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public synchronized void canoellTask(){
		if(mImageThreadPool!=null){
			mImageThreadPool.shutdown();
			mImageThreadPool=null;
		}
	}
	public interface OnImageDownloadListener{
		void onImageDownload(String url, Bitmap bitmap);
	}

}
