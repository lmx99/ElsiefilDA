package com.lifeisle.jekton.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderController;
import com.lifeisle.jekton.order.OrderModel;
import com.lifeisle.jekton.order.OrderView;
import com.lifeisle.jekton.ui.adapter.OrderListAdapter;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Toaster;
import com.zxing.activity.CaptureActivity;


/**
 * @author Jekton
 * @version 0.2 8/10/2015
 */
public class QRCodeScanActivity extends AppCompatActivity
        implements View.OnClickListener, OrderView, RadioGroup.OnCheckedChangeListener,
        View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String ORDER_DELIVERED = "OrderOperateReceiver.ORDER_DELIVERED";
    public static final String ORDER_LOGISTICS_UPDATE = "OrderOperateReceiver.ORDER_LOGISTICS_UPDATE";


    private static final String TAG = "QRCodeScanActivity";

    private static final int REQUEST_CODE_SCAN_QR_CODE = 0;

    private SwipeRefreshLayout swipeRefreshLayout;

    private TextView tvFailCount;
    private String failCountTemplateOne;
    private String failCountTemplateOther;

    private ProgressDialog progressDialog;
    private BaseAdapter orderListAdapter;

    private LinearLayout options;
    private RadioGroup orderOption;
    private RadioGroup sortOption;
    private RadioGroup sortOptionExtra;

    /**
     * used when click {@link QRCodeScanActivity#tvFailCount}, at {@link #onClick(View)}
     */
    private RadioButton rbPostFailed;
    /**
     * used when click the scan button, at {@link #onClick(View)}
     */
    private RadioButton rbAllOrder;

    private IntentFilter orderOperateFilter;
    private OrderOperateReceiver orderOperateReceiver = new OrderOperateReceiver();


    private OrderController orderController;
    private OrderModel orderModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        orderOperateFilter = new IntentFilter();
        orderOperateFilter.addAction(ORDER_DELIVERED);
        orderOperateFilter.addAction(ORDER_LOGISTICS_UPDATE);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        initFailCount();

        View btnScan = findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(this);
        btnScan.setOnTouchListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        initMVC();
    }

    private void initFailCount() {
        tvFailCount = (TextView) findViewById(R.id.failCount);
        tvFailCount.setOnClickListener(this);

        Resources resources = getResources();
        failCountTemplateOne = resources.getQuantityString(R.plurals.error_fail_count, 1);
        failCountTemplateOther = resources.getQuantityString(R.plurals.error_fail_count, 2);
    }

    private void initMVC() {
        ListView listView = (ListView) findViewById(R.id.orderList);
        listView.setOnTouchListener(this);

        orderModel = new OrderModel(this);
        orderListAdapter = new OrderListAdapter(this, orderModel);
        orderController = new OrderController(this, orderModel);

        listView.setAdapter(orderListAdapter);

        initOptions();
    }

    private void initOptions() {
        options = (LinearLayout) findViewById(R.id.options);

        orderOption = (RadioGroup) options.findViewById(R.id.orderOption);
        sortOption = (RadioGroup) options.findViewById(R.id.sortOption);
        sortOptionExtra = (RadioGroup) options.findViewById(R.id.sortOptionExtra);

        orderOption.setOnCheckedChangeListener(this);
        sortOption.setOnCheckedChangeListener(this);
        sortOptionExtra.setOnCheckedChangeListener(this);

        setOptions();

        rbPostFailed = (RadioButton) findViewById(R.id.postFail);
        rbAllOrder = (RadioButton) findViewById(R.id.all);
    }



    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(orderOperateReceiver, orderOperateFilter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        // deliverState at OrderOperateActivity
        orderController.notifyDataSetChanged(true);
    }


    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(orderOperateReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        orderModel.stop();
    }


    @Override
    public void onBackPressed() {
        if (options.getVisibility() == View.VISIBLE) {
            options.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qr_code_scan, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.optionMenu) {
            if (options.getVisibility() == View.INVISIBLE) {
                options.setVisibility(View.VISIBLE);
            } else {
                options.setVisibility(View.INVISIBLE);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setOptions() {
        int orderOption = getOrderOption();
        int sortOption = getSortOption();
        boolean ascending = getSortOptionExtra();

        orderController.setOptions(orderOption, sortOption, ascending);
    }

    private int getOrderOption() {
        switch (orderOption.getCheckedRadioButtonId()) {
            default:
            case R.id.all:
                return OrderController.OPTION_ORDER_ALL;
            case R.id.undelivered:
                return OrderController.OPTION_ORDER_UNDELIVERED;
            case R.id.delivered:
                return OrderController.OPTION_ORDER_DELIVERED;
            case R.id.abnormal:
                return OrderController.OPTION_ORDER_ABNORMAL;
            case R.id.postFail:
                return OrderController.OPTION_ORDER_POST_FAIL;
        }
    }

    private int getSortOption() {
        switch (sortOption.getCheckedRadioButtonId()) {
            default:
            case R.id.sortByCreateTime:
                return OrderController.OPTION_SORT_BY_CREATE_TIME;
        }
    }

    private boolean getSortOptionExtra() {
        return sortOptionExtra.getCheckedRadioButtonId() == R.id.ascending;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_SCAN_QR_CODE && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            Logger.d(TAG, "scan result: " + result);
            orderController.postQRCode(result);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                Intent startScan = new Intent(this, CaptureActivity.class);
                startActivityForResult(startScan, REQUEST_CODE_SCAN_QR_CODE);
                if (!rbAllOrder.isChecked())
                    rbAllOrder.setChecked(true);
                break;
            case R.id.failCount:
                rbPostFailed.setChecked(true);
                break;
        }

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        closeOptionMenu();
        return false;
    }

    private void closeOptionMenu() {
        if (options.getVisibility() == View.VISIBLE)
            options.setVisibility(View.INVISIBLE);
    }



    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        setOptions();
    }

    @Override
    public void showDialog() {
        progressDialog.show();
    }

    @Override
    public void closeDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void notifyDataSetChanged() {
        orderListAdapter.notifyDataSetChanged();
        Logger.d(TAG, "notifyDataSetChanged()");
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void setFailCount(int count) {
        if (count > 0) {
            String text;
            if (count > 1) {
                text = String.format(failCountTemplateOther, count);
            } else {
                text = String.format(failCountTemplateOne, count);
            }
            tvFailCount.setText(text);
            if (tvFailCount.getVisibility() == View.GONE) {
                tvFailCount.setVisibility(View.VISIBLE);
                tvFailCount.requestLayout();
            }
        } else if (tvFailCount.getVisibility() == View.VISIBLE) {
            tvFailCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void startScanActivity() {
        Intent startScan = new Intent(this, CaptureActivity.class);
        startActivityForResult(startScan, REQUEST_CODE_SCAN_QR_CODE);
    }

    @Override
    public void showErrMsg(int msgId) {
        Toaster.showShort(this, msgId);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        orderController.notifyDataSetChanged(false);
    }


    private class OrderOperateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            switch (action) {
                case ORDER_DELIVERED:
                    closeOptionMenu();
                    int orderID = intent.getIntExtra(OrderListAdapter.OrderListItem.EXTRA_ORDER_CODE, -1);
                    int eventID = intent.getIntExtra(OrderListAdapter.OrderListItem.EXTRA_EVENT_ID, -1);
                    orderController.postDeliveredOrder(orderID, eventID);
                    break;
                case ORDER_LOGISTICS_UPDATE:
                    orderController.notifyDataSetChanged(false);
                    break;
            }

        }
    }



}
