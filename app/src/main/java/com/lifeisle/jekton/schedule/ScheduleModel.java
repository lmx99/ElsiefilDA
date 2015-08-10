package com.lifeisle.jekton.schedule;

import android.content.Context;

import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.util.ScheduleDBUtils;
import com.lifeisle.jekton.util.Toaster;

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
        long id = ScheduleDBUtils.insertScheduleEvent(event);

        if (id < 0) {
            Toaster.showShort(context, R.string.error_fail_to_add_event);
        } else {
            Toaster.showShort(context, R.string.success_add_event);
        }
    }

}
