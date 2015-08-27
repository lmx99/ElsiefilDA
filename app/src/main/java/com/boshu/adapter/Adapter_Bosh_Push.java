package com.boshu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
   /* ViewHolder holder = (ViewHolder) convertView.getTag();
    if (holder == null) {
        holder = new ViewHolder();
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
        holder.message = (TextView) convertView.findViewById(R.id.message);
        holder.time = (TextView) convertView.findViewById(R.id.time);
        holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
        holder.msgState = convertView.findViewById(R.id.msg_state);
        holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
        convertView.setTag(holder);
        	private static class ViewHolder {
		/** 和谁的聊天记录 */
   /*TextView name;
    *//** 消息未读数 *//*
    TextView unreadLabel;
    *//** 最后一条消息的内容 *//*
    TextView message;
    *//** 最后一条消息的时间 *//*
    TextView time;
    *//** 用户头像 *//*
    ImageView avatar;
    *//** 最后一条消息的发送状态 *//*
    View msgState;
    *//** 整个list中每一行总布局 *//*
    RelativeLayout list_item_layout;

}
    }*/
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        System.out.println(list.get(position).getType().equals("0")+"__________________________((((("+list.get(position).getType());

        if(convertView==null){
            view=LayoutInflater.from(context).inflate(R.layout.row_boshu_pushmessage,null);
        }else{
            view=convertView;
        }
        viewHolder holder = (viewHolder) view.getTag(R.id.push_new);
        if(holder==null) {
            holder = new viewHolder();
            holder.img_push1 =(ImageView) view.findViewById(R.id.push_img1);
          // holder.img_push1.setImageResource(R.drawable.pic_a);
            holder.tv_push1 = (TextView) view.findViewById(R.id.push_tv1);
            holder.tv_push1.setText("这是一个承诺");



            holder.img_push2 = (ImageView) view.findViewById(R.id.push_img2);
            holder.img_push2.setImageResource(R.drawable.pic_b);
            holder.tv_push2 = (TextView) view.findViewById(R.id.push_tv2);
            holder.tv_push2.setText("生活岛app已经上线啦!你的外卖，我的生活岛！么么哒");
            holder.linear_push2= (LinearLayout) view.findViewById(R.id.push_linear2);

            holder.img_push3 = (ImageView) view.findViewById(R.id.push_img3);
            holder.img_push3.setImageResource(R.drawable.pic_b);
            holder.tv_push3 = (TextView) view.findViewById(R.id.push_tv3);
            holder.tv_push3.setText("生活岛app已经上线啦!你的外卖，我的生活岛！么么哒");
            holder.linear_push3= (LinearLayout) view.findViewById(R.id.push_linear3);

            holder.img_push4 = (ImageView) view.findViewById(R.id.push_img4);
            holder.img_push4.setImageResource(R.drawable.pic_b);
            holder.tv_push4 = (TextView) view.findViewById(R.id.push_tv4);
            holder.tv_push4.setText("生活岛app已经上线啦!你的外卖，我的生活岛！么么哒");
            holder.linear_push4= (LinearLayout) view.findViewById(R.id.push_linear4);
            view.setTag(R.id.push_new,holder);
        }
        /*else{
            if(convertView==null){
                view=LayoutInflater.from(context).inflate(R.layout.row_boshu_message2,null);
            }else{
                view=convertView;
            }
            viewHolder2 holder2 = (viewHolder2) view.getTag(R.id.pushd_dialog);
            if(holder2==null){
                holder2 = new viewHolder2();
                holder2.pushUserHead= (ImageView) view.findViewById(R.id.pushUserHead);
                holder2.tv_push_message= (TextView) view.findViewById(R.id.tv_push_message);
                view.setTag(R.id.pushd_dialog,holder2);
            }
        }*/
        return view;

    }
    private static  class viewHolder{
        ImageView img_push1;
        ImageView img_push2;
        ImageView img_push3;
        ImageView img_push4;
        TextView tv_push1;
        TextView tv_push2;
        TextView tv_push3;
        TextView tv_push4;
      LinearLayout linear_push2;
      LinearLayout linear_push3;
         LinearLayout linear_push4;

    }
    private static  class viewHolder2{
        TextView tv_push_message;
        ImageView pushUserHead;

    }
}

