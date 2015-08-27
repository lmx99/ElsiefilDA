package com.lifeisle.jekton.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.boshu.utils.Model;
import com.easemob.chatuidemo.MyApplication;


/**
 * see <a herf="http://blog.codeint.com/saving-user-session-after-login-using-webservices-in-android-app/">
 *     http://blog.codeint.com/saving-user-session-after-login-using-webservices-in-android-app/</a>
 * to get more information about setting (session) cookie.<br />
 *
 * @author Jekton
 * @version 0.01 7/22/2015
 */
public class Preferences {

    private static final String TAG = "Preferences";
    private static final String PREF_USER_INFO = "user_info";
    private static final String PREF_WORK_INFO = "work_info";

    private Preferences() {
        throw new AssertionError("Can not instantiate this class.");
    }


    public static SharedPreferences getInstance(String name) {
        return MyApplication.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE);
    }


    public static void saveCookie(String cookie) {
        if (cookie == null) {
            //the server did not return a cookie so we wont have anything to save
            return;
        }

        SharedPreferences prefs = getInstance(PREF_USER_INFO);
        if (null == prefs) {
            return;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cookie", cookie);
        editor.apply();
    }

    public static String getCookie() {
        SharedPreferences prefs = getInstance(PREF_USER_INFO);
        String cookie = prefs.getString("cookie", "");
        if (cookie.contains("expires")) {
            /** you might need to make sure that your cookie returns expires when its expired.
             *  I also noted that cokephp returns deleted */
            removeCookie();
            return "";
        }
        return cookie;
    }

    private static void removeCookie() {
        SharedPreferences prefs = getInstance(PREF_USER_INFO);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("cookie");
        editor.apply();
    }





    public static @NonNull String getUserName() {
        return getInstance(PREF_USER_INFO).getString("userName", "");
    }

    public static void setUserName(String userName) {
        SharedPreferences preferences = getInstance(PREF_USER_INFO);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", userName);
        editor.apply();
    }



    public static String getPassword() {
        return getInstance(PREF_USER_INFO).getString("password", "");
    }
    public static void setPassword(String password) {
        SharedPreferences preferences = getInstance(PREF_USER_INFO);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public static int getJCatID() {
        SharedPreferences preferences = getInstance(PREF_WORK_INFO);
        return preferences.getInt("jcat_id" + getUserName(), -1);
    }

    public static void setJCatID(int id) {
        SharedPreferences.Editor editor = getInstance(PREF_WORK_INFO).edit();
        editor.putInt("jcat_id" + getUserName(), id);
        editor.apply();
    }

    public static long getSignInMillis() {
        SharedPreferences preferences = getInstance(PREF_WORK_INFO);
        return preferences.getLong("sign_in_millis" + getUserName(), -1);
    }

    public static void setSignInMillis(long millis) {
        SharedPreferences.Editor editor = getInstance(PREF_WORK_INFO).edit();
        editor.putLong("sign_in_millis" + getUserName(), millis);
        editor.apply();
    }


    public static int getStatType() {
        SharedPreferences preferences = getInstance(PREF_WORK_INFO);
        return preferences.getInt("stat_type", -1);
    }

    public static void setStatType(int id) {
        SharedPreferences.Editor editor = getInstance(PREF_WORK_INFO).edit();
        editor.putInt("stat_type", id);
        editor.apply();
    }
    /*boshu*/
    public static void setIndent(String flag){
        SharedPreferences preferences = getInstance(PREF_USER_INFO);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Model.APPLACATION, flag);
        editor.apply();
    }
    public static String getIndent(){
        SharedPreferences preferences = getInstance(PREF_USER_INFO);
        return preferences.getString(Model.APPLACATION, null);
    };
}
