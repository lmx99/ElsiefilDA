package com.lifeisle.jekton.job.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifeisle.android.R;
import com.lifeisle.jekton.job.SubordinateActivity;
import com.lifeisle.jekton.job.data.bean.MyJobItem;
import com.lifeisle.jekton.job.data.bean.SubordinateItem;


/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class SubordinateListAdapter extends BaseAdapter implements View.OnClickListener {

    public static final String EXTRA_SUBORDINATE = "SubordinateListAdapter.EXTRA_SUBORDINATE";

    private Context mContext;
    private MyJobItem mItem;

    public SubordinateListAdapter(Context context, MyJobItem item) {
        mContext = context;
        mItem = item;
    }

    @Override
    public int getCount() {
        return mItem.subordinates.length;
    }

    @Override
    public SubordinateItem getItem(int position) {
        return mItem.subordinates[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(mContext);
            textView.setOnClickListener(this);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    mContext.getResources().getDrawable(R.drawable.icon_greater),
                    null);
        } else {
            textView = (TextView) convertView;
        }

        textView.setId(position);
        textView.setText(getItem(position).title);
        return textView;
    }

    @Override
    public void onClick(View v) {
        Gson gson = new Gson();
        String subordinate = gson.toJson(getItem(v.getId()));

        Intent intent = new Intent(mContext, SubordinateActivity.class);
        intent.putExtra(EXTRA_SUBORDINATE, subordinate);
        mContext.startActivity(intent);
    }
}
