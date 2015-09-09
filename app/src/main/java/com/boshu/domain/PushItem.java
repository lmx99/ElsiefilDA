package com.boshu.domain;

/**
 * Created by amou on 6/9/2015.
 */
public class PushItem {
    private int S_item_id;
    private int L_item_id;
    private String userName;
    private String topic;
    private String img_url;
    private String url;
    private String type;
    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getS_item_id() {
        return S_item_id;
    }

    public void setS_item_id(int s_item_id) {
        S_item_id = s_item_id;
    }

    public int getL_item_id() {
        return L_item_id;
    }

    public void setL_item_id(int l_item_id) {
        L_item_id = l_item_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
