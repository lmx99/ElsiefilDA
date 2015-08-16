package com.lifeisle.jekton.schedule;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.util.ScheduleDBUtils;

import java.util.ArrayList;
import java.util.List;

public class ScheduleTableActivity extends AppCompatActivity
        implements WeekView.EventClickListener, WeekView.MonthChangeListener, WeekView.EventLongPressListener {

    private WeekView weekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);

        weekView = (WeekView) findViewById(R.id.weekView);

        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setEventLongPressListener(this);
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // the internal representation of month starts with 0
        return getEvents(newYear, newMonth - 1);
    }

    private List<WeekViewEvent> getEvents(int year, int month) {
        List<WeekViewEvent> events = new ArrayList<>();
        List<ScheduleEvent> scheduleEvents = ScheduleDBUtils.getScheduleEvents(year, month);
        for (ScheduleEvent scheduleEvent : scheduleEvents) {
            events.add(scheduleEvent.toWeekViewEvent());
        }
        return events;
    }
}
