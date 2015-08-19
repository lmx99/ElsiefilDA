package com.lifeisle.jekton.schedule;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.lifeisle.android.R;

import java.util.List;

public class ScheduleTableActivity extends AppCompatActivity
        implements WeekView.EventClickListener, WeekView.MonthChangeListener, WeekView.EventLongPressListener, View.OnTouchListener {

    private static final String TAG = ScheduleTableActivity.class.getSimpleName();


    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int weekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView weekView;
    private RadioGroup options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);

        weekView = (WeekView) findViewById(R.id.weekView);

        weekView.setOnEventClickListener(this);
        weekView.setMonthChangeListener(this);
        weekView.setEventLongPressListener(this);
        weekView.setOnTouchListener(this);

        options = (RadioGroup) findViewById(R.id.weekViewOptions);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_schedule_table, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_option) {
            if (options.getVisibility() == View.VISIBLE) {
                options.setVisibility(View.GONE);
            } else {
                options.setVisibility(View.VISIBLE);
            }
            return true;
        } else if (itemId == R.id.action_today) {
            weekView.goToToday();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        // do nothing
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // the internal representation of month starts with 0
        return getEvents(newYear, newMonth - 1);
    }

    private List<WeekViewEvent> getEvents(int year, int month) {
        ScheduleReader reader = new ScheduleReader(year, month);
        return reader.getWeekViewEvent();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (options.getVisibility() == View.VISIBLE) {
            options.setVisibility(View.GONE);
        }
        return true;
    }
}
