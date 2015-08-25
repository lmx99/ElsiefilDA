package com.lifeisle.jekton.schedule;

import android.app.Activity;
import android.view.View;

import com.lifeisle.android.R;
import com.lifeisle.jekton.schedule.data.ScheduleEvent;

/**
 * @author Jekton
 * @version 0.1 8/10/2015
 */
public class ScheduleUpdateController extends ScheduleController implements View.OnClickListener {

    private ScheduleOperateModel mScheduleOperateModel;

    public ScheduleUpdateController(ScheduleDetailView scheduleDetailView,
                                    ScheduleOperateModel scheduleOperateModel) {
        super(scheduleDetailView);

        mScheduleOperateModel = scheduleOperateModel;

        Activity activity = (Activity) scheduleDetailView;
        View okButton = activity.findViewById(R.id.ok);
        okButton.setVisibility(View.VISIBLE);
        okButton.setOnClickListener(this);

        View deleteButton = activity.findViewById(R.id.delete);
        deleteButton.setVisibility(View.VISIBLE);
        deleteButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ok) {
            ScheduleEvent event = getEvent();
            mScheduleOperateModel.updateEvent(event);
        } else {
            mScheduleOperateModel.deleteEvent();
        }
        getScheduleDetailView().close();
    }
}
