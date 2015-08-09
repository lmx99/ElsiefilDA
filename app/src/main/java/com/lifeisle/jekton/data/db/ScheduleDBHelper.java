package com.lifeisle.jekton.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lifeisle.jekton.data.ScheduleContract.EventEntry;
import com.lifeisle.jekton.util.Logger;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class ScheduleDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 0;
    public static final String DATABASE_NAME = "schedules.db";

    private static final String  LOG_TAG = "ScheduleDBHelper";

    
    private static final String CREATE_TABLE_EVENTS = "create table " +
            EventEntry.TABLE_NAME + " (" +
            EventEntry._ID + " integer primary key autoincrement," +
            EventEntry.COL_EVENT_TITLE + " text not null," +
            EventEntry.COL_EVENT_START_TIME + " integer not null," +
            EventEntry.COL_EVENT_END_TIME + " integer not null," +
            EventEntry.COL_EVENT_REPEAT + " integer not null," +
            EventEntry.COL_EVENT_NOTIFY + " integer not null," +
            EventEntry.COL_EVENT_TYPE + " integer not null" +
            ");";


    public ScheduleDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            int version) {
        super(context, name, factory, version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.w(LOG_TAG, "upgrade from version " + oldVersion + " to version " + newVersion
                + ", which will destroy all old data");

        db.execSQL("drop table if exists " + EventEntry.TABLE_NAME);
        
        onCreate(db);
    }
}
