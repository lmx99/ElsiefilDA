package com.lifeisle.jekton.order.stat.controller;

import android.widget.BaseAdapter;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.DeliverStatFragment;
import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.order.stat.adapter.DeliverLogisticsStatListAdapter;
import com.lifeisle.jekton.order.stat.bean.DeliverLogisticsStatFactory;

/**
 * @author Jekton
 */
public class DeliverLogisticsStatController implements StatController {

    private DeliverStatModel mModel;
    private BaseAdapter mAdapter;

    public DeliverLogisticsStatController(DeliverStatFragment fragment) {
        mModel = new DeliverStatModel(fragment,
                                      new DeliverLogisticsStatFactory(),
                                      StatController.ACTION_STAT_TYPE_DELIVER);
        mAdapter = new DeliverLogisticsStatListAdapter(fragment.getActivity(), mModel);
    }

    @Override
    public int getResId() {
        return R.layout.fragment_deliver_logistics_stat;
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
