package com.boshu.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by amou on 16/8/2015.
 */
public class VersionUtils {
    private static String TAG="VersionUtils";
    //获取版本号
    public static int getVerCode(Context context) {
    int verCode = -1;
        try{
            verCode=context.getPackageManager().getPackageInfo("com.lifeisle.android",0).versionCode;
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }

    return verCode;
}
    //获取版本名称
    public static String getVerName(Context context) {
        String verName = "";
        try{
            verName=context.getPackageManager().getPackageInfo("com.lifeisle.android",0).versionName;
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return verName;
    }
    public static boolean getService(String newCode,int oldCode){
        if(Integer.parseInt(newCode)>oldCode){
            return true;
        }else{
            return false;
        }

    }
}
