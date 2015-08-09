package com.lifeisle.jekton.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.DeliverStatItem;
import com.lifeisle.jekton.model.DeliverStatModel;
import com.lifeisle.jekton.util.Logger;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class DeliverStatListAdapter extends BaseAdapter {

    private static final String LOG_TAG = "DeliverStatListAdapter";

    private Context context;
    private DeliverStatModel statModel;

    public DeliverStatListAdapter(Context context, DeliverStatModel statModel) {
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
    public DeliverStatItem getItem(int position) {
        return statModel.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.widget_deliver_stat_item,
                    parent, false);

            ViewHolder holder = new ViewHolder();
            holder.feeTextView = (TextView) convertView.findViewById(R.id.fee);
            holder.numTextView = (TextView) convertView.findViewById(R.id.num);
            holder.allowanceTextView = (TextView) convertView.findViewById(R.id.allowance);
            holder.castTextView = (TextView) convertView.findViewById(R.id.cash);
            holder.dateTextView = (TextView) convertView.findViewById(R.id.date);

            convertView.setTag(holder);
        }

        DeliverStatItem item = getItem(position);
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
