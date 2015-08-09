package com.lifeisle.jekton.bean;

import android.database.Cursor;

import com.lifeisle.jekton.data.ScheduleContract;
import com.lifeisle.jekton.util.ScheduleDBUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class ScheduleEvent {

    public static final int MASK_MONDAY    = 0b0000_0001;
    public static final int MASK_TUESDAY   = 0b0000_0010;
    public static final int MASK_WEDNESDAY = 0b0000_0100;
    public static final int MASK_THURSDAY  = 0b0000_1000;
    public static final int MASK_FRIDAY    = 0b0001_0000;
    public static final int MASK_SATURDAY  = 0b0010_0000;
    public static final int MASK_SUNDAY    = 0b0100_0000;

    public static final int MASK_EVERYDAY  = 0b0111_1111;
    public static final int MASK_WEEKDAY   = 0b0001_1111;
    public static final int MASK_NEVER     = 0b0000_0000;

    public int id;
    public String title;
    public long startMillis;
    public long endMillis;
    public int repeat;
    public int notify;
    public int type;


    @Override
    public String toString() {
        return "ScheduleEvent{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startMillis=" + startMillis +
                ", endMillis=" + endMillis +
                ", repeat=" + repeat +
                ", notify=" + notify +
                ", type=" + type +
                '}';
    }

    public static ScheduleEvent newInstance(JSONObject jsonObject) throws JSONException {
        ScheduleEvent event = new ScheduleEvent();
        event.title = jsonObject.getString(ScheduleContract.OWN_EVENT_TITLE);
        event.startMillis = jsonObject.getLong(ScheduleContract.OWN_EVENT_START_TIME);
        event.endMillis = jsonObject.getLong(ScheduleContract.OWN_EVENT_END_TIME);
        event.repeat = jsonObject.getInt(ScheduleContract.OWN_EVENT_REPEAT);
        event.notify = jsonObject.getInt(ScheduleContract.OWN_EVENT_NOTIFY);
        event.type = jsonObject.getInt(ScheduleContract.OWN_EVENT_TYPE);

        return event;
    }


    public static ScheduleEvent newInstance(Cursor cursor) {
        ScheduleEvent event = new ScheduleEvent();
        event.id = cursor.getInt(ScheduleDBUtils.COL_EVENT_ID);
        event.title = cursor.getString(ScheduleDBUtils.COL_EVENT_TITLE);
        event.startMillis = cursor.getLong(ScheduleDBUtils.COL_EVENT_START_TIME);
        event.endMillis = cursor.getLong(ScheduleDBUtils.COL_EVENT_END_TIME);
        event.repeat = cursor.getInt(ScheduleDBUtils.COL_EVENT_REPEAT);
        event.notify = cursor.getInt(ScheduleDBUtils.COL_EVENT_NOTIFY);
        event.type = cursor.getInt(ScheduleDBUtils.COL_EVENT_TYPE);

        return event;
    }
}