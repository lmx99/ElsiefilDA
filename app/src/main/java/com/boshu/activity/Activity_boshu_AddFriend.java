package com.boshu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boshu.domain.User;
import com.boshu.image.ImageDowloader;
import com.boshu.utils.Model;
import com.easemob.chat.EMContactManager;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;

/**
 * Created by amou on 8/9/2015.
 */
public class Activity_boshu_AddFriend extends Activity implements View.OnClickListener {
    private ImageView img;
    private TextView userName;
    private TextView nickName;
    private TextView hight;
    private TextView tv_ey;
    private TextView tv_school;
    private User user;
    private ImageDowloader dowloader;
    private Button bt_add;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialogCude;
    private InputMethodManager im;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_addfriend);
        user = (User) this.getIntent().getSerializableExtra("user");
        init();
        setData();
        Log.i("Activity_boshu_AddFriend", user.getHeadImage());
    }

    private void init() {
        img = (ImageView) this.findViewById(R.id.circle_touxiang);
        userName = (TextView) this.findViewById(R.id.tv_remarks);
        nickName = (TextView) this.findViewById(R.id.tv_nickName);
        hight = (TextView) this.findViewById(R.id.tv_hight);
        tv_ey = (TextView) this.findViewById(R.id.tv_boshu_edityear);
        tv_school = (TextView) this.findViewById(R.id.tv_boshu_editschool);
        bt_add = (Button) this.findViewById(R.id.bt_add);
        bt_add.setOnClickListener(this);


    }

    public void setData() {
        dowloader = new ImageDowloader(this);
        userName.setText(user.getUserName());
        nickName.setText(user.getNickName());
        hight.setText(user.getHight());
        tv_ey.setText(user.getEntrance_year());
        tv_school.setText(user.getSchool());
        String url = Model.PitureLoad + user.getHeadImage();
        Bitmap bitmap = dowloader.showCacheBitmap(url.replaceAll(
                "[^\\w]", ""));
        if (bitmap == null) {

        } else {
            dowloader.downloadImage(480, 480, url, new ImageDowloader.OnImageDownloadListener() {
                @Override
                public void onImageDownload(String url, Bitmap bitmap) {
                    if (url != null && bitmap != null) {
                        img.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }

    public void back(View view) {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                if(judeFriend(user.getUserName()))
                setView(user.getUserName(), "请输入认证信息");
                break;
        }

    }
    public boolean judeFriend(String userName){
        if (MyApplication.getInstance().getUserName().equals(userName)) {
            String str = getString(R.string.not_add_myself);
            Toast.makeText(this,str,0).show();
            //  startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
            return false;
        }

        if (MyApplication.getInstance().getContactList().containsKey(userName)) {
            //提示已在好友列表中，无需添加
            if (EMContactManager.getInstance().getBlackListUsernames().contains(userName)) {
                //startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
                return false;
            }
            String strin = getString(R.string.This_user_is_already_your_friend);
            Toast.makeText(this,strin,0).show();
            //   startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
            return false;
        }
        return true;

    }
    public void addFriend(final String userName, final String cudeString) {

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo写死了个reason，实际应该让用户手动填入

                    EMContactManager.getInstance().addContact(userName, cudeString);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    /* public void postChangPass(final String userName) {
         RequestQueue requestQueue = Volley.newRequestQueue(this);

         requestQueue.start();

         requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                 Model.PathLoad, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {


             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {

             }
         }) {
             @Override
             protected void setParams(Map<String, String> params) {
                 params.put("sys", "msg");
                 params.put("ctrl", "msger");
                 params.put("action", "add_friend");
                 params.put("friend_name", userName);
             }
         });
     }*/
    public void setView(final String userName, String cudeString) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) (dm.widthPixels / 1.1);
        int height = (int) (dm.heightPixels / 2.8);
        int paramsWith = 0;
        int paramsHeight = (int) (dm.widthPixels / 9);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(
                R.layout.alterdialog_select, null);
        TextView tv_Title = (TextView) view.findViewById(R.id.show_realname);
        tv_Title.setText(cudeString);
        final EditText editText = (EditText) view.findViewById(R.id.et_entry);
        Button no = (Button) view.findViewById(R.id.bt_boshu_updateno);
        Button yes = (Button) view.findViewById(R.id.bt_boshu_updateyes);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCude.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend(userName, editText.getText().toString());
                alertDialogCude.dismiss();

            }
        });
        builder.setView(view);
        alertDialogCude = builder.create();
        alertDialogCude.setCancelable(false);
        alertDialogCude.show();
        WindowManager.LayoutParams params = alertDialogCude.getWindow()
                .getAttributes();
        params.x = paramsWith;
        params.y = paramsHeight;
        params.width = width;//
        params.height = height;//
        alertDialogCude.getWindow().setAttributes(params);
        editText.post(new Runnable() {
            @Override
            public void run() {
                im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                im.showSoftInput(editText, 0);
            }
        });
    }
}
