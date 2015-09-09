/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.boshu.activity.Activity_boshu_AddFriend;
import com.boshu.adapter.Adapter_Boshu_AddContacht;
import com.boshu.domain.User;
import com.boshu.utils.Model;
import com.lifeisle.android.R;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class AddContactActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private EditText editText;
    //	private LinearLayout searchedUserLayout;
    private TextView nameText, mTextView;
    /*private Button searchBtn;
    private ImageView avatar;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;
    private ProgressDialog progressDialog;*/
    private String AddContactActivity = "AddContactActivity";
    private ListView listView;
    private List<com.boshu.domain.User> list;
    private Adapter_Boshu_AddContacht adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        mTextView = (TextView) findViewById(R.id.add_list_friends);
        editText = (EditText) findViewById(R.id.edit_note);
        listView = (ListView) findViewById(R.id.lv_boshu_find);
        listView.setOnItemClickListener(this);
        list = new ArrayList<com.boshu.domain.User>();
        adapter = new Adapter_Boshu_AddContacht(AddContactActivity.this, list, listView);
        listView.setAdapter(adapter);
    /*	String strAdd = getResources().getString(R.string.add_friend);
		mTextView.setText(strAdd);
		String strUserName = getResources().getString(R.string.user_name);
		editText.setHint(strUserName);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);*/
    }

    public void searchContactList() {

    }

    /**
     * 查找contact
     * @param v
     */
    public void searchContact(View v) {
        final String name = editText.getText().toString();
        searchContactPost(name);
		/*String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if(TextUtils.isEmpty(name)) {
				String st = getResources().getString(R.string.Please_enter_a_username);
				startActivity(new Intent(this, AlertDialog.class).putExtra("msg", st));
				return;
			}
			
			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			
			//服务器存在此用户，显示此用户和添加按钮
			searchedUserLayout.setVisibility(View.VISIBLE);
			nameText.setText(toAddUsername);
			
		} */
    }

    /**
     *  添加contact
     * @param view
     */
/*	public void addContact(View view){
		if(MyApplication.getInstance().getUserName().equals(nameText.getText().toString())){
			String str = getString(R.string.not_add_myself);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", str));
			return;
		}
		
		if(MyApplication.getInstance().getContactList().containsKey(nameText.getText().toString())){
		    //提示已在好友列表中，无需添加
		    if(EMContactManager.getInstance().getBlackListUsernames().contains(nameText.getText().toString())){
		        startActivity(new Intent(this, AlertDialog.class).putExtra("msg", "此用户已是你好友(被拉黑状态)，从黑名单列表中移出即可"));
		        return;
		    }
			String strin = getString(R.string.This_user_is_already_your_friend);
			startActivity(new Intent(this, AlertDialog.class).putExtra("msg", strin));
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo写死了个reason，实际应该让用户手动填入
					String s = getResources().getString(R.string.Add_a_friend);
					EMContactManager.getInstance().addContact(toAddUsername, s);
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
	}*/
    public void back(View v) {
        finish();
    }

    public void searchContactPost(final String searchText) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.start();

        requestQueue.add(new AutoLoginRequest(this, Request.Method.POST,
                Model.PathLoad, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    list.clear();
                    String status = response.getString("status");
                    if (status.equals("0")) {
                        JSONArray array = response.getJSONArray("users");
                        for (int i = 0; i < array.length(); i++) {
                            String nickName = array.getJSONObject(i).getString("nick_name");
                            String sex = array.getJSONObject(i).getString("sex");
                            String height = array.getJSONObject(i).getString("height");
                            String school = array.getJSONObject(i).getString("school");
                            String head_image = array.getJSONObject(i).getString("head_image");
                            String user_name = array.getJSONObject(i).getString("user_name");
                            String entrance_year= array.getJSONObject(i).getString("entrance_year");
                            com.boshu.domain.User user = new com.boshu.domain.User();
                            user.setNickName(nickName);
                            user.setUserName(user_name);
                            user.setSex(sex);
                            user.setHight(height);
                            user.setSchool(school);
                            user.setHeadImage(head_image);
                            user.setEntrance_year(entrance_year);
                            list.add(user);
                            Log.i(AddContactActivity, "查找到的信息：" + nickName);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    if (status.equals("1")) {
                        Toast.makeText(AddContactActivity.this, "查找不到相关好友", 0).show();
                        adapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.i(AddContactActivity, "解析出错了，请检查后台接口，错误信息：" + e.toString());

                }

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
                params.put("action", "find_friends");
                params.put("search_text", searchText);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user=list.get(position);
         Intent it=new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("user", user);
        it.putExtras(bundle);
        it.setClass(this, Activity_boshu_AddFriend.class);
        startActivity(it);
    }
}
