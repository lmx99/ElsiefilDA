package com.lifeisle.jekton.data;

import android.provider.BaseColumns;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class ScheduleContract {

    public static final String OWN_EVENT_TITLE = "title";
    public static final String OWN_EVENT_START_TIME = "start_time";
    public static final String OWN_EVENT_END_TIME = "end_time";
    public static final String OWN_EVENT_REPEAT = "repeat";
    public static final String OWN_EVENT_NOTIFY = "notify";
    public static final String OWN_EVENT_TYPE = "type";


    public static final class EventEntry implements BaseColumns {

        public static final String TABLE_NAME = "events";

        public static final String COL_EVENT_TITLE = "title";
        public static final String COL_EVENT_START_TIME = "start_time";
        public static final String COL_EVENT_END_TIME = "end_time";
        public static final String COL_EVENT_REPEAT = "repeat";
        public static final String COL_EVENT_NOTIFY = "notify";
        public static final String COL_EVENT_TYPE = "type";

    }
}
