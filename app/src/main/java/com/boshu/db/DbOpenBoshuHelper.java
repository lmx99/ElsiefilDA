package com.boshu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenBoshuHelper extends SQLiteOpenHelper {

    public DbOpenBoshuHelper(Context context) {
        super(context, "liftisland24.db", null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //String sql = "create table TSMessage(id integer primary key autoincrement,title varchar(128),content text,time varchar(16),phone varchar(20),visitorphone varchar(20),ql_id varchar(20))";
        String user_sql = "create table user(id integer primary key autoincrement,userName varchar(100),nickName varchar(100),sex varchar(100),age varchar(100),hight varchar(100),figure varchar(100),school varchar(100),major varchar(100),entrance_year varchar(20),realname varchar(20),myPhone varchar(100),friendPhone varchar(100),mail varchar(100),qQ varchar(100),jobWant varchar(100),resume varchar(300),idCard varchar(100),BeforeIdCard varchar(100),AferIdCard varchar(100),headImage varchar(100),studentImage varchar(100))";
        db.execSQL(user_sql);
        try {
            db.execSQL(user_sql);
        } catch (Exception e) {
            // TODO: handle exception
        }
     
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
