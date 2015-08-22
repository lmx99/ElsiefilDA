package com.boshu.utils;

import android.app.ProgressDialog;

/**
 * Created by amou on 20/8/2015.
 */
public class CudeUtils {
    public  void setCudeEnd(long current,long count,ProgressDialog mProgressDialog){

        int progress;
        if (current != count && current != 0) {
            progress = (int) (current / (float) count * 100);
            mProgressDialog.setProgress(progress);
        } else {
            progress = 100;
            mProgressDialog.setProgress(progress);
            mProgressDialog.dismiss();
        }
    }
    public  ProgressDialog setCudeStart(ProgressDialog mProgressDialog){
        mProgressDialog.setMessage("正在更新软件...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        return mProgressDialog;
    }
}
