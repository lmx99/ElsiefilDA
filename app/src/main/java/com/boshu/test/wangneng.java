package com.boshu.test;

import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

public class wangneng {
    public void setPost(){
       /* RequestQueue requestQueue=  Volley.newRequestQueue(this);

        requestQueue.start();
     
        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST, StringUtils.getServerUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.println("反问失败" + error);

                    }
                }) {

            @Override
            protected void setParams(Map<String, String> params) {
               params.put("user_name", "jekton");
                params.put("password", "helloworld");
                params.put("sys", "user");
                params.put("ctrl", "user");
                params.put("action", "login");
            }
        });
*/
      }
     
}
