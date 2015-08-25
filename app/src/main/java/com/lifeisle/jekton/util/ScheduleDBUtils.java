package com.lifeisle.jekton.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.schedule.data.ScheduleContract.EventEntry;
import com.lifeisle.jekton.schedule.data.ScheduleDBHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class ScheduleDBUtils {

    public static final String[] EVENT_COLUMNS = {
            EventEntry._ID,
            EventEntry.COL_EVENT_TITLE,
            EventEntry.COL_EVENT_START_TIME,
            EventEntry.COL_EVENT_END_TIME,
            EventEntry.COL_EVENT_REPEAT,
            EventEntry.COL_EVENT_NOTIFY,
            EventEntry.COL_EVENT_TYPE,
            EventEntry.COL_EVENT_NEED_POST,
    };

    /**
     * These indices are tied to {@link ScheduleDBUtils#EVENT_COLUMNS}.
     * If {@link ScheduleDBUtils#EVENT_COLUMNS} changes, these must change.
     */
    public static final int COL_EVENT_ID = 0;
    public static final int COL_EVENT_TITLE = 1;
    public static final int COL_EVENT_START_TIME = 2;
    public static final int COL_EVENT_END_TIME = 3;
    public static final int COL_EVENT_REPEAT = 4;
    public static final int COL_EVENT_NOTIFY = 5;
    public static final int COL_EVENT_TYPE = 6;
    public static final int COL_EVENT_NEED_POST = 7;


    private static final String LOG_TAG = "ScheduleDBUtils";

    private static SQLiteOpenHelper sqLiteOpenHelper =
            new ScheduleDBHelper(
                    MyApplication.getInstance(),
                    ScheduleDBHelper.DATABASE_NAME,
                    null,
                    ScheduleDBHelper.DATABASE_VERSION);


    private ScheduleDBUtils() {
        throw new AssertionError("You couldn't instantiate this class");
    }


    public static List<ScheduleEvent> getScheduleEvents(int year, int month) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();

        Calendar calendar = new GregorianCalendar(year, month, 1);
        long startTime = calendar.getTimeInMillis();
        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        long endTime = calendar.getTimeInMillis();

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(EventEntry.TABLE_NAME);
        builder.appendWhere(
                EventEntry.COL_EVENT_REPEAT + " != " + ScheduleEvent.REPEAT_NEVER + " or (" +
                EventEntry.COL_EVENT_START_TIME + " < " + endTime + " and " +
                startTime + " < " + EventEntry.COL_EVENT_END_TIME + ")"
                );
        Cursor cursor = builder.query(db, EVENT_COLUMNS, null, null, null, null,
                EventEntry.COL_EVENT_START_TIME + " asc ");
        List<ScheduleEvent> events = createEventsFromCursor(cursor);
        cursor.close();
        return events;
    }

    private static List<ScheduleEvent> createEventsFromCursor(Cursor cursor) {
        List<ScheduleEvent> events = new ArrayList<>();
        while (cursor.moveToNext()) {
            events.add(ScheduleEvent.newInstance(cursor));
        }

        return events;
    }


    public static ScheduleEvent getScheduleEvent(long id) {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.query(EventEntry.TABLE_NAME,
                EVENT_COLUMNS,
                EventEntry._ID + "=" + id,
                null,
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            return ScheduleEvent.newInstance(cursor);
        }
        return null;
    }

    public static long insertScheduleEvent(ScheduleEvent event) {
        ContentValues values = convertToValues(event);

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        long id = db.insert(EventEntry.TABLE_NAME, null, values);
        if (id < 0)
            Logger.e(LOG_TAG, "Fail to insert event to database, event = " + event);

        return id;
    }

    private static ContentValues convertToValues(ScheduleEvent event) {
        ContentValues values = new ContentValues();
        values.put(EventEntry.COL_EVENT_TITLE, event.title);
        values.put(EventEntry.COL_EVENT_START_TIME, event.startMillis);
        values.put(EventEntry.COL_EVENT_END_TIME, event.endMillis);
        values.put(EventEntry.COL_EVENT_REPEAT, event.repeat);
        values.put(EventEntry.COL_EVENT_NOTIFY, event.notify);
        values.put(EventEntry.COL_EVENT_TYPE, event.type);
        values.put(EventEntry.COL_EVENT_NEED_POST, event.needPost ? 1 : 0);

        return values;
    }


    public static int updateScheduleEvent(ScheduleEvent event) {
        deleteAlarm(event.id);

        ContentValues values = convertToValues(event);
        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int count = db.update(EventEntry.TABLE_NAME, values, EventEntry._ID + "=" + event.id, null);
        if (count <= 0)
            Logger.e(LOG_TAG, "Fail to update event to database, event = " + event);

        return count;
    }


    public static int deleteScheduleEvent(long id) {
        deleteAlarm(id);

        SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
        int count = db.delete(EventEntry.TABLE_NAME, EventEntry._ID + "=" + id, null);
        if (count <= 0) {
            Logger.e(LOG_TAG, "Fail to delete event to database, eventId = " + id);
        }

        return count;
    }

    private static void deleteAlarm(long id) {
        // TODO: 8/19/2015  delete old alarm
    }
}
