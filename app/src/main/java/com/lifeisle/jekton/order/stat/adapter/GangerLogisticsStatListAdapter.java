package com.lifeisle.jekton.order.stat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.DeliverStatModel;
import com.lifeisle.jekton.order.stat.bean.GangerLogisticsStatItem;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class GangerLogisticsStatListAdapter extends StatListAdapter {

    public GangerLogisticsStatListAdapter(Context context, DeliverStatModel statModel) {
        super(context, statModel);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(getContext())
                            .inflate(R.layout.widget_ganger_logistics_stat_item,
                                     parent, false);

            ViewHolder holder = new ViewHolder();
            holder.gangFeeTextView = (TextView) convertView.findViewById(R.id.gang_fee);
            holder.allowanceTextView = (TextView) convertView.findViewById(R.id.allowance);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }

        GangerLogisticsStatItem item = (GangerLogisticsStatItem) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.gangFeeTextView.setText(item.gangFee);
        holder.allowanceTextView.setText(item.allowance);
        holder.dateTextView.setText(item.date);

        return convertView;
    }


    private static class ViewHolder {
        TextView gangFeeTextView;
        TextView allowanceTextView;
        TextView dateTextView;
    }
}
