package com.boshu.image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
//文件缓存
public class FileCacheUtils {
	private static final String IMG_CACH_DIR="/imgCache";
	private static String DATA_ROOT_PATH=null;
	private static String SD_ROOT_PATH=Environment.getExternalStorageDirectory().getAbsolutePath();
	private static final int CACHE_SIZE=4;
	private static final String CACHE_TAIL=".cach";
	private static final int NEED_TO_CLEAN=10;
	private Context context;
	private static final String TAG="BitmapFileCacheUtils";
	public FileCacheUtils(Context context){
		this.context=context;
		DATA_ROOT_PATH=context.getCacheDir().getAbsolutePath();
	}
	//得到sdcard中的缓存文件
	public Bitmap getBitmapFromFile(String key){
		if(key==null){
			return null;
		}
		String filename=this.getCacheDirectory()+File.separator+converKeyToFilename(key);
		File file=new File(filename);
		if(file.exists()){
			Bitmap bitmap=BitmapFactory.decodeFile(filename);
			if(bitmap==null){
				file.delete();
			}
			else{
				updateFileModifiedTime(filename);
				Log.i(TAG, "get file from sdcard cache sucess...");
				return bitmap;
			}
		}
		return null;
	}
	//将缓存文件保存sdcard中
	public void addBitmapToFile(String key,Bitmap bm){
		if(bm==null||key==null){//key为路径，bm 位bitmap
			return;
		}
		removeCache(getCacheDirectory());//getCacheDirectory为缓存文件的路劲，
		String filename=converKeyToFilename(key);
		File dir=new File(getCacheDirectory());
		if(!dir.exists()){
			dir.mkdirs();
		}
		File file=new File(dir,filename);
		try {
			OutputStream out=new FileOutputStream(file);
			bm.compress(CompressFormat.JPEG, 100, out);
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	//得到缓存文件的路劲
	private String getCacheDirectory(){
		String cachePath=null;
		if(isSdcardAvailable()){
			cachePath=SD_ROOT_PATH+IMG_CACH_DIR;
		}else{
			cachePath=DATA_ROOT_PATH+IMG_CACH_DIR;
		}
		return cachePath;
	}
	private void removeCache(String dirPath) {
		File dir=new File(dirPath);
		File[] files = dir.listFiles();
		if(files==null){
			return;
		}
		double total_size=0;
		for(File file:files){
			total_size+=file.length();
		}
		total_size=total_size/1024/1024;
		if(total_size>CACHE_SIZE||getSdCardFreeSpace()<=NEED_TO_CLEAN){
			Log.i(TAG, "remove cache from sdcard cache....");
			int removeFactor=(int)(files.length*0.4);
			Arrays.sort(files,new FileLastModifiedComparator());
			for(int i=0;i<removeFactor;i++){
				files[i].delete();
			}
		}
	}
	private int getSdCardFreeSpace(){
		StatFs stat=new StatFs(Environment.getExternalStorageDirectory().getPath());
		double freespace=stat.getAvailableBlocks()*stat.getBlockSize();
		return (int)(freespace/1024/1024);
	}
	private boolean isSdcardAvailable(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	private String converKeyToFilename(String key){
		if(key==null){
			return "";
		}
		return key.hashCode()+CACHE_TAIL;
	}
	private void updateFileModifiedTime(String path){
		File file=new File(path);
		file.setLastModified(System.currentTimeMillis());
	}
	private class FileLastModifiedComparator implements Comparator<File>{

		@Override
		public int compare(File lhs, File rhs) {
			// TODO Auto-generated method stub
			if(lhs.lastModified()>rhs.lastModified()){
				return 1;
			}else if(lhs.lastModified()==rhs.lastModified()){
				return 0;
			}else{
				return -1;
			}
			
		}
		
	}

}
