package com.lifeisle.jekton.order.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author Jekton
 * @version 0.01 7/16/2015
 */
public class ResultListAdapter extends BaseAdapter {

    private static final String TAG = "ResultListAdapter";

    private Context mContext;
    private List<OrderItem> mOrderItems;

    /**
     * Used by {@link OrderListItem}, place in this place to reduce object creation since a inner
     * class can't have a static field.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    public ResultListAdapter(Context context, List<OrderItem> orderItems) {
        mContext = context;
        mOrderItems = orderItems;
    }


    @Override
    public int getCount() {
        return mOrderItems.size();
    }

    @Override
    public OrderItem getItem(int position) {
        return mOrderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderListItem listItem;
        if (convertView == null) {
            listItem = new OrderListItem();
        } else {
            listItem = (OrderListItem) convertView;
        }

        listItem.fillData(getItem(position));
        return listItem;
    }








    /**
     * ItemView that used by {@link ResultListAdapter}
     *
     * @author Jekton Luo
     */
    public class OrderListItem extends RelativeLayout implements View.OnClickListener {

        private TextView mOrderNum;
        private TextView mTime;
        private TextView mDetailsInfo;
        private TextView mOrderInfo;

        public OrderListItem() {
            super(mContext);

            LayoutInflater inflater = LayoutInflater.from(mContext);
            inflater.inflate(R.layout.widget_order_search_result_item, this, true);
            setOnClickListener(this);

            mOrderNum = (TextView) findViewById(R.id.orderNum);
            mTime = (TextView) findViewById(R.id.time);
            mDetailsInfo = (TextView) findViewById(R.id.detailsInfo);
            mOrderInfo = (TextView) findViewById(R.id.orderInfo);
        }


        @Override
        public void onClick(View v) {
            toggleDetailView();
        }

        private void toggleDetailView() {
            if (mDetailsInfo.getVisibility() != GONE) {
                mDetailsInfo.setVisibility(View.GONE);
            } else {
                mDetailsInfo.setVisibility(View.VISIBLE);
            }
        }

        public void fillData(OrderItem order) {
            mOrderInfo.setText(order.getOrderInfo());

            if (mDetailsInfo.getVisibility() != GONE) {
                mDetailsInfo.setVisibility(View.GONE);
            }

            mDetailsInfo.setText(order.getDetails());
            mOrderNum.setText(order.goodsItems == null ? "" : "" + order.goodsItems.length);

            mTime.setText(dateFormat.format(order.addTime));

        }

    }


}
