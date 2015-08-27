package com.lifeisle.jekton.order.list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lifeisle.jekton.order.OrderDBUtils;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;

/**
 * @author Jekton
 */
public class JobTimeOutReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = JobTimeOutReceiver.class.getSimpleName();

    public static final String ACTION_TIMEOUT =
            "com.lifeisle.jekton.order.list.JobTimeOutReceiver.ACTION_TIMEOUT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(LOG_TAG, "onReceive");
        Preferences.setJCatID(-1);
        OrderDBUtils.clearOrders();

        Intent notifyDataSetChanged = new Intent(QRCodeScanActivity.ORDER_LOGISTICS_UPDATE);
        context.sendBroadcast(notifyDataSetChanged);
    }
}
