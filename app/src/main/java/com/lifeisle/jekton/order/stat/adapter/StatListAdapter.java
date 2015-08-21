package com.lifeisle.jekton.order.stat.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.util.Logger;

/**
 * @author Jekton
 */
public abstract class StatListAdapter extends BaseAdapter {

    private static final String TAG = StatListAdapter.class.getSimpleName();
    private Context mContext;
    private DeliverStatModel mStatModel;

    public StatListAdapter(Context context, DeliverStatModel statModel) {
        mContext = context;
        mStatModel = statModel;
    }

    @Override
    public int getCount() {
        int count = mStatModel.getCount();
        Logger.d(TAG, "getCount() count = " + count);
        return count;
    }

    @Override
    public Object getItem(int position) {
        return mStatModel.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected Context getContext() {
        return mContext;
    }
}
