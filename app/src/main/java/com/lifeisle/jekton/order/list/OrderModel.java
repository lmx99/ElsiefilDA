package com.lifeisle.jekton.order.list;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderDBUtils;
import com.lifeisle.jekton.order.OrderOperateActivity;
import com.lifeisle.jekton.order.list.sorter.OrderSorter;
import com.lifeisle.jekton.order.list.updater.OrderListUpdater;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jekton
 * @version 0.3 8/5/2015
 */
public class OrderModel {

    private static final String TAG = "OrderModel";
    private static final String SERVER_PATH = StringUtils.getServerPath();

    private static final int WHAT_UPDATE_ORDER_ITEM = 0;

    private Context context;
    private OrderView orderView;
    private Handler handler;

    private ExecutorService executorService;
    private int orderRequestCount;

    volatile private List<OrderItem> orderItems;
    /**
     * 用于跟踪是否需要更新内存中的数据
     */
    private int initCount;

    // strategies
    private OrderSorter orderSorter;
    private OrderListUpdater orderListUpdater;

    private int jcat_id = Preferences.getJCatID();



    public OrderModel(Context context) {
        this.context = context;
        orderView = (OrderView) context;

        orderItems = new ArrayList<>();

        executorService = Executors.newSingleThreadExecutor();

        handler = new OrderUpdateHandler(this);
        setupJobTimeOutAlarm();
    }


    public void reloadData(boolean showDialog) {
        init(orderListUpdater, showDialog);
    }


