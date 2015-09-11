package com.lifeisle.jekton.job.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.job.data.bean.SubordinateItem;
import com.lifeisle.jekton.job.widget.FlowLayout;
import com.lifeisle.jekton.util.DimensionUtils;

/**
 * @author Jekton
 */
public class SubordinatePeriodsAdapter extends BaseAdapter {

    private Context mContext;
    private SubordinateItem mItem;

    private String mFromText;
    private String mToText;
    private int mPeerTextViewPadding;

    public SubordinatePeriodsAdapter(Context context, SubordinateItem item) {
        mContext = context;
        mItem = item;

        mFromText = context.getString(R.string.from);
        mToText = context.getString(R.string.to);
        mPeerTextViewPadding = DimensionUtils.dp2px(context, 6);
    }

    @Override
    public int getCount() {
        return mItem.periods.length;
    }

    @Override
    public SubordinateItem.Period getItem(int position) {
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

        SubordinateItem.Period periodItem = getItem(position);

        ViewHolder holder = (ViewHolder) view.getTag();
        String periodText = String.format("%s%s\n%s%s",
                                          mFromText, periodItem.startTime,
                                          mToText, periodItem.endTime);
        holder.period.setText(periodText);
        holder.peerGroup.removeAllViews();
        for (SubordinateItem.Period.Peer peer : periodItem.peers) {
            TextView textView = new TextView(mContext);
            textView.setPadding(mPeerTextViewPadding,
                                mPeerTextViewPadding,
                                mPeerTextViewPadding,
                                mPeerTextViewPadding);
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
