package com.lifeisle.jekton.bean;

import android.database.Cursor;

import com.alamkanak.weekview.WeekViewEvent;
import com.lifeisle.jekton.schedule.data.ScheduleContract;
import com.lifeisle.jekton.util.ScheduleDBUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class ScheduleEvent implements Cloneable {

    public static final int MASK_MONDAY    = 0b0000_0001;
    public static final int MASK_TUESDAY   = 0b0000_0010;
    public static final int MASK_WEDNESDAY = 0b0000_0100;
    public static final int MASK_THURSDAY  = 0b0000_1000;
    public static final int MASK_FRIDAY    = 0b0001_0000;
    public static final int MASK_SATURDAY  = 0b0010_0000;
    public static final int MASK_SUNDAY    = 0b0100_0000;

    public static final int REPEAT_EVERYDAY = 0b0111_1111;
    public static final int REPEAT_WEEKDAY  = 0b0001_1111;
    public static final int REPEAT_NEVER    = 0b0000_0000;

    private static final String TAG = ScheduleEvent.class.getSimpleName();

    public int id;
    public String title;
    public long startMillis;
    public long endMillis;
    public int repeat;
    public int notify;
    public int type;
    public boolean needPost;


    public WeekViewEvent toWeekViewEvent() {
        Calendar startTime = new GregorianCalendar();
        startTime.setTimeInMillis(startMillis);
        Calendar endTime = new GregorianCalendar();
        endTime.setTimeInMillis(endMillis);
        return new WeekViewEvent(id, title, startTime, endTime);
    }

    @Override
    public String toString() {
        return "ScheduleEvent{" +
                "id=" + id + "\n" +
                ", title='" + title + '\'' + "\n" +
                ", startMillis=" + startMillis + "\n" +
                ", endMillis=" + endMillis + "\n" +
                ", repeat=" + repeat + "\n" +
                ", notify=" + notify + "\n" +
                ", type=" + type + "\n" +
                ", needPost=" + needPost + "\n" +
                '}';
    }


    public static ScheduleEvent newInstance(ScheduleEvent event) {
        ScheduleEvent newEvent = new ScheduleEvent();
        newEvent.id = event.id;
        newEvent.title = event.title;
        newEvent.startMillis = event.startMillis;
        newEvent.endMillis = event.endMillis;
        newEvent.repeat = event.repeat;
        newEvent.notify = event.notify;
        newEvent.type = event.type;
        newEvent.needPost = event.needPost;

        return newEvent;
    }


    public static ScheduleEvent newInstance(JSONObject jsonObject) throws JSONException {
        ScheduleEvent event = new ScheduleEvent();
        event.id = jsonObject.getInt(ScheduleContract.OWN_EVENT_LOCAL_ID);
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
        event.needPost = cursor.getInt(ScheduleDBUtils.COL_EVENT_NEED_POST) != 0;

        return event;
    }



    public List<ScheduleEvent> expandEvent(Calendar startTime) {
        final int DAYS_OF_WEEK = 7;
        ArrayList<ScheduleEvent> events = new ArrayList<>();

        for (int i = 0, days = startTime.getActualMaximum(Calendar.DAY_OF_MONTH); i < days; ++i) {
            int mask = -1;
            switch (startTime.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    mask = MASK_MONDAY;
                    break;
                case Calendar.TUESDAY:
                    mask = MASK_TUESDAY;
                    break;
                case Calendar.WEDNESDAY:
                    mask = MASK_WEDNESDAY;
                    break;
                case Calendar.THURSDAY:
                    mask = MASK_THURSDAY;
                    break;
                case Calendar.FRIDAY:
                    mask = MASK_FRIDAY;
                    break;
                case Calendar.SATURDAY:
                    mask = MASK_SATURDAY;
                    break;
                case Calendar.SUNDAY:
                    mask = MASK_SUNDAY;
                    break;
            }
            if ((mask & repeat) != 0) {
                ScheduleEvent newEvent = newInstance(this);
                // TODO: 8/18/2015 set time
            }
        }



        return events;
    }
}
