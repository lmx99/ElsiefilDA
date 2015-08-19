package com.lifeisle.jekton.schedule;

import android.content.Context;

import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.ScheduleEvent;
import com.lifeisle.jekton.schedule.data.ScheduleContract;
import com.lifeisle.jekton.util.DateUtils;
import com.lifeisle.jekton.util.ScheduleDBUtils;
import com.lifeisle.jekton.util.Toaster;

/**
 * @author Jekton
 * @version 0.1 8/7/2015
 */
public class ScheduleOperateModel {

    private Context context;
    private ScheduleDetailView view;
    private ScheduleEvent event;


    public ScheduleOperateModel(Context context) {
        this(context, -1);
    }

    public ScheduleOperateModel(Context context, long eventId) {
        this.context = context;
        view = (ScheduleDetailView) context;

        if (eventId >= 0) {
            initEventView(eventId);
            view.setActionBarTitle(R.string.title_activity_schedule_detail);

            if (event.type == ScheduleContract.EventEntry.TYPE_JOB) {
                view.setEnabled(false);
            }
        }
    }

    private void initEventView(long eventId) {
        ScheduleEvent event = ScheduleDBUtils.getScheduleEvent(eventId);
        if (event == null) {
            view.showErrMsg(R.string.error_event_not_found);
            view.close();
            return;
        }
        this.event = event;

        view.setEventTitle(event.title);
        String[] startTime = DateUtils.timeMillis2String(event.startMillis);
        view.setStartTime(startTime[0], startTime[1]);
        String[] endTime = DateUtils.timeMillis2String(event.endMillis);
        view.setEndTime(endTime[0], endTime[1]);
        view.setRepeat(event.repeat);
        view.setNotify(event.notify);
    }



    public void insertEvent(ScheduleEvent event) {
        long id = ScheduleDBUtils.insertScheduleEvent(event);

        if (id < 0) {
            Toaster.showShort(context, R.string.error_fail_to_add_event);
        } else {
            Toaster.showShort(context, R.string.success_add_event);
        }
    }


    public void updateEvent(ScheduleEvent event) {
        event.id = this.event.id;
        event.type = this.event.type;
        int count = ScheduleDBUtils.updateScheduleEvent(event);

        if (count == 0) {
            Toaster.showShort(context, R.string.error_fail_to_update_event);
        } else {
            Toaster.showShort(context, R.string.success_update_event);
        }
    }

    public void deleteEvent() {
        int count = ScheduleDBUtils.deleteScheduleEvent(event.id);

        if (count == 0) {
            Toaster.showShort(context, R.string.error_fail_to_delete_event);
        } else {
            Toaster.showShort(context, R.string.success_delete_event);
        }
    }
}
