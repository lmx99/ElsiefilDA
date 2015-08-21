package com.lifeisle.jekton.order.stat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.order.stat.bean.MotorLogisticsStatItem;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class MotorLogisticsStatListAdapter extends StatListAdapter {

    public MotorLogisticsStatListAdapter(Context context, DeliverStatModel statModel) {
        super(context, statModel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.widget_motor_logistics_stat_item,
                                     parent, false);

            ViewHolder holder = new ViewHolder();
            holder.motorFeeTextView = (TextView) convertView.findViewById(R.id.motor_fee);
            holder.allowanceTextView = (TextView) convertView.findViewById(R.id.allowance);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }

        MotorLogisticsStatItem item = (MotorLogisticsStatItem) getItem(position);
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
