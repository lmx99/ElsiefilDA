package com.lifeisle.jekton.util;

import android.test.AndroidTestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class testScheduleDBUtils extends AndroidTestCase {

    public void testCalendar() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar start = new GregorianCalendar(2015, 7, 1);
        String formattedTime = dateFormat.format(start.getTime());
        assertEquals("2015-08-01 00:00:00", formattedTime);


        Calendar end = new GregorianCalendar(2015, 7, 31, 23, 59, 59);
        formattedTime = dateFormat.format(end.getTime());
        assertEquals("2015-08-31 23:59:59", formattedTime);


        int days[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        for (int i = 0; i < 12; ++i) {
            Calendar calendar = new GregorianCalendar(2015, i, i);
            assertEquals(days[i], calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        int anotherDays[] = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        for (int i = 0; i < 12; ++i) {
            Calendar calendar = new GregorianCalendar(2012, i, i);
            assertEquals(anotherDays[i], calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
    }
}
