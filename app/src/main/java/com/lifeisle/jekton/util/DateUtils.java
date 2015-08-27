package com.lifeisle.jekton.util;

import com.lifeisle.android.R;
import com.lifeisle.jekton.schedule.data.ScheduleEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class DateUtils {

    private DateUtils() {
        throw new AssertionError("Can not instantiate this class.");
    }


    public static String[] getDefaultDeliverStatInterval() {
        Calendar now = new GregorianCalendar();
        int month = now.get(Calendar.MONTH);
        int year = now.get(Calendar.YEAR);

        if (month == 0) {
            month = 11;
            --year;
        } else {
            --month;
        }

        // a month ago
        now.set(Calendar.MONTH, month);
        now.set(Calendar.YEAR, year);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] interval = new String[2];
        interval[0] = dateFormat.format(now.getTime());

        now.add(Calendar.MONTH, 1);
        interval[1] = dateFormat.format(now.getTime());

        return interval;
    }

    /**
     * @return date format of "yyyy-MM-dd"
     */
    public static String formatDate(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        return formatDate(year, monthOfYear, dayOfMonth);
    }

    /**
     * @return date format of "yyyy-MM-dd"
     */
    public static String formatDate(int year, int monthOfYear, int dayOfMonth) {
        return String.format("%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
    }

    /**
     * @param date "yyyy-MM-dd" format
     * @return year in <code>int</code>
     */
    public static int extractYear(String date) {
        return Integer.parseInt(date.substring(0, 4));
    }

    /**
     * @param date "yyyy-MM-dd" format
     * @return month in <code>int</code>
     */
    public static int extractMonth(String date) {
        return Integer.parseInt(date.substring(5, 7)) - 1;
    }

    /**
     * @param date "yyyy-MM-dd" format
     * @return day in <code>int</code>
     */
    public static int extractDay(String date) {
        return Integer.parseInt(date.substring(8, 10));
    }

    /**
     * @param time time in "hh:mm" format
     * @return hour in <code>int</code>
     */
    public static int extractHour(String time) {
        return Integer.parseInt(time.substring(0, 2));
    }

    /**
     * @param time time in "hh:mm" format
     * @return minute in <code>int</code>
     */
    public static int extractMin(String time) {
        return Integer.parseInt(time.substring(3, 5));
    }

    /**
     * @return time in "hh:mm" format
     */
    public static String formatTime(Calendar calendar) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return formatTime(hourOfDay, minute);
    }

    /**
     * @param hourOfDay hour of day
     * @param minute minute of hour
     * @return time in "hh:mm" format
     */
    public static String formatTime(int hourOfDay, int minute) {
        return String.format("%02d:%02d", hourOfDay, minute);
    }


    public static String formatRepeatOfWeekString(int daysOfWeek) {
        switch (daysOfWeek) {
            case ScheduleEvent.REPEAT_EVERYDAY:
                return StringUtils.getStringFromResource(R.string.repeat_opt_everyday);
            case ScheduleEvent.REPEAT_WEEKDAY:
                return StringUtils.getStringFromResource(R.string.repeat_opt_weekdays);
            case ScheduleEvent.REPEAT_NEVER:
                return StringUtils.getStringFromResource(R.string.repeat_opt_never);
        }

        final int NUM_DAYS_OF_WEEK = 7;
        String[] days = StringUtils.getStringsFromResource(R.array.day_of_week_abbrs);
        int day = 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < NUM_DAYS_OF_WEEK; ++i) {
            if ((daysOfWeek & day) != 0) {
                builder.append(days[i]);
                builder.append(",");
            }
            day <<= 1;
        }
        return builder.substring(0, builder.length() - 1);
    }


    public static long translateTimeMillis(String date, String time) {
        int year = extractYear(date);
        int month = extractMonth(date);
        int day = extractDay(date);
        int hour = extractHour(time);
        int min = extractMin(time);

        return new GregorianCalendar(year, month, day, hour, min).getTimeInMillis();
    }

    public static String[] timeMillis2String(long millis) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);

        String[] result = new String[2];
        result[0] = formatDate(calendar);
        result[1] = formatTime(calendar);

        return result;
    }
}
