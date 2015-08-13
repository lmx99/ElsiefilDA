package com.boshu.domain;

/**
 * Created by amou on 10/8/2015.
 */
public class NickHeadUser {
    private String nickName;
    private  String userName;
    private  String headImage;

    public String getUserName() {
        return userName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void SetString(String userName,String nickName,String headImage){
        this.userName=userName;
        this.nickName=nickName;
        this.headImage=headImage;
    }
}
