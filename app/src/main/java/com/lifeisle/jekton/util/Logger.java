package com.lifeisle.jekton.util;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * @author Jekton
 * @version 0.01 7/16/2015
 */
public class Logger {
    private static boolean debug = true;

    public static void e(String tag, String msg) {
        if (debug)
            Log.e(tag, msg);
    }

    public static void e(String tag, VolleyError volleyError) {
        NetworkResponse response = volleyError.networkResponse;
        if (response != null)
            e(tag, " statusCode = " + response.statusCode + " headers = " + response.headers);
        else
            e(tag, "" + volleyError.getMessage());
    }

    public static void e(String tag, int id) {
        e(tag, StringUtils.getStringFromResource(id));
    }

    public static void w(String tag, String msg) {
        if (debug)
            Log.w(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (debug)
            Log.d(tag, msg);
    }



}
