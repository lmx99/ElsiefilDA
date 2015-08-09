package com.boshu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boshu.domain.User;

public class UserDao {
   DbOpenBoshuHelper helper;
    public UserDao(Context context){
       helper= new DbOpenBoshuHelper(context);
    }
    public void addUser(User user){
        try {
            
        
       SQLiteDatabase db= helper.getWritableDatabase();
      /* create table user(id integer primary 
       * key autoincrement,userName varchar(100),nickName varchar(100),
       * sex varchar(100),age varchar(100),hight varchar(100),figure varchar(100),
       * school varchar(100),major varchar(100),entrance_year varchar(20),realname varchar(20),
       * myPhone varchar(100),friendPhone varchar100),mail varchar(100),qQ varchar(100),
       * jobWant varchar(100),resume varchar(300),idCard varchar(100),
       * BeforeIdCard varchar(100),AferIdCard varchar(100),headImage varchar(100))";*/
       System.out.println("------------------------------username"+user.getUserName());
       ContentValues values=new ContentValues();
       values.put("userName", user.getUserName());
       values.put("nickName", user.getNickName());
       values.put("sex", user.getSex());
       System.out.println(".........................dddd");
       values.put("age", user.getAge());
       values.put("hight", user.getHight());
       values.put("figure", user.getFigure());
       values.put("school", user.getSchool());
       values.put("major", user.getMajor());
       values.put("entrance_year", user.getEntrance_year());
       System.out.println(".............................fffff");
       values.put("realname", user.getRealName());
       values.put("myPhone", user.getMyPhone());
       values.put("friendPhone", user.getFriendPhone());
       values.put("mail", user.getMail());
       values.put("qQ", user.getqQ());
       values.put("jobWant", user.getJobWant());
       System.out.println(".........................fffffff");
       values.put("resume", user.getResume());
       values.put("idCard", user.getIdCard());
       values.put("BeforeIdCard", user.getBeforeIdCard());
       values.put(" AferIdCard", user.getAferIdCard());
       values.put("headImage", user.getHeadImage());
       values.put("studentImage", user.getStudentImage());
      long i= db.insert("user", null, values);
      db.close();
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
        }
    }
    public User find(String userName){
        String sql="select*from user where userName=?";
        User user=null;
        SQLiteDatabase db=helper.getReadableDatabase();
       Cursor cursor= db.rawQuery(sql, new String[]{userName});
       if (cursor.moveToNext()) {
           String userName1 = cursor.getString(cursor
                   .getColumnIndex("userName"));
           String nickName = cursor.getString(cursor
                   .getColumnIndex("nickName"));
           String  sex= cursor.getString(cursor
                   .getColumnIndex("sex"));
           String  age= cursor.getString(cursor
                   .getColumnIndex("age"));
           String hight = cursor.getString(cursor
                   .getColumnIndex("hight"));
           String  figure= cursor.getString(cursor
                   .getColumnIndex("figure"));
           String  school= cursor.getString(cursor
                   .getColumnIndex("school"));
           String  major= cursor.getString(cursor
                   .getColumnIndex("major"));
           String  entrance_year= cursor.getString(cursor
                   .getColumnIndex("entrance_year"));
           String  realName= cursor.getString(cursor
                   .getColumnIndex("realname"));
           String  myPhone= cursor.getString(cursor
                   .getColumnIndex("myPhone"));
           String  friendPhone= cursor.getString(cursor
                   .getColumnIndex("friendPhone"));
           String  mail= cursor.getString(cursor
                   .getColumnIndex("mail"));
           String  qQ= cursor.getString(cursor
                   .getColumnIndex("qQ"));
           String jobWant = cursor.getString(cursor
                   .getColumnIndex("jobWant"));
           String  resume= cursor.getString(cursor
                   .getColumnIndex("resume"));
           String  idCard= cursor.getString(cursor
                   .getColumnIndex("idCard"));
           String  BeforeIdCard= cursor.getString(cursor
                   .getColumnIndex("BeforeIdCard"));
           String  AferIdCard= cursor.getString(cursor
                   .getColumnIndex("AferIdCard"));
           String headImage = cursor.getString(cursor
                   .getColumnIndex("headImage"));
           String student = cursor.getString(cursor
                   .getColumnIndex("studentImage"));
           user=new User();
          user.setAferIdCard(AferIdCard);
          user.setAge(age);
          user.setBeforeIdCard(BeforeIdCard);
          user.setEntrance_year(entrance_year);
          user.setFigure(figure);
          user.setFriendPhone(friendPhone);
          user.setHeadImage(headImage);
          user.setHight(hight);
          user.setIdCard(idCard);
          user.setJobWant(jobWant);
          user.setMail(mail);
          user.setMajor(major);
          user.setMyPhone(myPhone);
          user.setUserName(userName);    
          user.setSex(sex);
          user.setSchool(school);
          user.setResume(resume);
          user.setRealName(realName);
          user.setqQ(qQ);
          user.setNickName(nickName);

       }
       cursor.close();
       db.close();
        return user;
        
    }
    public void update(User user){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("userName", user.getUserName());
        values.put("nickName", user.getNickName());
        values.put("sex", user.getSex());
        values.put("age", user.getAge());
        values.put("hight", user.getHight());
        values.put("figure", user.getFigure());
        values.put("school", user.getSchool());
        values.put("major", user.getMajor());
        values.put("entrance_year", user.getEntrance_year());
        values.put("realname", user.getRealName());
        values.put("myPhone", user.getMyPhone());
        values.put("friendPhone", user.getFriendPhone());
        values.put("mail", user.getMail());
        values.put("qQ", user.getqQ());
        values.put("jobWant", user.getJobWant());
        values.put("resume", user.getResume());
        values.put("idCard", user.getIdCard());
        values.put("BeforeIdCard", user.getBeforeIdCard());
        values.put(" AferIdCard", user.getAferIdCard());
        values.put("headImage", user.getHeadImage());
        values.put("studentImage", user.getStudentImage());
        db.update("user", values, "userName=?", new String[]{user.getUserName()});
        db.close();
    }
    public void delete(String userName){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("user", "phone=?", new String[]{userName});
        db.close();
    }
  
    

}
