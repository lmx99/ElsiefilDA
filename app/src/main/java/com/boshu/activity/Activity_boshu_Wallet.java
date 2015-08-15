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
 * Created by amou on 13/8/2015.
 */
public class Activity_boshu_Wallet extends Activity implements View.OnClickListener {
    private Button bt_boshu_ali;
    private EditText et_boshu_ali;
    private EditText et_boshu_repeatali;
    private EditText et_boshu_bank;
    private EditText et_boshu_repeatbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_wallet);
        bt_boshu_ali = (Button) this.findViewById(R.id.bt_boshu_commit);
        et_boshu_ali = (EditText) this.findViewById(R.id.et_boshu_ali);
        et_boshu_repeatali = (EditText) this.findViewById(R.id.et_boshu_repeatali);
        et_boshu_repeatbank = (EditText) this.findViewById(R.id.et_boshu_repeatbank);
        et_boshu_bank = (EditText) this.findViewById(R.id.et_boshu_bank);
        bt_boshu_ali.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_boshu_commit:
                String aliString=et_boshu_ali.getText().toString();
                String aliRepeatString=et_boshu_repeatali.getText().toString();
                String bankString=et_boshu_bank.getText().toString();
                String bankRepeatString=et_boshu_repeatbank.getText().toString();
                if(aliString.equals("")){
                    Toast.makeText(this,"输入支付宝不能为空！",Toast.LENGTH_SHORT).show();
                }
                if(!aliRepeatString.equals(aliString)){
                    Toast.makeText(this,"两次输入支付宝账号不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bankString.equals("")){
                    Toast.makeText(this,"输入银行不能为空！",Toast.LENGTH_SHORT).show();
                }
                if(!bankRepeatString.equals(bankString)){
                    Toast.makeText(this,"两次输入的银行账号不一样！",Toast.LENGTH_SHORT);
                    return;
                }
                postAccout(aliString,bankString);
                break;

        }
    }
    public void postAccout(final String aliString, final String bankString) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.start();

        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String flagString= response.getString("status");

                    if(flagString.equals("0")){
                        Activity_boshu_Wallet.this.finish();
                        Toast.makeText(Activity_boshu_Wallet.this,"提交成功",Toast.LENGTH_SHORT).show();
                    }

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
                params.put("action", "mod_fn");
                params.put("ali_account", aliString);
                params.put("bank_account", bankString);
            }
        });
    }
    public void back(View view){
        finish();
    }
}
