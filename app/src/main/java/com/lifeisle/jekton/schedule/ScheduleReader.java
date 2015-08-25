package com.lifeisle.jekton.schedule;

import com.alamkanak.weekview.WeekViewEvent;
import com.lifeisle.jekton.schedule.data.ScheduleEvent;
import com.lifeisle.jekton.util.ScheduleDBUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Jekton
 */
public class ScheduleReader {

    private static final String TAG = ScheduleReader.class.getSimpleName();

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
        splitEvent();
        expandEvent();

        List<WeekViewEvent> events = new ArrayList<>();
        for (ScheduleEvent event : scheduleEvents) {
//            Calendar startTime = new GregorianCalendar();
//            startTime.setTimeInMillis(event.startMillis);
//            Calendar endTime = new GregorianCalendar();
//            endTime.setTimeInMillis(event.endMillis);
//            Logger.d(TAG, "start: month = " + startTime.get(Calendar.MONTH) +
//                    ", day = " + startTime.get(Calendar.DAY_OF_MONTH));
//            Logger.d(TAG, "end: month = " + endTime.get(Calendar.MONTH) +
//                    ", day = " + endTime.get(Calendar.DAY_OF_MONTH));
            events.add(event.toWeekViewEvent());
        }
        return events;
    }

    private void splitEvent() {
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
        List<ScheduleEvent> events = new ArrayList<>(scheduleEvents.size());

        for (ScheduleEvent event : scheduleEvents) {
            if (event.repeat == ScheduleEvent.REPEAT_NEVER) {
                events.add(event);
            } else {
                Calendar startTime = getRepeatTime(event.startMillis);
                Calendar endTime = getRepeatTime(event.endMillis);
                Calendar cursor = new GregorianCalendar(year, month, 1);

                for (int day = 1; day <= daysOfThisMonth; ++day) {
                    if (event.repeat == ScheduleEvent.REPEAT_EVERYDAY) {
                        // duplicated but can avoid to compute the repeat mask
                        ScheduleEvent copy = newCopy(event, startTime, endTime, day);
                        events.add(copy);
                    } else {
                        int mask = getRepeatMask(cursor);
                        if ((mask & event.repeat) != 0) {
                            ScheduleEvent copy = newCopy(event, startTime, endTime, day);
                            events.add(copy);
                        }
                    }
                    cursor.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
        
        scheduleEvents = events;
    }

    private int getRepeatMask(Calendar calendar) {
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                return ScheduleEvent.MASK_MONDAY;
            case Calendar.TUESDAY:
                return ScheduleEvent.MASK_TUESDAY;
            case Calendar.WEDNESDAY:
                return ScheduleEvent.MASK_WEDNESDAY;
            case Calendar.THURSDAY:
                return ScheduleEvent.MASK_THURSDAY;
            case Calendar.FRIDAY:
                return ScheduleEvent.MASK_FRIDAY;
            case Calendar.SATURDAY:
                return ScheduleEvent.MASK_SATURDAY;
            case Calendar.SUNDAY:
                return ScheduleEvent.MASK_SUNDAY;
            default:
                return 0;
        }
    }

    private Calendar getRepeatTime(long millis) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(millis);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);

        return c;
    }

    private ScheduleEvent newCopy(ScheduleEvent event, Calendar startTime, Calendar endTime,
                                  int dayOfMonth) {
        startTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        endTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        ScheduleEvent copy = ScheduleEvent.newInstance(event);
        copy.startMillis = startTime.getTimeInMillis();
        copy.endMillis = endTime.getTimeInMillis();

        return copy;
    }
}
