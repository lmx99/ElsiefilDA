/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.activity.Activity_boshu_EditBaseMessage;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.MyApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.SignUpActivity;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.SessionRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity {
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()&&Preferences.getCookie()!=null) {
			autoLogin = true;
		//	LoginActivity.this.usernameEditText;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);

		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (MyApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(MyApplication.getInstance().getUserName());
		}
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login(View view) {
	     pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

	    setWeLoginPost();
		
	}

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);
		
		// 添加"Robot"
		User robotUser = new User();
		String strRobot = getResources().getString(R.string.robot_chat);
		robotUser.setUsername(Constant.CHAT_ROBOT);
		robotUser.setNick(strRobot);
		robotUser.setHeader("");
		userlist.put(Constant.CHAT_ROBOT, robotUser);
		
		// 存入内存
		MyApplication.getInstance().setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
	
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
	    
		startActivityForResult(new Intent(this,SignUpActivity.class), 0);
	}
   
	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}
	//请求环信后台服务器
	public void setHuanxinLoginPost(final int flag){
	    
	    if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = usernameEditText.getText().toString().trim();
        currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
       
        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
               
                if (!progressShow) {
                    return;
                }
                //基于吉同要求设置用户名和密码
                Preferences.setUserName(currentUsername);
                Preferences.setPassword(currentPassword);
                // 登陆成功，保存用户名密码
                MyApplication.getInstance().setUserName(currentUsername);
                MyApplication.getInstance().setPassword(currentPassword);

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
                    initializeContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            MyApplication.getInstance().logout(null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        MyApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // 进入主页面
               if(flag==1){
                   Intent intent = new Intent(LoginActivity.this,
                           Activity_boshu_EditBaseMessage.class);
                   intent.putExtra("FLAG", flag);
                   startActivity(intent);
               }else{
                   Intent intent = new Intent(LoginActivity.this,
                           MainActivity.class);
                   startActivity(intent);
               }
              
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
	}
	//请求我们的服务器
	public void setWeLoginPost(){
	    RequestQueue requestQueue = null;
	    if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.start();
        }
	    requestQueue.add(new SignInRequest(Request.Method.POST, StringUtils.getServerPath(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       
                      
                        try {
                            int status = response.getInt("status");
                            switch (status) {
                                case 1:     // login successfully  Preferences.setPassword(LoginActivity.this.passwordEditText.toString());
                                   
                                    LoginActivity.this.setHuanxinLoginPost(1);
                                    break;
                                case 0:
                                    Preferences.setPassword(LoginActivity.this.passwordEditText.toString());
                                   
                                    LoginActivity.this.setHuanxinLoginPost(2);
                                    break;
                                case 2:
                                    Toaster.showShort(LoginActivity.this,
                                            R.string.error_password_invalid);
                                    pd.dismiss();
                                    break;
                                case 3:
                                    Toaster.showShort(LoginActivity.this,
                                            R.string.error_user_name_invalid);
                                    pd.dismiss();
                                    break;
                                default:
                                    Toaster.showShort(LoginActivity.this,
                                            R.string.error_unknown);
                                    pd.dismiss();
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
                        pd.dismiss();
                        Logger.e(TAG, error);
                        Toaster.showShort(LoginActivity.this, R.string.error_fail_to_login);
                    }
                }));
	   
	}
	 private class SignInRequest extends SessionRequest {

	        public SignInRequest(int method, String url, Response.Listener<JSONObject> listener,
	                             Response.ErrorListener errorListener) {
	            super(method, url, listener, errorListener);
	        }

	        @Override
	        protected void setParams(Map<String, String> params) {
	            params.put("user_name", usernameEditText.getText().toString());
	            params.put("password", passwordEditText.getText().toString());
	            params.put("sys", "user");
	            params.put("ctrl", "user");
	            params.put("action", "login");
	        }
	    }
}
