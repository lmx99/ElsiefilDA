package com.lifeisle.jekton.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.job.data.bean.MyJobItem;
import com.lifeisle.jekton.job.widget.FlowLayout;

/**
 * @author Jekton
 */
public class MyJobPeriodsAdapter extends BaseAdapter {

    private Context mContext;
    private MyJobItem mItem;

    private String mFromText;
    private String mToText;


    public MyJobPeriodsAdapter(Context context, MyJobItem item) {
        mContext = context;
        mItem = item;

        mFromText = context.getString(R.string.from);
        mToText = context.getString(R.string.to);
    }

    @Override
    public int getCount() {
        return mItem.periods.length;
    }

    @Override
    public MyJobItem.Period getItem(int position) {
        return mItem.periods[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.widget_my_job_period, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.period = (TextView) view.findViewById(R.id.period);
            holder.peerGroup = (FlowLayout) view.findViewById(R.id.peers);
            view.setTag(holder);
        } else {
            view = convertView;
        }

        MyJobItem.Period periodItem = getItem(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        String periodText = String.format("%s%s ~ %s%s",
                                          mFromText, periodItem.startTime,
                                          mToText, periodItem.endTime);
        holder.period.setText(periodText);
        for (MyJobItem.Period.Peer peer : periodItem.peers) {
            TextView textView = new TextView(mContext);
            textView.setText(peer.userName);
            holder.peerGroup.addView(textView);
        }

        return view;
    }


    class ViewHolder {
        TextView period;
        FlowLayout peerGroup;
    }


}
