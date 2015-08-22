package com.lifeisle.jekton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.OrderItem;
import com.lifeisle.jekton.order.EventIDMapper;
import com.lifeisle.jekton.ui.adapter.GoodsListAdapter;
import com.lifeisle.jekton.ui.adapter.OrderListAdapter;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.OrderDBUtils;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * @author Jekton
 * @version 0.01 7/17/2015
 */
public class OrderOperateActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String SELECT_ALL = "OrderOperateActivity.SELECT_ALL";

    private static final String TAG = "OrderOperateActivity";

    private boolean selectedAll;

    private OrderItem orderItem;
    private GoodsListAdapter adapter;
    private RadioButton rbDeliver;
    private RadioGroup eventRatioGroup;
    private EditText etReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_operate);

        findViewById(R.id.ok).setOnClickListener(this);
        rbDeliver = (RadioButton) findViewById(R.id.deliver);
        eventRatioGroup = (RadioGroup) findViewById(R.id.eventGroup);
        etReason = (EditText) findViewById(R.id.reason);

        initOrderItem();
    }

    private void initOrderItem() {
        Intent intent = getIntent();
        final String orderCode = intent.getStringExtra(OrderListAdapter.OrderListItem.EXTRA_ORDER_CODE);
        selectedAll = intent.getBooleanExtra(SELECT_ALL, false);
        if (orderCode == null) {
            Toaster.showShort(this, R.string.error_order_code_null);
            finish();
            return;
        }

        orderItem = OrderDBUtils.getOrderItem(orderCode);
        initView();
    }

    private void initView() {
        ListView goodsListView = (ListView) findViewById(R.id.goodsList);

        adapter = new GoodsListAdapter(this, orderItem.goodsItems, selectedAll);
        goodsListView.setAdapter(adapter);

        rbDeliver.setChecked(selectedAll);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onClick(View v) {
        final Set<Integer> checkedSet = getGoodsCheckedSet();
        if (checkedSet.size() == 0) {
            Toaster.showShort(this, R.string.error_no_goods_item_selected);
            return;
        }

        Logger.d(TAG, checkedSet.toString());

        final int eventID = getEventID();
        Logger.d(TAG, "eventID = " + eventID);

        MyApplication.addToRequestQueue(new AutoLoginRequest(this, Request.Method.POST, StringUtils.getServerPath(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Logger.d(TAG, "response: " + response);
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject order = OrderItem.getOrderItemAt(response, 0);
                                            OrderItem item = OrderItem.newOrderItem(order);
                                            OrderItem.updateLogistics(item.goodsItems);
                                        } catch (JSONException e) {
                                            Logger.e(TAG, e.toString(), e);
                                        }
                                    }
                                }.start();

                                Toaster.showShort(OrderOperateActivity.this, R.string.success_post);
                                finish();
                            }
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString());
                            Toaster.showShort(OrderOperateActivity.this, R.string.error_fail_post);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toaster.showShort(OrderOperateActivity.this, R.string.error_fail_post);
                    }
                }) {

            @Override
            protected void setParams(Map<String, String> params) {
                for (int i : checkedSet) {
                    params.put("item_ids[]", "" + i);
                }
                params.put("event_id", "" + eventID);
                params.put("remarks", etReason.getText().toString());
                params.put("sys", "lgst");
                params.put("ctrl", "lgst_nor");
                params.put("action", "items_lgst");
            }
        });
    }

    private Set<Integer> getGoodsCheckedSet() {
        return adapter.getCheckedIDSet();
    }

    private int getEventID() {
        switch (eventRatioGroup.getCheckedRadioButtonId()) {
            case R.id.deliver:
                Logger.d(TAG, "R.id.deliver selected");
                return EventIDMapper.EVENT_DELIVERED;
            case R.id.receivedByAgent:
                Logger.d(TAG, "R.id.receivedByAgent selected");
                return EventIDMapper.EVENT_RECEIVED_BY_AGENT;
            case R.id.supplement:
                Logger.d(TAG, "R.id.supplement selected");
                return EventIDMapper.EVENT_SUPPLEMENT;
            case R.id.returnGoods:
                Logger.d(TAG, "R.id.returnGoods selected");
                return EventIDMapper.EVENT_RETURN_GOODS;
            case R.id.exchange:
                Logger.d(TAG, "R.id.exchange selected");
                return EventIDMapper.EVENT_EXCHANGE;
            case R.id.other:
                Logger.d(TAG, "R.id.other selected");
                return EventIDMapper.EVENT_OTHER;
            default:
                return -1;
        }
    }
}