    public void retrieveAllScannedData() {
        RetrieveAllScannedOrderRequest request = new RetrieveAllScannedOrderRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getInt("status") == 0) {
                                if (!executorService.isShutdown()) {
                                    executorService.execute(new FillAllScannedOrderTask(jsonObject));
                                }
                            } else {
                                reloadData(false);
                                Logger.e(TAG, "error occurred when retrieve all scanned orders");

                            }
                        } catch (JSONException e) {
                            reloadData(false);
                            Logger.e(TAG, "error occurred when retrieve all scanned orders", e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        reloadData(false);
                        Logger.e(TAG,
                                 "error occurred when retrieve all scanned orders" + volleyError);
                    }
                }
        );
        MyApplication.addToRequestQueue(request);
    }


    public OrderItem getItem(int index) {
        return orderItems.get(index);
    }


    public int getSize() {
        return orderItems.size();
    }


    public void setOrderListUpdater(OrderListUpdater orderListUpdater) {
        this.orderListUpdater = orderListUpdater;
    }

    public void setOrderSorter(final OrderSorter orderSorter) {
        this.orderSorter = orderSorter;
    }


    private void init(final OrderListUpdater orderListUpdater, final boolean showDialog) {
        ++initCount;
        Logger.d(TAG, "init() initCount = " + initCount);
        if (showDialog)
            orderView.showDialog();
        orderRequestCount = 0;
        orderView.setFailCount(orderRequestCount);
        handler.removeMessages(WHAT_UPDATE_ORDER_ITEM);

        final int currentInitCount = initCount;
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Logger.d(TAG, "init()");
                List<OrderItem> orders = orderListUpdater.init();
                if (orderSorter != null) {
                    orders = orderSorter.sort(orders);
                }

                for (int i = 0, size = orders.size(); i < size; ++i) {
                    OrderItem item = orders.get(i);
                    if (item.requestType != OrderItem.REQUEST_NO_NEED) {
                        postOrderCode(item.orderCode, i, item.requestType, currentInitCount);
                    }
                }
                Logger.d(TAG, "init() finish");
                final List<OrderItem> orderList = orders;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        orderItems = orderList;
                        orderView.notifyDataSetChanged();
                        Logger.d(TAG, "init() orderItems.size() = " + orderItems.size());
                        if (showDialog)
                            orderView.closeDialog();
                    }
                });
            }
        });
    }




    public void stop() {
        executorService.shutdown();
    }


    public void addOrder(String orderCode) {
        if (jcat_id < 0) {
            Toaster.showShort(context, R.string.error_not_sign_in_job);
            return;
        }

        switch (OrderDBUtils.getOrderExistsState(orderCode)) {
            case OrderDBUtils.ORDER_STATE_NOT_EXIST:
                addOrderHelper(orderCode);
                break;
            case OrderDBUtils.ORDER_STATE_EXIST:
                Intent intent = new Intent(context, OrderOperateActivity.class);
                intent.putExtra(OrderListAdapter.OrderListItem.EXTRA_ORDER_CODE, orderCode);
                intent.putExtra(OrderOperateActivity.SELECT_ALL, true);
                context.startActivity(intent);
                break;
            case OrderDBUtils.ORDER_STATE_EXIST_BUT_NOT_DATA:
                Toaster.showShort(context, R.string.error_order_code_existed);
                break;
        }
    }

    private void addOrderHelper(String orderCode) {
        OrderItem orderItem = new OrderItem(orderCode);
        orderItems.add(orderItem);
        orderView.notifyDataSetChanged();
        executorService.execute(new OrderCodeInsertTask(orderCode));
        postOrderCode(orderCode, orderItems.size() - 1, OrderItem.REQUEST_ALL_DATA, initCount);
    }



    public void signInAJob(JSONObject jsonObject) {
        QRInfoRequest request =
                new QRInfoRequest(
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Logger.d(TAG, "signInAJob() response: " + response);
                                parseSignInResponse(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Logger.e(TAG, error);
                                Toaster.showShort(context, R.string.error_fail_sign_in_jobs_1);
                            }
                        }
                );
        MyApplication.addToRequestQueue(request);
    }

    private void parseSignInResponse(JSONObject response) {
        try {
            int status = response.getInt("status");
            switch (status) {
                case 0:
                    readJCatId(response);
                    break;
                case 2:
                    if (jcat_id > 0) {
                        Toaster.showShort(context, R.string.error_fail_sign_in_jobs_2_1);
                    } else {
                        try {
                            readJCatId(response);
                        } catch (JSONException ex) {
                            Toaster.showShort(context, R.string.error_fail_sign_in_jobs_2_2);
                        }
                    }
                    break;
                case 1:
                default:
                    Toaster.showShort(context, R.string.error_fail_sign_in_jobs_1);
                    break;
            }
        } catch (JSONException e) {
            Toaster.showShort(context, R.string.error_fail_sign_in_jobs_1);
            Logger.e(TAG, "Sign In Jobs response: " + response, e);
        }
    }

    private void readJCatId(JSONObject response) throws JSONException{
        jcat_id = response.getInt("jcat_id");
        Preferences.setJCatID(jcat_id);
        Toaster.showShort(context, R.string.success_sign_in_jobs);
        setupJobTimeOutAlarm();
    }


    private void setupJobTimeOutAlarm() {
        Intent intent = new Intent(context, JobTimeOutReceiver.class);
//        intent.setAction(JobTimeOutReceiver.ACTION_TIMEOUT);

        final int requestCode = 0;
//        PendingIntent pendingIntent =
//                PendingIntent.getBroadcast(context, requestCode, intent,
//                                           PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent =
                PendingIntent.getService(context, requestCode, intent,
                                         PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar now = new GregorianCalendar();
//        now.add(Calendar.HOUR_OF_DAY, 3);
        now.add(Calendar.MINUTE, 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, now.getTimeInMillis(), pendingIntent);
        Logger.d(TAG, "set alarm " + now);
    }


    public void signOutJob(JSONObject jsonObject) {
        MyApplication.addToRequestQueue(new QRInfoRequest(jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.d(TAG, "signOutAJob() response: " + response);
                        try {
                            if (response.getInt("status") == 0) {
                                Preferences.setJCatID(-1);
                                OrderDBUtils.clearOrders();
                                Toaster.showShort(context, R.string.success_sign_out_jobs);
                            } else {
                                Toaster.showShort(context, R.string.error_fail_sign_out_jobs);
                            }
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString(), e);
                            Logger.e(TAG, "Sign Out Jobs response: " + response);
                            Toaster.showShort(context, R.string.error_fail_sign_out_jobs);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(TAG, error);
                        Toaster.showShort(context, R.string.error_fail_sign_out_jobs);
                    }
                }));
    }

    public void enterJob(JSONObject jsonObject) {
        QRInfoRequest request =
                new QRInfoRequest(
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
//                                Logger.d(TAG, "enterJob() response: " + response);
                                try {
                                    int status = response.getInt("status");
                                    switch (status) {
                                        case 0:
                                            Toaster.showShort(context, R.string.success_enter_jobs);
                                            break;
                                        case 1:
                                            Toaster.showShort(context, R.string.error_fail_enter_jobs_1);
                                            break;
                                        case 2:
                                            Toaster.showShort(context, R.string.error_fail_enter_jobs_2);
                                            break;
                                        case 3:
                                            Toaster.showShort(context, R.string.error_fail_enter_jobs_3);
                                            break;
                                        case 5:     // without 4
                                            Toaster.showShort(context, R.string.error_fail_enter_jobs_5);
                                            break;
                                        case 6:
                                            Toaster.showShort(context, R.string.error_fail_enter_jobs_6);
                                            break;
                                        default:
                                            Toaster.showShort(context, R.string.error_fail_enter_jobs);
                                            break;
                                    }
                                } catch (JSONException e) {
                                    Logger.e(TAG, e.toString(), e);
                                    Logger.e(TAG, "enter job response: " + response);
                                    Toaster.showShort(context, R.string.error_fail_enter_jobs);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Logger.e(TAG, error);
                                Toaster.showShort(context, R.string.error_fail_enter_jobs);
                            }
                        }
                );

        MyApplication.addToRequestQueue(request);
    }




    private void postOrderCode(final String orderCode,final int index, final int requestType,
                               final int currentInitCount) {
        orderRequestCount++;
        MyApplication.addToRequestQueue(new OrderItemInfoRequest(orderCode, requestType,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.d(TAG, "postOrderCode response(" + orderCode + "): \n" + response);
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                orderRequestCount--;
                                updateLogistics(response, index, currentInitCount);
                            } else {
                                OrderDBUtils.deleteOrderCode(orderCode);
                                Toaster.showShort(context, R.string.error_order_code_invalid);
                                reloadData(false);
                            }
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString(), e);
                            orderView.setFailCount(orderRequestCount);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        orderView.setFailCount(orderRequestCount);
                        if (!executorService.isTerminated()) {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    OrderDBUtils.setNeedRequest(orderCode, requestType);
                                }
                            });
                        }
                        Logger.e(TAG, "post order code fail: " + error);
                    }
                }));
    }


    private void updateLogistics(JSONObject response, int index, int currentInitCount)
            throws JSONException {
        // length == 1
        JSONObject order = OrderItem.getOrderItemAt(response, 0);
        Logger.d(TAG, "postOrderCode success, initCount = " + currentInitCount);
        if (!executorService.isShutdown()) {
            executorService.execute(
                    new OrderInfoFilledTask(order,
                                            index,
                                            currentInitCount));
        }
    }




    public void postDeliveredOrder(int orderID, int eventID, final int position) {
        final int currentInitCount = initCount;
        MyApplication.addToRequestQueue(new PostDeliveredOrderRequest(orderID, eventID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Logger.d(TAG, "postDeliveredOrder() response: " + response);
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                Toaster.showShort(context, R.string.success_post);
                                updateLogistics(response, position, currentInitCount);
                                orderListUpdater.update(orderItems);
                            } else {
                                Logger.d(TAG, "postDeliveredOrder fail, response = " + response);
                                Toaster.showShort(context, R.string.error_fail_post_delivered);
                            }

                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString(), e);
                            Toaster.showShort(context, R.string.error_fail_post_delivered);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(TAG, error);
                        Toaster.showShort(context, R.string.error_fail_post_delivered);
                    }
                }));
    }







    // ****************************************************************
    // request definition
    // ****************************************************************


    /**
     * post order code
     */
    private class OrderItemInfoRequest extends AutoLoginRequest {

        private String orderCode;
        private int requestType;

        public OrderItemInfoRequest(String orderCode, int requestType,
                                    Response.Listener<JSONObject> listener,
                                    Response.ErrorListener errorListener) {
            super(context, Method.POST, SERVER_PATH, listener, errorListener);

            this.orderCode = orderCode;
            this.requestType = requestType;

            Logger.d(TAG, "OrderItemInfoRequest");
        }


        @Override
        protected void setParams(Map<String, String> params) {
            params.put("sys", "lgst");
            params.put("ctrl", "lgst_nor");

            switch (requestType) {
                case OrderItem.REQUEST_ALL_DATA:
                    int eventID;
                    switch (jcat_id) {
                        case 2:
                            eventID = 5;  break;
                        case 3:
                            eventID = 6;  break;
                        case 4:
                            eventID = 14; break;
                        default:
                            eventID = 3;  break;
                    }
                    params.put("event_id", "" + eventID);
                    params.put("order_code", orderCode);
                    params.put("action", "order_lgst");
                    break;
                case OrderItem.REQUEST_LOGISTICS_UPDATE:
                    params.put("order_codes", orderCode);
                    params.put("action", "orders_info");
                    break;
            }

        }
    }





    /**
     * used such as post sign in or sign out info
     */
    private class QRInfoRequest extends AutoLoginRequest {

        private static final String TAG = "QRInfoRequest";

        private JSONObject mJSONObject;

        public QRInfoRequest(JSONObject jsonObject,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener) {
            super(context, Method.POST, SERVER_PATH, listener, errorListener);

            mJSONObject = jsonObject;
        }

        @Override
        protected void setParams(Map<String, String> params) {
            try {
                Iterator<String> iterator = mJSONObject.keys();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    params.put(key, mJSONObject.getString(key));
                }

                Logger.d(TAG, params.toString());
            } catch (JSONException e) {
                Logger.e(TAG, e.toString(), e);
            }
        }
    }




    private class RetrieveAllScannedOrderRequest extends AutoLoginRequest {
        public RetrieveAllScannedOrderRequest(Response.Listener<JSONObject> listener,
                                              Response.ErrorListener errorListener) {
            super(context, Method.POST, SERVER_PATH, listener, errorListener);
        }

        @Override
        protected void setParams(Map<String, String> params) {
            params.put("limit", "100000");
            params.put("offset", "0");
            params.put("sys", "lgst");
            params.put("ctrl", "lgst_nor");
            params.put("action", "scaned_orders");
        }
    }




    /**
     * post deliverState order info
     */
    private class PostDeliveredOrderRequest extends AutoLoginRequest {

        private int orderID;
        private int eventID;

        public PostDeliveredOrderRequest(int orderID, int eventID, Response.Listener<JSONObject> listener,
                                         Response.ErrorListener errorListener) {
            super(context, Method.POST, SERVER_PATH, listener, errorListener);

            this.orderID = orderID;
            this.eventID = eventID;
        }


        @Override
        protected void setParams(Map<String, String> params) {
            params.put("order_id", "" + orderID);
            params.put("event_id", "" + eventID);
            Logger.d(TAG, "PostDeliveredOrderRequest params = " + params);

            params.put("sys", "lgst");
            params.put("ctrl", "lgst_nor");
            params.put("action", "order_lgst");
        }
    }







    /**
     * insert a order with orderCode (without other information)
     */
    private static class OrderCodeInsertTask implements Runnable {

        private String orderCode;

        public OrderCodeInsertTask(String orderCode) {
            this.orderCode = orderCode;
        }

        @Override
        public void run() {
            OrderDBUtils.insertOrderCode(orderCode, OrderItem.REQUEST_ALL_DATA);
        }
    }




    private class OrderInfoFilledTask implements Runnable  {

        private JSONObject orderJson;
        private int index;
        private int currentInitCount;

        public OrderInfoFilledTask(JSONObject orderJson, int index, int currentInitCount) {
            this.orderJson = orderJson;
            this.index = index;
            this.currentInitCount = currentInitCount;
        }

        @Override
        public void run() {
            try {
                final OrderItem orderItem = OrderItem.newOrderItem(orderJson);
                if (orderItem != null) {
                    updateOrderItem(index, orderItem);
                }
            } catch (JSONException e) {
                Logger.e(TAG, e.toString(), e);
                Logger.e(TAG, orderJson.toString());
            }
        }

        private void updateOrderItem(int index, OrderItem orderItem) {
            // update in-memory data
            Message message = handler.obtainMessage(
                    WHAT_UPDATE_ORDER_ITEM,
                    index,
                    currentInitCount,
                    orderItem);
            Logger.d(TAG, "message.arg2 = " + message.arg2);
            message.sendToTarget();

            updateOrderData(orderItem);
        }


    }

    private static void updateOrderData(OrderItem orderItem) {
        switch (OrderDBUtils.getOrderExistsState(orderItem.orderCode)) {
            case OrderDBUtils.ORDER_STATE_EXIST:
                OrderItem.updateLogistics(orderItem.goodsItems);
                OrderDBUtils.setNeedRequest(orderItem.orderCode, OrderItem.REQUEST_NO_NEED);
                break;
            case OrderDBUtils.ORDER_STATE_EXIST_BUT_NOT_DATA:
                OrderDBUtils.fillOrderData(orderItem);
                break;
        }

    }


    private class FillAllScannedOrderTask implements Runnable {

        private JSONObject mResponse;

        FillAllScannedOrderTask(JSONObject response) {
            mResponse = response;
        }

        @Override
        public void run() {
            try {
                JSONArray orders = mResponse.getJSONArray("scaned_orders");
                for (int i = 0, len = orders.length(); i < len; ++i) {
                    JSONObject order = orders.getJSONObject(i);
                    OrderItem orderItem = OrderItem.newOrderItem(order);
                    updateOrderData(orderItem);
                }
            } catch (JSONException e) {
                Logger.e(TAG, "Fail to fill (all) scanned orders", e);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    reloadData(false);
                }
            });
        }
    }







    private static class OrderUpdateHandler extends Handler {

        private WeakReference<OrderModel> orderModelRef;

        public OrderUpdateHandler(OrderModel orderModel) {
            orderModelRef = new WeakReference<>(orderModel);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_UPDATE_ORDER_ITEM) {
                OrderModel orderModel = orderModelRef.get();
                if (orderModel != null && msg.arg2 == orderModel.initCount) {
                    Logger.d(TAG, "handleMessage, msg.arg2 = " + msg.arg2);
                    // might out of index when quickly changing the updater
                    // handle in this way will be able  to remove the message

                    orderModel.orderItems.set(msg.arg1, (OrderItem) msg.obj);
                    orderModel.orderItems = orderModel.orderListUpdater.update(orderModel.orderItems);
                    orderModel.orderView.notifyDataSetChanged();
                }
            }
        }
    }
}
