package com.lifeisle.jekton.schedule;

import com.alamkanak.weekview.WeekViewEvent;
import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.util.ScheduleDBUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Jekton
 */
public class ScheduleReader {

    private int year;
    private int month;
    private int daysOfThisMonth;

    private List<ScheduleEvent> scheduleEvents;

    public ScheduleReader(int year, int month) {
        this.year = year;
        this.month = month;

        Calendar thisMonth = new GregorianCalendar(year, month, 1);
        daysOfThisMonth = thisMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

        scheduleEvents = ScheduleDBUtils.getScheduleEvents(year, month);
    }


    public List<WeekViewEvent> getWeekViewEvent() {
        spliceEvent();
        expandEvent();

        List<WeekViewEvent> events = new ArrayList<>();
        for (ScheduleEvent event : scheduleEvents) {
            events.add(event.toWeekViewEvent());
        }
        return events;
    }

    private void spliceEvent() {
        List<ScheduleEvent> events = new ArrayList<>(scheduleEvents.size());

        Calendar startTime = new GregorianCalendar();
        Calendar endTime = new GregorianCalendar();
        for (ScheduleEvent event : scheduleEvents) {
            startTime.setTimeInMillis(event.startMillis);
            endTime.setTimeInMillis(event.endMillis);

            int startDay = startTime.get(Calendar.DAY_OF_YEAR);
            int endDay = endTime.get(Calendar.DAY_OF_YEAR);
            if (startDay != endDay) {
                startDay = startTime.get(Calendar.DAY_OF_MONTH);
                endDay = startTime.get(Calendar.DAY_OF_MONTH);
                if (startTime.get(Calendar.MONTH) != month) {
                    startTime.set(year, month, 1, 0, 0, 0);
                    startDay = 1;
                }
                if (endTime.get(Calendar.MONTH) != month) {
                    endTime.set(year, month, daysOfThisMonth, 23, 59, 59);
                    endDay = daysOfThisMonth;
                }
                Calendar midnight = new GregorianCalendar();
                midnight.set(year, month, startDay, 23, 59, 59);

                for (int i = startDay; i < endDay; ++i) {
                    ScheduleEvent spliced = ScheduleEvent.newInstance(event);
                    spliced.startMillis = startTime.getTimeInMillis();
                    spliced.endMillis = midnight.getTimeInMillis();
                    events.add(spliced);

                    // next day
                    startTime.set(year, month, i + 1, 0, 0, 0);
                    midnight.add(Calendar.DAY_OF_MONTH, 1);
                }
                ScheduleEvent last = ScheduleEvent.newInstance(event);
                last.startMillis = startTime.getTimeInMillis();
                last.endMillis = endTime.getTimeInMillis();
                events.add(last);
            } else {
                events.add(event);
            }
        }

        scheduleEvents = events;
    }

    private void expandEvent() {

    }
}
