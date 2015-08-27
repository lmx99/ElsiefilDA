package com.lifeisle.jekton.order;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lifeisle.jekton.order.list.OrderModel;
import com.lifeisle.jekton.order.list.QRCodeScanActivity;

/**
 * @author Jekton
 */
public class OrderDataCleanService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        OrderModel.cleanUpData();

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
