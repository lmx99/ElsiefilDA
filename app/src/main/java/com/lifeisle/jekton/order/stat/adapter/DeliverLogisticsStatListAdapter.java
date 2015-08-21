package com.lifeisle.jekton.order.stat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.order.stat.bean.DeliverLogisticsStatItem;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class DeliverLogisticsStatListAdapter extends StatListAdapter {

    private static final String LOG_TAG = "DeliverLogisticsStatListAdapter";

    public DeliverLogisticsStatListAdapter(Context context, DeliverStatModel statModel) {
        super(context, statModel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.widget_deliver_logistics_stat_item,
                             parent, false);

            ViewHolder holder = new ViewHolder();
            holder.feeTextView = (TextView) convertView.findViewById(R.id.fee);
            holder.numTextView = (TextView) convertView.findViewById(R.id.num);
            holder.allowanceTextView = (TextView) convertView.findViewById(R.id.allowance);
            holder.castTextView = (TextView) convertView.findViewById(R.id.cash);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }

        DeliverLogisticsStatItem item = (DeliverLogisticsStatItem) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.feeTextView.setText(item.fee);
        holder.numTextView.setText(item.num);
        holder.allowanceTextView.setText(item.allowance);
        holder.castTextView.setText(item.cash);
        holder.dateTextView.setText(item.date);

        return convertView;
    }


    private static class ViewHolder {
        TextView feeTextView;
        TextView numTextView;
        TextView allowanceTextView;
        TextView castTextView;
        TextView dateTextView;
    }
}
