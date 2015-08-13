package com.boshu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.boshu.domain.Push;
import com.lifeisle.android.R;

import java.util.List;

/**
 * Created by amou on 9/8/2015.
 */
public class Adapter_Bosh_Push extends BaseAdapter {
    private Context context;
    private List<Push> list;
    private ListView listView;

    public Adapter_Bosh_Push(Context context, List<Push> list, ListView listView) {
        this.context = context;
        this.list = list;
        this.listView = listView;
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=LayoutInflater.from(context).inflate(R.layout.row_boshu_pushmessage,null);
        return convertView;
    }
}

