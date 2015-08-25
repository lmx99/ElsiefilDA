package com.lifeisle.jekton.util;


import com.easemob.chatuidemo.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Jekton Luo
 * @version 0.01 6/5/2015.
 */
public class StringUtils {
    private static final String SERVER_HOST = "http://shenghuodao.gotoip2.com/";
   // private static final String SERVER_HOST = "http://192.168.0.103/";

    private static final String SERVER_PATH = SERVER_HOST + "work/main.php";

    private static final String SERVER_BASE = SERVER_HOST + "work/";

    private StringUtils() {
        throw new AssertionError("Can not instantiate this class.");
    }


    public static String getServerPath() {
        return SERVER_PATH;
    }

    public static String getServerBase() {
        return SERVER_BASE;
    }

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        return readFromInputStream(inputStream, "UTF-8");
    }


    public static String readFromInputStream(InputStream inputStream, String encoding)
            throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }

        return builder.toString();
    }



    public static String getStringFromResource(int id) {
        return MyApplication.getInstance().getResources().getString(id);
    }


    public static String[] getStringsFromResource(int id) {
        return MyApplication.getInstance().getResources().getStringArray(id);
    }
}
