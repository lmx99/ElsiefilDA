package com.lifeisle.jekton.util.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import org.json.JSONObject;

import java.util.Map;

/**
 * @author Jekton
 * @version 0.01 7/22/2015
 */
public class LoginRequest extends SessionRequest {

    private String userName;
    private String password;

    public LoginRequest(String url, String userName, String password,
                        Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, listener, errorListener);

        this.userName = userName;
        this.password = password;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        return super.parseNetworkResponse(response);
    }


    @Override
    protected void setParams(Map<String, String> params) {
        params.put("user_name", userName);
        params.put("password", password);
        params.put("sys", "user");
        params.put("ctrl", "user");
        params.put("action", "login");
    }
}
