package com.lifeisle.jekton.util.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Poster;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Jekton
 * @version 0.01 7/22/2015
 */
public class AutoLoginRequest extends SessionRequest {

    private static final String TAG = "AutoLoginRequest";

    private Context context;             // used to goto LoginActivity

    private Executor executor;

    private volatile Response<JSONObject> originalResponse;
    public AutoLoginRequest(Context context, int method, String url,
                            Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.context = context;
    }
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        originalResponse = super.parseNetworkResponse(response);
        JSONObject jsonObject = originalResponse.result;
        try {
            final int status = jsonObject.getInt("status");
            Logger.d(TAG, "status = " + status);
            Logger.d(TAG, "original response: " + jsonObject);
            if (status == -1) {     // has posted an invalid session ID, re-login
                executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Poster loginPoster = new LoginPoster(getUrl()) ;
                            loginPoster.addParam("user_name", Preferences.getUserName());
                            loginPoster.addParam("password", Preferences.getPassword());
                            loginPoster.addParam("sys", "user");
                            loginPoster.addParam("ctrl", "user");
                            loginPoster.addParam("action", "login");

                            loginPoster.post();
                        } catch (UnsupportedEncodingException e) {
                            loginFail();
                        }

                    }
                });

                // wait for login
                Logger.d(TAG, "wait for re-login");
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Logger.e(TAG, e.toString());
                    }
                }
            }
        } catch (JSONException e) {
            Logger.e(TAG, e.toString());
        }


        return originalResponse;
    }

    private void loginFail() {
        Logger.d(TAG, "loginFail()");
        if (context instanceof Activity) {
            Logger.d(TAG, "loginFail() is instanceof Activity");
            Activity activity = (Activity) context;
//            Toaster.showShort(activity, R.string.error_auto_login_fail);
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
        waitUpOriginRequest();
    }

    private void waitUpOriginRequest() {
        synchronized (this) {
            notify();
        }
    }



    private class LoginPoster extends Poster {

        public LoginPoster(String url) {
            super(url);
        }

        @Override
        protected void onSuccess(HttpURLConnection connection) {
            String cookie = connection.getHeaderField("Set-Cookie");
            Logger.d(TAG, "re-login cookie = " + cookie);
            Preferences.saveCookie(cookie);
            InputStream inputStream = null;
            try {
                inputStream = connection.getInputStream();
                String jsonString = StringUtils.readFromInputStream(inputStream);
                Logger.d(TAG, "login response: " + jsonString);
                JSONObject json = new JSONObject(jsonString);
                int status = json.getInt("status");
                Logger.d(TAG, "LoginPoster loginStatus = " + status);
                Logger.d(TAG, "LoginPoster response = " + json);
                if (status == 0) {
//                if (true) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // Re-request
                            Poster poster = new ReRequestPoster(getUrl());
                            try {
//                                Logger.d(TAG, "original params: " + getParams());
                                for (Map.Entry<String, String> entry : getParams().entrySet()) {
                                    Logger.d(TAG, "re-request params: " + entry.getKey() + "->" + entry.getValue());
                                    poster.addParam(entry.getKey(), entry.getValue());
                                }
                                poster.post();
                            } catch (AuthFailureError | UnsupportedEncodingException e) {
                                Logger.e(TAG, e.toString());
                                loginFail();
                            }
                        }
                    });
                } else {
                    loginFail();
                }
            } catch (IOException | JSONException e) {
                Logger.e(TAG, e.toString());
                loginFail();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    Logger.e(TAG, e.toString());
                }
            }
        }

    }




    private class ReRequestPoster extends Poster {
        public ReRequestPoster(String url) {
            super(url);
        }


        @Override
        protected void setRequestProperties(HttpURLConnection connection) {
            Logger.d(TAG, "re-request cookie: " + Preferences.getCookie());
            connection.setRequestProperty("Cookie", Preferences.getCookie());

        }


        @Override
        protected void onSuccess(HttpURLConnection connection) {
            try {
                InputStream inputStream = connection.getInputStream();
                String responseString = StringUtils.readFromInputStream(inputStream);
                Logger.d(TAG, "re-request response: " + responseString);
                JSONObject response = new JSONObject(responseString);
                originalResponse = Response.success(response, getCacheEntry());
                waitUpOriginRequest();
            }catch (JSONException | IOException e) {
                Logger.e(TAG, e.toString());
                loginFail();
            }
        }
    }
}





