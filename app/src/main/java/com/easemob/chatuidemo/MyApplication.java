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
package com.easemob.chatuidemo;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.easemob.EMCallBack;
import com.easemob.chatuidemo.domain.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.Map;

public class MyApplication extends Application {

	public static Context applicationContext;
	private static MyApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";

	/**
	 * 当前用户nickname,为了苹果推送不是userid而是昵称
	 */
	public static String currentUserNick = "";
	public static DemoHXSDKHelper hxSDKHelper = new DemoHXSDKHelper();


	private static RequestQueue requestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;

		requestQueue = Volley.newRequestQueue(this);
		requestQueue.start();

		/**
		 * this function will initialize the HuanXin SDK
		 *
		 * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
		 *
		 * 环信初始化SDK帮助函数
		 * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
		 *
		 * for example:
		 * 例子：
		 *
		 * public class DemoHXSDKHelper extends HXSDKHelper
		 *
		 * HXHelper = new DemoHXSDKHelper();
		 * if(HXHelper.onInit(context)){
		 *     // do HuanXin related work
		 * }
		 */
		hxSDKHelper.onInit(applicationContext);

		initImageLoader(applicationContext);
	}

	public static MyApplication getInstance() {
		return instance;
	}

	/**
	 * 获取内存中好友user list
	 *
	 * @return
	 */
	public Map<String, User> getContactList() {
		return hxSDKHelper.getContactList();
	}

	/**
	 * 设置好友user list到内存中
	 *
	 * @param contactList
	 */
	public void setContactList(Map<String, User> contactList) {
		hxSDKHelper.setContactList(contactList);
	}

	/**
	 * 获取当前登陆用户名
	 *
	 * @return
	 */
	public String getUserName() {
		return hxSDKHelper.getHXId();
	}

	/**
	 * 获取密码
	 *
	 * @return
	 */
	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	/**
	 * 设置用户名
	 *
	 * @param username
	 */
	public void setUserName(String username) {
		hxSDKHelper.setHXId(username);
	}

	/**
	 * 设置密码 下面的实例代码 只是demo，实际的应用中需要加password 加密后存入 preference 环信sdk
	 * 内部的自动登录需要的密码，已经加密存储了
	 *
	 * @param pwd
	 */
	public void setPassword(String pwd) {
		hxSDKHelper.setPassword(pwd);
	}

	/**
	 * 退出登录,清空数据
	 */
	public void logout(final EMCallBack emCallBack) {
		// 先调用sdk logout，在清理app中自己的数据
		hxSDKHelper.logout(emCallBack);
	}






	// *****************************************************
	// Jekton
	// *****************************************************
	public static <T> void addToRequestQueue(Request<T> request) {
		requestQueue.add(request);
	}

	public static void cancelRequest(Object tag) {
		requestQueue.cancelAll(tag);
	}


	private static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
}