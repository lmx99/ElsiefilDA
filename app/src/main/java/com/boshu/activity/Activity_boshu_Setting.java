package com.boshu.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.MyApplication;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.chatuidemo.activity.MainActivity;
import com.lifeisle.android.R;

/**
 * Created by amou on 14/8/2015.
 */
public class Activity_boshu_Setting extends Activity implements View.OnClickListener{
    private Button retreat;
    private RelativeLayout rl_boshu_Change_Pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_settting);
        init();
    }
    public void init(){
        retreat= (Button) this.findViewById(R.id.retreat);
        rl_boshu_Change_Pass= (RelativeLayout) this.findViewById(R.id.rl_boshu_changePass);
        retreat.setOnClickListener(this);
        rl_boshu_Change_Pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it=new Intent();
        switch (v.getId()){
            case R.id.retreat:
                layout();
                break;
            case R.id.rl_boshu_changePass:
                it.setClass(this,Activity_boshu_ChangePass.class);
                this.startActivity(it);
                break;
        }
    }
    public void back(View view){
        finish();
    }
    public void layout(){
        final ProgressDialog pd = new ProgressDialog(this);
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        MyApplication.getInstance().logout(new EMCallBack() {

            @Override
            public void onSuccess() {
               Activity_boshu_Setting.this.runOnUiThread(new Runnable() {
                   public void run() {
                       pd.dismiss();
                       // 重新显示登陆页面
                      MainActivity.context.finish();
                       startActivity(new Intent(Activity_boshu_Setting.this, LoginActivity.class));

                   }
               });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {

            }
        });

    }
}
