package com.lifeisle.jekton.order.list;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lifeisle.jekton.order.OrderDBUtils;
import com.lifeisle.jekton.util.Preferences;

/**
 * @author Jekton
 */
public class OrderCleanService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Preferences.setJCatID(-1);
        OrderDBUtils.clearOrders();

        Intent notifyDataSetChanged = new Intent(QRCodeScanActivity.ORDER_LOGISTICS_UPDATE);
        sendBroadcast(notifyDataSetChanged);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
