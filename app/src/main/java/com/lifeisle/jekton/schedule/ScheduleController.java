package com.lifeisle.jekton.schedule;

import com.lifeisle.android.R;
import com.lifeisle.jekton.schedule.data.ScheduleContract;
import com.lifeisle.jekton.schedule.data.ScheduleEvent;
import com.lifeisle.jekton.util.DateUtils;
import com.lifeisle.jekton.util.Logger;

import java.util.Arrays;

/**
 * @author Jekton
 * @version 0.1 8/10/2015
 */
public abstract class ScheduleController {

    private ScheduleDetailView mScheduleDetailView;
    private static final String TAG = ScheduleController.class.getSimpleName();

    public ScheduleController(ScheduleDetailView scheduleDetailView) {
        mScheduleDetailView = scheduleDetailView;
    }



    protected ScheduleDetailView getScheduleDetailView() {
        return mScheduleDetailView;
    }


    protected ScheduleEvent getEvent() {
        String title = mScheduleDetailView.getEventTitle();
        if (title.equals("")) {
            mScheduleDetailView.showErrMsg(R.string.error_event_title_empty);
            return null;
        }

        String[] startTime = mScheduleDetailView.getStartTime();
        long startMillis = DateUtils.translateTimeMillis(startTime[0], startTime[1]);
        String[] endTime = mScheduleDetailView.getEndTime();
        long endMillis = DateUtils.translateTimeMillis(endTime[0], endTime[1]);
        Logger.d(TAG, "startTime = " + Arrays.toString(startTime) +
                ", endTime = " + Arrays.toString(endTime));
        if (endMillis < startMillis) {
            mScheduleDetailView.showErrMsg(R.string.error_start_greater_end);
            return null;
        }

        int repeat = mScheduleDetailView.getRepeat();
        int notify = mScheduleDetailView.getNotify();

        ScheduleEvent event = new ScheduleEvent();
        event.title = title;
        event.startMillis = startMillis;
        event.endMillis = endMillis;
        event.repeat = repeat;
        event.notify = notify;
        event.type = ScheduleContract.EventEntry.TYPE_PERSONAL;
        event.needPost = true;

        return event;
    }
}
