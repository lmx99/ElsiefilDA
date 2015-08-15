package com.boshu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.utils.Model;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by amou on 14/8/2015.
 */
public class Activity_boshu_ChangePass extends Activity{
    private Button bt_boshu_passCommit;
    private EditText et_boshu_oldpass;
    private EditText et_boshu_newpass;
    private EditText et_boshu_new_PassAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_changepass);
        init();;
    }
    public void setToast(String cue){
        Toast.makeText(Activity_boshu_ChangePass.this,cue,Toast.LENGTH_SHORT);
    }
    public void init(){
        bt_boshu_passCommit= (Button) this.findViewById(R.id.bt_boshu_passCommit);
        et_boshu_oldpass= (EditText) this.findViewById(R.id.et_boshu_oldpass);
        et_boshu_newpass= (EditText) this.findViewById(R.id.et_boshu_newPass);
        et_boshu_new_PassAgain= (EditText) this.findViewById(R.id.et_boshu_newPassAgain);
        bt_boshu_passCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassString=et_boshu_oldpass.getText().toString();
                String newPassString=et_boshu_new_PassAgain.getText().toString();
                String againPassString=et_boshu_new_PassAgain.getText().toString();
                if(!newPassString.equals(againPassString)){
                    setToast("两次输入的密码不一致");
                    return;
                }
                postChangPass(oldPassString,newPassString,againPassString);

            }
        });
    }
    public void postChangPass(final String oldPassString, final String newPassString, final String againPassString) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.start();

        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String flagString= response.getString("status");

                    if(flagString.equals("0")){
                        setToast("密码修改成功！");
                        /*Activity_boshu_Wallet.this.finish();
                        Toast.makeText(Activity_boshu_Wallet.this, "提交成功", Toast.LENGTH_SHORT).show();*/
                    }
                    if(flagString.equals("2")){
                        setToast("密码错误，请重新输入！");
                    }
                    setToast("未知错误！");

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
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
                params.put("action", "mod_account");
                params.put("old_password", oldPassString);
                params.put("new_password", newPassString);
                params.put("re_password",againPassString);
            }
        });
    }
}
