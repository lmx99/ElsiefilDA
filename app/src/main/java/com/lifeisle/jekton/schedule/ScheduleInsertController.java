package com.lifeisle.jekton.schedule;

import android.app.Activity;
import android.view.View;

import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.util.DateUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author Jekton
 * @version 0.1 8/10/2015
 */
public class ScheduleInsertController extends ScheduleController
        implements View.OnClickListener {

    private ScheduleOperateModel mScheduleOperateModel;

    public ScheduleInsertController(ScheduleDetailView view, ScheduleOperateModel scheduleOperateModel) {
        super(view);

        mScheduleOperateModel = scheduleOperateModel;

        Activity activity = (Activity) view;
        View btn = activity.findViewById(R.id.ok);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(this);

        setupDefaultTime();
    }

    private void setupDefaultTime() {
        Calendar today = new GregorianCalendar();
        ScheduleDetailView detailView = getScheduleDetailView();

        String date = DateUtils.formatDate(today);
        String time = DateUtils.formatTime(today);

        detailView.setStartTime(date, time);
        detailView.setEndTime(date, time);
    }


    @Override
    public void onClick(View v) {
        ScheduleEvent event = getEvent();
        if (event != null) {
            mScheduleOperateModel.insertEvent(event);
            getScheduleDetailView().close();
        }
    }
}
