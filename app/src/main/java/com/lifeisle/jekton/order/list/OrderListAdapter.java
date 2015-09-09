package com.lifeisle.jekton.order.list;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.order.EventIDMapper;
import com.lifeisle.jekton.order.OrderItem;
import com.lifeisle.jekton.order.OrderOperateActivity;
import com.lifeisle.jekton.util.DimensionUtils;
import com.lifeisle.jekton.util.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Jekton
 * @version 0.01 7/16/2015
 */
public class OrderListAdapter extends BaseAdapter {

    private static final String TAG = "OrderListAdapter";


    /**
     * For {@link com.lifeisle.jekton.order.list.OrderListAdapter.OrderListItem}
     */
    private static final long MILLI_30_MIN = 30 * 60 * 1000;
    private static final long MILLI_45_MIN = 45 * 60 * 1000;
    private static final long MILLI_60_MIN = 60 * 60 * 1000;

    private static final int URGENCY_LEVEL_NORMAL =
            MyApplication.getInstance().getResources().getColor(R.color.bg_order_list_item);
    private static final int URGENCY_LEVEL_LIGHT = Color.YELLOW;
    private static final int URGENCY_LEVEL_MIDDLE = Color.rgb(255, 165, 0);
    private static final int URGENCY_LEVEL_SEVER = Color.RED;

    private static final int WIDTH_URGENCY_NORMAL = 0;
    private static final int WIDTH_URGENCY_LIGHT = DimensionUtils.dp2px(MyApplication.getInstance(), 8);
    private static final int WIDTH_URGENCY_MIDDLE = DimensionUtils.dp2px(MyApplication.getInstance(), 16);
    private static final int WIDTH_URGENCY_SEVER = DimensionUtils.dp2px(MyApplication.getInstance(), 24);


    private QRCodeScanActivity activity;
    private OrderModel orderModel;

    /**
     * Used by {@link OrderListItem}, place in this place to reduce object creation since a inner
     * class can't have a static field.
     */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    public OrderListAdapter(QRCodeScanActivity activity, OrderModel orderModel) {
        this.activity = activity;
        this.orderModel = orderModel;
    }


    @Override
    public int getCount() {
        return orderModel.getSize();
    }

    @Override
    public OrderItem getItem(int position) {
        return orderModel.getItem(position);
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

        listItem.position = position;
        OrderItem orderItem = getItem(position);

        if (!listItem.isDetailHidden()) {
            listItem.hideDetails();
        }

        if (listItem.isDeliveredButtonEnable()) {
            listItem.setDeliverButtonEnable(false);
        }

        listItem.fillData(orderItem);
        return listItem;
    }








    /**
     * ItemView that used by {@link OrderListAdapter}
     *
     * @author Jekton Luo
     * @version 0.03 7/20/2015.
     */
    public class OrderListItem extends RelativeLayout implements View.OnClickListener {

        public static final String EXTRA_ORDER_CODE = "OrderListItem.EXTRA_ORDER_UPDATE";

        private TextView btnDeliver;
        private TextView btnReceivedByAgent;
        private TextView btnMore;
        private TextView tvOrderNum;
        private TextView tvTime;
        private TextView tvDetailsInfo;
        private TextView tvOrderInfo;
        private View mUrgencyIndicator;

        private int position;
        private OrderItem orderItem;

        private int mUrgencyIndicatorOriginHeight;
        private int mUrgencyIndicatorExpandedHeight;

        public OrderListItem() {
            super(activity);

            LayoutInflater inflater = LayoutInflater.from(activity);
            inflater.inflate(R.layout.widget_order_list_item, this, true);
            setOnClickListener(this);
            setWillNotDraw(false);

            btnDeliver = (TextView) findViewById(R.id.deliver);
            btnReceivedByAgent = (TextView) findViewById(R.id.receivedByAgent);
            btnMore = (TextView) findViewById(R.id.more);
            tvOrderNum = (TextView) findViewById(R.id.orderNum);
            tvTime = (TextView) findViewById(R.id.time);
            tvDetailsInfo = (TextView) findViewById(R.id.detailsInfo);
            tvOrderInfo = (TextView) findViewById(R.id.orderInfo);
            mUrgencyIndicator = findViewById(R.id.urgency_indicator);

            btnDeliver.setOnClickListener(this);
            btnReceivedByAgent.setOnClickListener(this);
            btnMore.setOnClickListener(this);
        }


        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // calculate once time
            if (mUrgencyIndicatorOriginHeight == 0) {
                mUrgencyIndicatorOriginHeight = computeHeight();
                post(new Runnable() {
                    @Override
                    public void run() {
                        mUrgencyIndicator.getLayoutParams().height = mUrgencyIndicatorOriginHeight;
                        Logger.d(TAG, "mUrgencyIndicatorOriginHeight = " + mUrgencyIndicatorOriginHeight);
                        mUrgencyIndicator.requestLayout();
                    }
                });
            }

