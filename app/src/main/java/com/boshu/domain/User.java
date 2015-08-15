package com.boshu.domain;

public class User {
    private String id;
    private String userName;
    private String nickName;
    private String sex;
    private String age;
    private String hight;
    private String figure;
    private String school;
    private String major;
    private String entrance_year;
    private String realName;
    private String myPhone;
    private String friendPhone;
    private String mail;
    private String qQ;
    private String jobWant;
    private String resume;// 简历
    private String idCard;
    private String BeforeIdCard;
    private String AferIdCard;
    private String headImage;
    private String studentImage;
    

    public String getStudentImage() {
        return studentImage;
    }

    public void setStudentImage(String studentImage) {
        this.studentImage = studentImage;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEntrance_year() {
        return entrance_year;
    }

    public void setEntrance_year(String entrance_year) {
        this.entrance_year = entrance_year;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMyPhone() {
        return myPhone;
    }

    public void setMyPhone(String myPhone) {
        this.myPhone = myPhone;
    }

    public String getFriendPhone() {
        return friendPhone;
    }

    public void setFriendPhone(String friendPhone) {
        this.friendPhone = friendPhone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getqQ() {
        return qQ;
    }

    public void setqQ(String qQ) {
        this.qQ = qQ;
    }

    public String getJobWant() {
        return jobWant;
    }

    public void setJobWant(String jobWant) {
        this.jobWant = jobWant;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBeforeIdCard() {
        return BeforeIdCard;
    }

    public void setBeforeIdCard(String beforeIdCard) {
        BeforeIdCard = beforeIdCard;
    }

    public String getAferIdCard() {
        return AferIdCard;
    }

    public void setAferIdCard(String aferIdCard) {
        AferIdCard = aferIdCard;
    }

    public void setUser(String userName,String nickName, String sex, String age,
            String hight, String figure, String school, String major,
            String entrance_year, String realName, String myPhone,
            String friendPhone, String mail, String qQ, String jobWant,
            String resume, String idCard, String BeforeIdCard,
            String afterIdCard,String headImage,String studentIdCard) {
        this.userName = userName;
        this.nickName = nickName;
        this.sex = sex;
        this.age = age;
        this.hight = hight;
        this.figure = figure;
        this.school = school;
        this.major = major;
        this.entrance_year = entrance_year;
        this.realName=realName;
        this.myPhone=myPhone;
        this.friendPhone=friendPhone;
        this.mail=mail;
        this.qQ=qQ;
        this.jobWant=jobWant;
        this.resume=resume;
        this.idCard=idCard;
        this.BeforeIdCard=BeforeIdCard;
        this.AferIdCard=afterIdCard;
        this.headImage=headImage;
        this.studentImage=studentIdCard;
        

    }

}
