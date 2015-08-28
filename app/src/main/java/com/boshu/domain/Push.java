package com.boshu.domain;

/**
 * Created by amou on 10/8/2015.
 */
public class Push {

    private String type;
    private String picUrl;
    private String articlUrl;

    public String getPicUrl() {
        return picUrl;
    }

    public String getArticlUrl() {
        return articlUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setArticlUrl(String articlUrl) {
        this.articlUrl = articlUrl;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
