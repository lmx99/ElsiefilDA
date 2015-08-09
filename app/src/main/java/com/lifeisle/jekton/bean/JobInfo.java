package com.lifeisle.jekton.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 * @version 0.01 7/31/2015
 */
public class JobInfo {

    public int reId;
    public String url;
    public String imageUrl;
    public String title;
    public String briefIntro;



    public static JobInfo newInstance(JSONObject jsonObject) throws JSONException {
        JobInfo info = new JobInfo();

        info.reId = jsonObject.getInt("re_id");
        info.url = jsonObject.getString("url");
        info.imageUrl = jsonObject.getString("img");
        info.title = jsonObject.getString("title");
        info.briefIntro = jsonObject.getString("intro");

        return info;
    }
}
