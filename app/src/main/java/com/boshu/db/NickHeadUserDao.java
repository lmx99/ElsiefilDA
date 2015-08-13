package com.boshu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.boshu.domain.NickHeadUser;

/**
 * Created by amou on 10/8/2015.
 */
public class NickHeadUserDao {
    private  String  userName_sql="username";
    private String nickName_sql="nickname";
    private String headImage_sql="headimage";
    private String table_sql="nickHeadUser";
    DbOpenBoshuHelper helper;
    public NickHeadUserDao(Context context){
        helper= new DbOpenBoshuHelper(context);
    }
    public void addNHUser(NickHeadUser user){
        SQLiteDatabase db= helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(userName_sql, user.getUserName());
        values.put(nickName_sql, user.getNickName());
        values.put(headImage_sql, user.getHeadImage());
        long i= db.insert(table_sql, null, values);
        System.out.println(i+"===========");
        db.close();
    }
    public NickHeadUser find(String userName) {
        String sql = "select*from " + table_sql + " where " + userName_sql + "=?";
       NickHeadUser user=null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        if (cursor.moveToNext()) {
           user=new NickHeadUser();
            user.setNickName(cursor.getString(cursor.getColumnIndex(nickName_sql)));
            user.setHeadImage(cursor.getString(cursor.getColumnIndex(headImage_sql)));
            user.setUserName(userName);

        }
        cursor.close();
        db.close();
        return user;
    }
    public void update(NickHeadUser user){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(userName_sql, user.getUserName());
        values.put(nickName_sql,user.getNickName());
        values.put(headImage_sql,user.getHeadImage());
        db.update(table_sql, values, nickName_sql+"=?", new String[]{user.getUserName()});
        db.close();
    }
    public void delete(String userName){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(table_sql, nickName_sql+"=?", new String[]{userName});
        db.close();
    }
}
