package com.lifeisle.jekton.util;

import android.test.AndroidTestCase;

/**
 * @author Jekton
 * @version 0.1 8/6/2015
 */
public class testDateUtils extends AndroidTestCase {


    public void testExtraYear() {
        String date = "1994-07-27";
        int year = DateUtils.extractYear(date);
        assertEquals(1994, year);

        date = "2015-08-06";
        year = DateUtils.extractYear(date);
        assertEquals(2015, year);

    }

    public void testExtraMonth() {
        String date = "1994-07-27";
        int month = DateUtils.extractMonth(date);
        assertEquals(7, month);

        date = "2015-08-06";
        month = DateUtils.extractMonth(date);
        assertEquals(8, month);

    }

    public void testExtraDay() {
        String date = "1994-07-27";
        int day = DateUtils.extractDay(date);
        assertEquals(27, day);

        date = "2015-08-06";
        day = DateUtils.extractDay(date);
        assertEquals(6, day);

    }


    public void testFormatDateString() {
        int year = 1994;
        int month = 7;
        int day = 27;

        String date = DateUtils.formatDate(year, month, day);
        assertEquals("1994-07-27", date);
    }
}