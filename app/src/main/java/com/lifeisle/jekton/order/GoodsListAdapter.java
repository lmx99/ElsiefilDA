package com.lifeisle.jekton.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.list.OrderItem;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Jekton
 * @version 0.01 7/30/2015
 */
public class GoodsListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {

    private Context context;
    private OrderItem.GoodsItem[] goodsItems;

    private Set<Integer> checkedIDSet;

    public GoodsListAdapter(Context context, OrderItem.GoodsItem[] goodsItems, boolean allChecked) {
        this.context = context;
        this.goodsItems = goodsItems;

        checkedIDSet = new HashSet<>(goodsItems.length);
        if (allChecked) {
            for (OrderItem.GoodsItem item : goodsItems) {
                checkedIDSet.add(item.itemID);
            }
        }
    }


    @Override
    public int getCount() {
        return goodsItems.length;
    }

    @Override
    public OrderItem.GoodsItem getItem(int position) {
        return goodsItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.widget_order_operate_goods_item, parent, false);
        }
        CheckBox checkBox = (CheckBox) convertView;

        OrderItem.GoodsItem goodsItem = getItem(position);
        checkBox.setText(position + ": " + goodsItem.courseName);
        checkBox.setId(goodsItem.itemID);
        checkBox.setOnCheckedChangeListener(this);

        checkBox.setChecked(checkedIDSet.contains(goodsItem.itemID));

        return checkBox;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (isChecked)
            checkedIDSet.add(id);
        else
            checkedIDSet.remove(id);
    }



    public Set<Integer> getCheckedIDSet() {
        return checkedIDSet;
    }
}
