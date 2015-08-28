package com.lifeisle.jekton.order.stat.controller;

import android.widget.BaseAdapter;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.DeliverStatFragment;
import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.order.stat.adapter.GangerLogisticsStatListAdapter;
import com.lifeisle.jekton.order.stat.factory.GangerLogisticsStatFactory;

/**
 * @author Jekton
 */
public class GangerLogisticsStatController implements StatController {

    private DeliverStatModel mModel;
    private BaseAdapter mAdapter;

    public GangerLogisticsStatController(DeliverStatFragment fragment) {
        mModel = new DeliverStatModel(fragment,
                                      new GangerLogisticsStatFactory(),
                                      StatController.ACTION_STAT_TYPE_GANGER);
        mAdapter = new GangerLogisticsStatListAdapter(fragment.getActivity(), mModel);
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
