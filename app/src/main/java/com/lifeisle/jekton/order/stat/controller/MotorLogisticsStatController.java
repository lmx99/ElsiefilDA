package com.lifeisle.jekton.order.stat.controller;

import android.widget.BaseAdapter;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.DeliverStatFragment;
import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.order.stat.adapter.MotorLogisticsStatListAdapter;
import com.lifeisle.jekton.order.stat.factory.MotorLogisticsStatFactory;

/**
 * @author Jekton
 */
public class MotorLogisticsStatController implements StatController {

    private DeliverStatModel mModel;
    private BaseAdapter mAdapter;

    public MotorLogisticsStatController(DeliverStatFragment fragment) {
        mModel = new DeliverStatModel(fragment,
                                      new MotorLogisticsStatFactory(),
                                      StatController.ACTION_STAT_TYPE_MOTOR);
        mAdapter = new MotorLogisticsStatListAdapter(fragment.getActivity(), mModel);
    }


    @Override
    public int getResId() {
        return R.layout.fragment_motor_logistics_stat;
    }

    @Override
    public DeliverStatModel getDeliverStatModel() {
        return mModel;
    }

    @Override
    public BaseAdapter getAdapter() {
        return mAdapter;
    }
}
