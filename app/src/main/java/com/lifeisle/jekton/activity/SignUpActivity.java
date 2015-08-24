package com.lifeisle.jekton.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.SessionRequest;
import com.lifeisle.jekton.util.validator.PasswordValidator;
import com.lifeisle.jekton.util.validator.PhoneNumberValidator;
import com.lifeisle.jekton.util.validator.UserNameValidator;
import com.lifeisle.jekton.util.validator.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @author Jekton
 * @version 0.01 7/20/2015
 */
public class SignUpActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private static final int MILLI_COUNT_DOWN = 2 * 60 * 1000;
    private static final int MILLI_COUNT_DOWN_STEP = 1000;

    private RequestQueue requestQueue;

    private EditText etUserName;
    private EditText etPassword;
    private EditText etReenterPassword;
    private EditText etPhone;
    private EditText etVerificationCode;
    private TextView btnGetVerifyCode;
    private Button btnSignUp;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etUserName = (EditText) findViewById(R.id.userName);
        etPassword = (EditText) findViewById(R.id.password);
        etReenterPassword = (EditText) findViewById(R.id.reenterPassword);
        etPhone = (EditText) findViewById(R.id.phone);
        etVerificationCode = (EditText) findViewById(R.id.verificationCode);
        btnGetVerifyCode = (TextView) findViewById(R.id.verify);
        btnSignUp = (Button) findViewById(R.id.sign_up);


        etUserName.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
        etReenterPassword.addTextChangedListener(this);
        etPhone.addTextChangedListener(this);
        etVerificationCode.addTextChangedListener(this);

        btnGetVerifyCode.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.start();
    }


    @Override
    protected void onDestroy() {
        requestQueue.stop();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null)
            progressDialog.dismiss();
        super.onBackPressed();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // do nothing
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (allInfoFilled()) {
            if (!btnSignUp.isEnabled()) {
                btnSignUp.setEnabled(true);
            }
        } else {
            if (btnSignUp.isEnabled()) {
                btnSignUp.setEnabled(false);
            }
        }
    }

    private boolean allInfoFilled() {
        return etUserName.length() != 0
                && etPassword.length() != 0
                && etReenterPassword.length() != 0
                && etPhone.length() != 0
                && etVerificationCode.length() != 0;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.verify:
                PhoneNumberValidator validator = new PhoneNumberValidator(etPhone.getText().toString());
                if (!validate(validator)) break;
                new VerifyCountDownTime(SignUpActivity.this, MILLI_COUNT_DOWN, MILLI_COUNT_DOWN_STEP)
                        .start();
                v.setEnabled(false);
                requestQueue.add(new VerifyCodeRequestSimple(Request.Method.POST, StringUtils.getServerPath(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {

                                // TODO parse the responded json
                                Logger.d(TAG, "get verify code, onResponse()");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Logger.e(TAG, volleyError);
                                Toast.makeText(SignUpActivity.this, R.string.error_network_fail,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }));
                break;

            case R.id.sign_up:

                UserNameValidator userNameValidator = new UserNameValidator(etUserName.getText().toString());
                PasswordValidator passwordValidator = new PasswordValidator(
                        etPassword.getText().toString(), etReenterPassword.getText().toString());
                PhoneNumberValidator phoneNumberValidator = new PhoneNumberValidator(etPhone.getText().toString());

                if (!validate(userNameValidator, passwordValidator, phoneNumberValidator)) break;

                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(SignUpActivity.this, ProgressDialog.STYLE_SPINNER);
                }
                progressDialog.show();

                requestQueue.add(new SignUpRequestSimple(Request.Method.POST, StringUtils.getServerPath(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                                try {
                                    int status = response.getInt("status");
                                    switch (status) {
                                        case 0:     // sign up successfully
                                            Intent intent = new Intent(SignUpActivity.this,
                                                    LoginActivity.class);
                                            startActivity(intent);
                                            break;
                                        case 1:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_user_name_empty);
                                            break;
                                        case 2:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_pwd_length);
                                            break;
                                        case 3:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_phone_changed);
                                            break;
                                        case 4:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_pwd_unmatched);
                                            break;
                                        case 5:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_verify_code_invalid);
                                            break;
                                        case 6:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_phone_registered);
                                            break;
                                        case 7:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_user_name_registered);
                                            break;
                                        case 8:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_server_db_insert_fail);
                                            break;
                                        case 9:
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_user_info_init_fail);
                                            break;
                                        default:     // unknown case
                                            Toaster.showShort(SignUpActivity.this,
                                                    R.string.error_unknown);
                                            break;
                                    }
                                } catch (JSONException e) {
                                    Logger.d(TAG, e.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.dismiss();
                                Logger.d(TAG, error.getMessage());
                                Toast.makeText(SignUpActivity.this, R.string.error_network_fail,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }));

                break;
        }
    }

    private boolean validate(Validator... validators) {
        for (Validator validator : validators) {
            if (!validator.validate()) {
                Toast.makeText(this, validator.getErrorMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    private static class VerifyCountDownTime extends CountDownTimer {

        private static final String normal =
                StringUtils.getStringFromResource(R.string.btn_sign_up_verify);
        private static final String wait =
                StringUtils.getStringFromResource(R.string.btn_sign_up_verify_wait);


        private WeakReference<SignUpActivity> weakReference;

        public VerifyCountDownTime(SignUpActivity activity, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            weakReference = new WeakReference<>(activity);
            Logger.d("VerifyCountDownTime", "normal = " + normal + "\nwait = " + wait);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Logger.d("VerifyCountDownTime", "onTick()");
            SignUpActivity activity;
            if ((activity = weakReference.get()) != null) {
                activity.btnGetVerifyCode.setText(String.format(wait, millisUntilFinished / 1000));
                Logger.d("VerifyCountDownTime", "onTick() text = "
                        + String.format(wait, millisUntilFinished / 1000));
            } else {
                cancel();
            }
        }

        @Override
        public void onFinish() {
            SignUpActivity activity;
            if ((activity = weakReference.get()) != null) {
                activity.btnGetVerifyCode.setEnabled(true);
                activity.btnGetVerifyCode.setText(normal);
            }
        }


    }


    private class VerifyCodeRequestSimple extends SessionRequest {

        public VerifyCodeRequestSimple(int method, String url, Response.Listener<JSONObject> listener,
                                       Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }



        @Override
        protected void setParams(Map<String, String> params) {

            params.put("mobile_phone", etPhone.getText().toString());
            params.put("sys", "user");
            params.put("ctrl", "user");
            params.put("action", "sendPIN");
        }
    }




    private class SignUpRequestSimple extends SessionRequest {

        public SignUpRequestSimple(int method, String url, Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }



        @Override
        protected void setParams(Map<String, String> params) {
            params.put("user_name", etUserName.getText().toString());
            params.put("password", etPassword.getText().toString());
            params.put("repassword", etReenterPassword.getText().toString());
            params.put("verified_phone", etPhone.getText().toString());
            params.put("sub_PIN", etVerificationCode.getText().toString());
            params.put("sys", "user");
            params.put("ctrl", "user");
            params.put("action", "reg");
        }
    }


}
