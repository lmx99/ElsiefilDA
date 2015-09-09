package com.boshu.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * Created by amou on 18/8/2015.
 */
public class upteVersionDialog {
    private String TAG="upteVersionDialog";
    private  AlertDialog alertDialog;
    private String urlApk;
    private String appName;
    private String verCode;
    private String verNameApk;
    private String verName;
    public static upteVersionDialog getInstance(){
        upteVersionDialog instance=new upteVersionDialog();
        return instance;
    }

    public AlertDialog setAlertDialog(final Context context,WindowManager window,LayoutInflater inflater) {
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / 1.2);
        int height = (int) (dm.heightPixels / 2.8);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = inflater.inflate(
                R.layout.alterdialog_boshu_update, null);
        Button no = (Button) view.findViewById(R.id.bt_boshu_updateno);
        Button yes = (Button) view.findViewById(R.id.bt_boshu_updateyes);
       TextView tv= (TextView) view.findViewById(R.id.tv_boshu_cude);
        tv.setText("当前版本为"+VersionUtils.getVerName(context)+",检测到有新版本"+verName+",是否更新");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dowloadApk(appName,urlApk,context);
                alertDialog.dismiss();

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams params = alertDialog.getWindow()
                .getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = width;//
        params.height = height;//
        alertDialog.getWindow().setAttributes(params);
        return alertDialog;
    }
    public void postVersion(final Context context, final WindowManager window, final LayoutInflater inflater, final boolean toast) {

            final ProgressDialog pd = new ProgressDialog(context);
            if (toast == true) {
                pd.setMessage("正在检测中~");
                pd.setCanceledOnTouchOutside(true);
                pd.setCancelable(true);
                pd.show();
            }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
        requestQueue.add(new AutoLoginRequest(context, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                try {
                    String status = response.getString("status");
                    if (status.equals("0")) {
                        urlApk = response.getString("url");
                        appName = response.getString("appName");
                        String[] appNameSpl = appName.split("_");
                        verCode = appNameSpl[1];
                        verNameApk = appNameSpl[2];
                        verName = verNameApk.substring(0, verNameApk.length() - 4);
                        if (VersionUtils.getService(verCode, VersionUtils.getVerCode(context))) {
                           setAlertDialog(context,window,inflater);
                            if(toast==true) {
                                pd.dismiss();
                            }
                        } else {
                            if(toast==true) {

                                Toast.makeText(context, "已经为最新版本.么么哒！", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    } else {
                        Toast.makeText(context, "更新：未知错误", Toast.LENGTH_SHORT).show();
                        if(toast==true) {
                            pd.dismiss();
                        }

                    }


                } catch (Exception e) {
                    Toast.makeText(context, "更新：服务器出错！", Toast.LENGTH_SHORT).show();
                    if(toast==true) {
                        pd.dismiss();
                    }

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error: " + error);


            }
        }) {

            @Override
            protected void setParams(Map<String, String> params) {
                params.put("sys", "user");
                params.put("ctrl", "user");
                params.put("action", "upd_ver");
            }
        });
    }

    private void dowloadApk(String apkName, String url, final Context context) {
        final   ProgressDialog  mProgressDialog = new ProgressDialog(context);
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + apkName;
        File f = new File(apkPath);
      /*if (!f.exists()) {
          // 如果路径不存在,则创建
          f.mkdirs();
      }*/

        if (f.exists()) {
            f.delete();
        }
        FinalHttp fh = new FinalHttp();
        //调用download方法开始下载
        HttpHandler handler = fh.download(url, apkPath, true, new AjaxCallBack<File>() {
            @Override
            public boolean isProgress() {
                return super.isProgress();
            }

            @Override
            public void onLoading(long count, long current) {
                int progress;
                if (current != count && current != 0) {
                    progress = (int) (current / (float) count * 100);
                    mProgressDialog.setProgress(progress);
                } else {
                    progress = 100;
                    mProgressDialog.setProgress(progress);
                    mProgressDialog.dismiss();
                }


                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(File file) {
                installApk(file, context);
                super.onSuccess(file);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                System.out.println(t.toString() + "---" + errorNo + "==" + strMsg);
                super.onFailure(t, errorNo, strMsg);
            }

            @Override
            public void onStart() {
                mProgressDialog.setMessage("正在更新软件...");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
                super.onStart();
            }

            @Override
            public AjaxCallBack<File> progress(boolean progress, int rate) {
                System.out.println("rate=" + rate);
                return super.progress(progress, rate);

            }
        });
        //调用stop()方法停止下载
    }

    private void installApk(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
