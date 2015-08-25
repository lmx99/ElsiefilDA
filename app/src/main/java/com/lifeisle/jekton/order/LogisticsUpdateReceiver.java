package com.lifeisle.jekton.order;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.lifeisle.jekton.bean.OrderItem;
import com.lifeisle.jekton.order.list.QRCodeScanActivity;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.OrderDBUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 * @version 0.01 7/31/2015
 */
public class LogisticsUpdateReceiver extends BroadcastReceiver {

    private static final String TAG = "LogisticsUpdateReceiver";

    public static final String ACTION_LOGISTICS_UPDATE
            = "com.lifeisle.jekton.order.LogisticsUpdateReceiver.ACTION_LOGISTICS_UPDATE";
    public static final String EXTRA_ORDER_UPDATE = "EXTRA_ORDER_UPDATE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String extra = intent.getStringExtra(EXTRA_ORDER_UPDATE);
        final Handler handler = new Handler();
        Logger.d(TAG, extra);
        try {
            JSONObject jsonObject = new JSONObject(extra);
            switch (jsonObject.getString("type")) {
                case "logistics_update":
                    final String orderCode = jsonObject.getString("order_code");
                    if (orderCode.equals("")) return;

                    new Thread() {
                        @Override
                        public void run() {
                            Logger.d(TAG, "orderCode = " + orderCode);
                            if (OrderDBUtils.isOrderCodeExists(orderCode)) {
                                OrderDBUtils.setNeedRequest(orderCode, OrderItem.REQUEST_LOGISTICS_UPDATE);
                            } else {
                                OrderDBUtils.insertOrderCode(orderCode, OrderItem.REQUEST_LOGISTICS_UPDATE);
                            }

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent notifyDataSetChanged = new Intent(QRCodeScanActivity.ORDER_LOGISTICS_UPDATE);
                                    context.sendBroadcast(notifyDataSetChanged);
                                }
                            });
                        }
                    }.start();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
}
