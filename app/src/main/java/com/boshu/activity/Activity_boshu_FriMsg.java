package com.boshu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boshu.adapter.Adapter_Boshu_alterDialog;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.lifeisle.android.R;

/**
 * Created by amou on 15/8/2015.
 */
public class Activity_boshu_FriMsg extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private TextView tv_remarks;
    private ImageView img_sex;
    private ImageView img_head;
    private ImageView img_more;
    private TextView tv_nickName;
    private TextView tv_entryYear;
    private TextView tv_school;
    private Button bt_chat;
    private AlertDialog alertDialog;
    private Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_frimsg);
        init();
    }
    public void init(){
        it=this.getIntent();
        tv_remarks= (TextView) this.findViewById(R.id.tv_remarks);
        tv_nickName= (TextView) this.findViewById(R.id.tv_nickName);
        img_sex= (ImageView) this.findViewById(R.id.img_sex);
        tv_entryYear= (TextView) this.findViewById(R.id.tv_boshu_edityear);
        tv_school= (TextView) this.findViewById(R.id.tv_boshu_editschool);
        img_head= (ImageView) this.findViewById(R.id.circle_touxiang);
        img_more= (ImageView) this.findViewById(R.id.more);
        bt_chat= (Button) this.findViewById(R.id.bt_chat);
        img_more.setOnClickListener(this);
        bt_chat.setOnClickListener(this);
    }
    public void back(View v){
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                String[] ss=new String[]{"设置备注","删除好友"};
                DisplayMetrics dm1= new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm1);
                int width1 = (int) (dm1.widthPixels / 1.2);
                int height1 = (int) (dm1.heightPixels / 2.8);
                setAlertDialog(this, ss,width1, height1);
                break;
            case R.id.bt_chat:
                it.setClass(this, ChatActivity.class);
                this.startActivity(it);
                this.finish();
                break;
        }

    }
    public void setAlertDialog(Context context, String[] ss, int width,
                               int height) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = this.getLayoutInflater().inflate(
                R.layout.alterdialog_boshu_tx, null);
        ListView lv = (ListView) view.findViewById(R.id.lv);

        Adapter_Boshu_alterDialog adapter = new Adapter_Boshu_alterDialog(this,
                ss);
        // BaseData2Adapter adapter = new BaseData2Adapter(context, ss);//
        // this,salaryarr,R.id.salary
        lv.setAdapter(adapter);

        builder.setView(view);
        alertDialog = builder.create();
        lv.setOnItemClickListener(this);
        alertDialog.show();
        WindowManager.LayoutParams params = alertDialog.getWindow()
                .getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = width;//
        params.height = height;//
        alertDialog.getWindow().setAttributes(params);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    public void setNewFriend()
    {   img_more.setVisibility(View.GONE);
        tv_remarks.setVisibility(View.GONE);
        bt_chat.setText("加为好友");
    }}
