package com.lifeisle.jekton.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.easemob.chatuidemo.activity.MainActivity;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.SessionRequest;
import com.lifeisle.jekton.util.validator.UserNameValidator;
import com.lifeisle.jekton.util.validator.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Jekton
 * @version 0.01 7/21/2015
 */
public class SignInActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    private RequestQueue requestQueue;

    private EditText etUserName;
    private EditText etPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etUserName = (EditText) findViewById(R.id.userName);
        etPassword = (EditText) findViewById(R.id.password);

        etUserName.setText(Preferences.getUserName());
        etPassword.setText(Preferences.getPassword());

        findViewById(R.id.sign_in).setOnClickListener(this);
        findViewById(R.id.sign_up).setOnClickListener(this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) requestQueue.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                Validator validator = new UserNameValidator(etUserName.getText().toString());
                if (!validator.validate()) {
                    Toaster.showShort(this, validator.getErrorMessage());
                    return;
                }
                progressDialog = new ProgressDialog(this);
                progressDialog.show();

                // save user name even login failed
                 Preferences.setUserName(etUserName.getText().toString());

                if (requestQueue == null) {
                    requestQueue = Volley.newRequestQueue(this);
                    requestQueue.start();
                }
                requestQueue.add(new SignInRequest(Request.Method.POST, StringUtils.getServerPath(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                                try {
                                    int status = response.getInt("status");
                                    switch (status) {
                                        case 0:     // login successfully
                                        case 1:
                                            Preferences.setPassword(etPassword.getText().toString());

                                            Class<? extends Activity> gotoClass;
                                            if (status == 0)
                                                gotoClass = MainActivity.class;
                                            else
                                                // TODO
                                                gotoClass = null;

                                            Intent intent = new Intent(SignInActivity.this, gotoClass);
                                            startActivity(intent);
                                            break;
                                        case 2:
                                            Toaster.showShort(SignInActivity.this,
                                                    R.string.error_password_invalid);
                                            break;
                                        case 3:
                                            Toaster.showShort(SignInActivity.this,
                                                    R.string.error_user_name_invalid);
                                            break;
                                        default:
                                            Toaster.showShort(SignInActivity.this,
                                                    R.string.error_unknown);
                                            break;
                                    }
                                    
                                } catch (JSONException e) {
                                    Logger.e(TAG, e.toString());
                                }
                                
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Logger.e(TAG, error);
                                Toaster.showShort(SignInActivity.this, R.string.error_fail_to_login);
                            }
                        }));
                break;
            case R.id.sign_up:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

//请求登录的 
    private class SignInRequest extends SessionRequest {

        public SignInRequest(int method, String url, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        protected void setParams(Map<String, String> params) {
            params.put("user_name", etUserName.getText().toString());
            params.put("password", etPassword.getText().toString());
            params.put("sys", "user");
            params.put("ctrl", "user");
            params.put("action", "login");
        }
    }
}
