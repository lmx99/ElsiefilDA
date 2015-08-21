package com.lifeisle.jekton.order.stat.controller;

import android.widget.BaseAdapter;

import com.lifeisle.jekton.order.stat.DeliverStatModel;

/**
 * @author Jekton
 */
public interface StatController {

    String ACTION_STAT_TYPE_DELIVER = "edate_data";
    String ACTION_STAT_TYPE_MOTOR = "motor_date_data";

    int getResId();

    DeliverStatModel getDeliverStatModel();

    BaseAdapter getAdapter();
}
