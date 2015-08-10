package com.lifeisle.jekton.schedule;

import android.content.Context;

import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.util.ScheduleDBUtils;

/**
 * @author Jekton
 * @version 0.1 8/7/2015
 */
public class ScheduleModel {

    private Context context;


    public ScheduleModel(Context context) {
        this.context = context;
    }



    public void insertEvent(final ScheduleEvent event) {
        new Thread() {
            @Override
            public void run() {
                ScheduleDBUtils.insertScheduleEvent(event);
            }
        }.start();
    }

}
