package com.lifeisle.jekton.job.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lifeisle.android.R;


/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class SubordinateListAdapter extends BaseAdapter {

    private Context context;

    public SubordinateListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setText("TestItem");
            textView.setTextColor(Color.BLACK);
//            textView.setTextSize();
            textView.setBackgroundColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    context.getResources().getDrawable(R.drawable.icon_greater), null);
        } else {
            textView = (TextView) convertView;
        }

        return textView;
    }
}
