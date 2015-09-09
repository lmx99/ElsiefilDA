package com.boshu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.boshu.domain.PushItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by amou on 6/9/2015.
 */
public class PushItemDao {
    private String L_item_id = "L_item_id";
    private  String S_item_id = "S_item_id";
    private  String userNane = "userName";
    private  String topic = "topic";
    private  String img_url = "img_url";
    private  String url = "url";
    private  String time = "time";
    private String type="type";
    private  String test = "test";
    DbOpenBoshuHelper helper;

    public PushItemDao(Context context) {
        helper = new DbOpenBoshuHelper(context);
    }

    public void addItemt(PushItem item) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(L_item_id, item.getL_item_id());
            values.put(S_item_id, item.getS_item_id());
            values.put(userNane, item.getUserName());
            values.put(topic, item.getTopic());
            values.put(img_url, item.getImg_url());
            values.put(url, item.getUrl());
            values.put(type,item.getType());
            values.put(time, new Date().getTime());
            long i = db.insert("pushitem", null, values);
            Log.i(test, i + "");
            db.close();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("推送数据库报错", e.toString());
        }
    }

    public List<PushItem> findAll(String userName) {
        String sql = "select*from pushitem where userName=?";
        PushItem item = null;
        List<PushItem> list=new ArrayList<PushItem>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        while (cursor.moveToNext()) {
            item=new PushItem();
            String userName1 = cursor.getString(cursor
                    .getColumnIndex("userName"));
            String topic = cursor.getString(cursor
                    .getColumnIndex("topic"));
            String img_url = cursor.getString(cursor
                    .getColumnIndex("img_url"));
            String url = cursor.getString(cursor
                    .getColumnIndex("url"));
            String type=cursor.getString(cursor.getColumnIndex("type"));
            Long time=cursor.getLong(cursor.getColumnIndex("time"));
            int L_item_id=cursor.getInt(cursor.getColumnIndex("L_item_id"));
            int S_itme_id=cursor.getInt(cursor.getColumnIndex("S_item_id"));
            item.setImg_url(img_url);
            item.setL_item_id(L_item_id);
            item.setS_item_id(S_itme_id);
            item.setTime(time);
            item.setTopic(topic);
            item.setType(type);
            item.setUserName(userName1);
            item.setUrl(url);
            list.add(item);
        }
        cursor.close();
        db.close();
        return list;
    }
  /*  public void delete(String userName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("user", "userName=?", new String[]{userName});
        db.close();
    }*/

}

