package com.lifeisle.jekton.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.MotorLogisticsStatItem;
import com.lifeisle.jekton.model.DeliverStatModel;
import com.lifeisle.jekton.util.Logger;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class MotorLogisticsStatListAdapter extends BaseAdapter {

    private static final String LOG_TAG = "DeliverLogisticsStatListAdapter";

    private Context context;
    private DeliverStatModel statModel;

    public MotorLogisticsStatListAdapter(Context context, DeliverStatModel statModel) {
        this.context = context;
        this.statModel = statModel;
    }

    @Override
    public int getCount() {
        int count = statModel.getCount();
        Logger.d(LOG_TAG, "getCount() count = " + count);
        return count;
    }

    @Override
    public MotorLogisticsStatItem getItem(int position) {
        return (MotorLogisticsStatItem) statModel.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.widget_motor_logistics_stat_item,
                    parent, false);

            ViewHolder holder = new ViewHolder();
            holder.motorFeeTextView = (TextView) convertView.findViewById(R.id.motor_fee);
            holder.allowanceTextView = (TextView) convertView.findViewById(R.id.allowance);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }

        MotorLogisticsStatItem item = getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.motorFeeTextView.setText(item.motorFee);
        holder.allowanceTextView.setText(item.allowance);
        holder.dateTextView.setText(item.date);

        return convertView;
    }


    private static class ViewHolder {
        TextView motorFeeTextView;
        TextView allowanceTextView;
        TextView dateTextView;
    }
}