            if (mUrgencyIndicatorExpandedHeight == 0 && !isDetailHidden()) {
                mUrgencyIndicatorExpandedHeight = computeHeight();
                post(new Runnable() {
                    @Override
                    public void run() {
                        mUrgencyIndicator.getLayoutParams().height = mUrgencyIndicatorExpandedHeight;
                        Logger.d(TAG, "dispatchDraw() mUrgencyIndicatorExpandedHeight = "
                                + mUrgencyIndicatorExpandedHeight);
                        mUrgencyIndicator.requestLayout();
                    }
                });
            }

        }

        private int computeHeight() {
            return getHeight();
        }



        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.deliver:
                    postDeliveredOrder(orderItem.orderID, EventIDMapper.EVENT_DELIVERED);
                    break;
                case R.id.receivedByAgent:
                    postDeliveredOrder(orderItem.orderID, EventIDMapper.EVENT_RECEIVED_BY_AGENT);
                    break;
                case R.id.more:
                    Intent intent = new Intent(activity, OrderOperateActivity.class);
                    intent.putExtra(EXTRA_ORDER_CODE, orderItem.orderCode);
                    activity.startActivity(intent);
                    break;
                default:
                    if (orderItem != null && orderItem.orderID != -1) {
                        toggleDetailView();
                    }
                    break;
            }
        }

        private void postDeliveredOrder(int orderID, int eventID) {
            activity.postDeliveredOrder(orderID, eventID, position);
        }

        private void toggleDetailView() {
            if (!isDetailHidden()) {
                tvDetailsInfo.setVisibility(View.GONE);
                mUrgencyIndicator.getLayoutParams().height = mUrgencyIndicatorOriginHeight;
            } else {
                tvDetailsInfo.setVisibility(View.VISIBLE);
                if (mUrgencyIndicatorExpandedHeight > 0) {
                    mUrgencyIndicator.getLayoutParams().height = mUrgencyIndicatorExpandedHeight;
                }
            }
        }


        public void fillData(OrderItem order) {
            orderItem = order;

            tvOrderInfo.setText(order.getOrderInfo());

            if (!isDetailHidden()) {
                tvDetailsInfo.setVisibility(View.GONE);
            }

            tvDetailsInfo.setText(order.getDetails());
            tvOrderNum.setText(order.goodsItems == null ? "" : "" + order.goodsItems.length);

            tvTime.setText(dateFormat.format(order.addTime));
            setUrgencyLevel();

            setButtonsEnable(order.orderID > 0);

            if (order.isPostFail() || order.isDelivered() || order.isAbnormal()) {
                if (isDeliveredButtonEnable()) {
                    setDeliverButtonEnable(false);
                }
            } else {
                if (!isDeliveredButtonEnable()) {
                    setDeliverButtonEnable(true);
                }
            }


        }

        private void setUrgencyLevel() {
            Date date = new Date();
            long delta = date.getTime() - orderItem.createTime.getTime();
            Logger.d(TAG, "create time delta = " + delta);

            int color;
            int width;
            if (delta > MILLI_60_MIN) {
                color = URGENCY_LEVEL_SEVER;
                width = WIDTH_URGENCY_SEVER;
            } else if (delta > MILLI_45_MIN) {
                color = URGENCY_LEVEL_MIDDLE;
                width = WIDTH_URGENCY_MIDDLE;
            } else if (delta > MILLI_30_MIN) {
                color = URGENCY_LEVEL_LIGHT;
                width = WIDTH_URGENCY_LIGHT;
            } else {
                color = URGENCY_LEVEL_NORMAL;
                width = WIDTH_URGENCY_NORMAL;
            }
            mUrgencyIndicator.setBackgroundColor(color);
            mUrgencyIndicator.getLayoutParams().width = width;
            mUrgencyIndicator.requestLayout();
            mUrgencyIndicator.invalidate();
        }


        public boolean isDetailHidden() {
            return tvDetailsInfo.getVisibility() == GONE;
        }

        public void hideDetails() {
            tvDetailsInfo.setVisibility(View.GONE);
        }


        public void setDeliverButtonEnable(boolean enabled) {
            btnDeliver.setEnabled(enabled);
            btnReceivedByAgent.setEnabled(enabled);
        }

        public boolean isDeliveredButtonEnable() {
            return btnDeliver.isEnabled();
        }


        public void setButtonsEnable(boolean enabled) {
            btnMore.setEnabled(enabled);
//            btnDetail.setEnabled(enabled);
            setDeliverButtonEnable(enabled);
        }

    }


}
