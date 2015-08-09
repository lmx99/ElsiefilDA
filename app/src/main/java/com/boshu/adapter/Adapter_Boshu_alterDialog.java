package com.boshu.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lifeisle.android.R;

public class Adapter_Boshu_alterDialog extends BaseAdapter{
    private Context context;
    private String[] ss;
    public Adapter_Boshu_alterDialog(Context context,String[] ss){
        this.context=context;
        this.ss=ss;
        
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ss.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return ss[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        ViewGroup group=(ViewGroup) inflater.inflate(R.layout.alterdialog_list_item, null);
        TextView tv=(TextView) group.findViewById(R.id.tv);
        tv.setText(ss[position]);
        return group;
    }
  

    

}
