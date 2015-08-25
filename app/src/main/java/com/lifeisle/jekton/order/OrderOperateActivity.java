package com.lifeisle.jekton.order;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.lifeisle.jekton.order.list.OrderItem;
import com.lifeisle.jekton.order.list.OrderListAdapter;
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

    private Object requestTag = new Object();


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_operate, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_call) {
            try {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + orderItem.mobilePhone));
                startActivity(callIntent);
            } catch (ActivityNotFoundException e) {
                Toaster.showShort(this, R.string.error_fail_call_user);
                Logger.e("Calling a Phone Number", "Call failed", e);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.cancelRequest(requestTag);
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

        AutoLoginRequest request =
                new AutoLoginRequest(
                        this,
                        Request.Method.POST,
                        StringUtils.getServerPath(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(final JSONObject response) {
                                Logger.d(TAG, "response: " + response);
                                try {
                                    int status = response.getInt("status");
                                    if (status == 0) {
                                        new Thread(new LogisticsUpdateRunnable(response)).start();

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
                        }
                ) {

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
                };
        request.setTag(requestTag);
        MyApplication.addToRequestQueue(request);
    }

    private Set<Integer> getGoodsCheckedSet() {
        return adapter.getCheckedIDSet();
    }

    private int getEventID() {
        switch (eventRatioGroup.getCheckedRadioButtonId()) {
            case R.id.deliver:
                return EventIDMapper.EVENT_DELIVERED;
            case R.id.receivedByAgent:
                return EventIDMapper.EVENT_RECEIVED_BY_AGENT;
            case R.id.supplement:
                return EventIDMapper.EVENT_SUPPLEMENT;
            case R.id.returnGoods:
                return EventIDMapper.EVENT_RETURN_GOODS;
            case R.id.exchange:
                return EventIDMapper.EVENT_EXCHANGE;
            case R.id.other:
                return EventIDMapper.EVENT_OTHER;
            default:
                return -1;
        }
    }



    public static final class LogisticsUpdateRunnable implements Runnable {

        private JSONObject mResponse;

        LogisticsUpdateRunnable(JSONObject response) {
            mResponse = response;
        }

        @Override
        public void run() {
            try {
                JSONObject order = OrderItem.getOrderItemAt(mResponse, 0);
                OrderItem item = OrderItem.newOrderItem(order);
                OrderItem.updateLogistics(item.goodsItems);
            } catch (JSONException e) {
                Logger.e(TAG, e.toString(), e);
            }
        }
    }
}