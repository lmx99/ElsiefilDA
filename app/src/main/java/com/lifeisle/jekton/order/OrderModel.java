package com.lifeisle.jekton.order;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.OrderOperateActivity;
import com.lifeisle.jekton.bean.OrderItem;
import com.lifeisle.jekton.order.sorter.OrderSorter;
import com.lifeisle.jekton.order.updater.OrderListUpdater;
import com.lifeisle.jekton.ui.adapter.OrderListAdapter;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.OrderDBUtils;
import com.lifeisle.jekton.util.Preferences;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
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


    String [] testOrderCodes = {
            "1090024901000",
            "1090024901017",
            "1090024901024",
            "1090024901031",
            "1090024901048",
            "1090024901055",
            "1090024901062",
            "1090024901079",
            "1090024901086",
            "1090024901093",
            "1090024901109",
            "1090024901116",
            "1090024901123",
            "1090024901130",
            "1090024901147",
            "1090024901154",
            "1090024901161",
            "1090024901178",
            "1090024901185",
            "1090024901192",
            "1090024901208",
            "1090024901215",
            "1090024901222",
            "1090024901239",
            "1090024901246",
            "1090024901253",
            "1090024901260",
            "1090024901277",
            "1090024901284",
            "1090024901291",
            "1090024901307",
            "1090024901314",
            "1090024901321",
            "1090024901338",
            "1090024901345",
            "1090024901352",
            "1090024901369",
            "1090024901376",
            "1090024901383",
            "1090024901390",
            "1090024901406",
            "1090024901413",
            "1090024901420",
            "1090024901437",
            "1090024901444",
            "1090024901451",
            "1090024901468",
            "1090024901475",
            "1090024901482",
            "1090024901499",
            "1090024901505",
            "1090024901512",
            "1090024901529",
            "1090024901536",
            "1090024901543",
            "1090024901550",
            "1090024901567",
            "1090024901574",
            "1090024901581",
            "1090024901598",
            "1090024901604",
            "1090024901611",
            "1090024901628",
            "1090024901635",
            "1090024901642",
            "1090024901659",
            "1090024901666",
            "1090024901673",
            "1090024901680",
            "1090024901697",
            "1090024901703",
            "1090024901710",
            "1090024901727",
            "1090024901734",
            "1090024901741",
            "1090024901758",
            "1090024901765",
            "1090024901772",
            "1090024901789",
            "1090024901796",
            "1090024901802",
            "1090024901819",
            "1090024901826",
            "1090024901833",
            "1090024901840",
            "1090024901857",
            "1090024901864",
            "1090024901871",
            "1090024901888",
            "1090024901895",
            "1090024901901",
            "1090024901918",
            "1090024901925",
            "1090024901932",
            "1090024901949",
            "1090024901956",
            "1090024901963",
            "1090024901970",
            "1090024901987",
            "1090024901994",
            "1090024902007",
            "1090024902014",
            "1090024902021",
            "1090024902038",
            "1090024902045",
            "1090024902052",
            "1090024902069",
            "1090024902076",
            "1090024902083",
            "1090024902090",
            "1090024902106",
            "1090024902113",
            "1090024902120",
            "1090024902137",
    };



    public OrderModel(Context context) {
        this.context = context;
        orderView = (OrderView) context;

        orderItems = new ArrayList<>();

        executorService = Executors.newSingleThreadExecutor();

        handler = new OrderUpdateHandler(this);
    }


    public void reloadData(boolean showDialog) {
        init(orderListUpdater, showDialog);
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


    /**
     *
     * @param orderCode EAN-13 orderCode
     */
    public void addOrder(String orderCode) {
        if (jcat_id < 0) {
            Toaster.showShort(context, R.string.error_not_sign_in_job);
            return;
        }

        if (!orderCode.matches("\\d{15}")) {
            Toaster.showShort(context, R.string.error_order_code_invalid);
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



    public void signInAJob(String qrCode) {
        MyApplication.addToRequestQueue(new SignInJobsRequest(qrCode, true,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.d(TAG, "signInAJob() response: " + response);
                        try {
                            jcat_id = response.getInt("jcat_id");
                            Preferences.setJCatID(jcat_id);
                            for (String orderCode : testOrderCodes) {
                                addOrder(orderCode);
                            }
                            Toaster.showShort(context, R.string.success_sign_in_jobs);
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString());
                            Logger.e(TAG, "Sign In Jobs response: " + response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Logger.e(TAG, error);
                        Toaster.showShort(context, R.string.error_fail_sign_in_jobs);
                    }
                }));
    }


    public void signOutJob(String qrCode) {
        MyApplication.addToRequestQueue(new SignInJobsRequest(qrCode, true,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.d(TAG, "signOutAJob() response: " + response);
                        try {
                            if (response.getInt("status") == 0) {
                                Preferences.setJCatID(-1);
                                OrderDBUtils.clearOrders();
                                Toaster.showShort(context, R.string.success_sign_out_jobs);
                            }
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString(), e);
                            Logger.e(TAG, "Sign In Jobs response: " + response);
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




    private void postOrderCode(final String orderCode,final int index, final int requestType,
                               final int currentInitCount) {
        orderRequestCount++;
        MyApplication.addToRequestQueue(new OrderItemInfoRequest(orderCode, requestType,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Logger.d(TAG, "postOrderCode response: \n" + response);
                        try {
                            if (response.getInt("status") == 0) {
                                orderRequestCount--;
                                updateLogistics(response, index, currentInitCount);
                            } else {
                                Logger.d(TAG, "postOrderCode response: \n" + response);
                                Toaster.showShort(context, R.string.error_order_code_invalid);
                            }
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString());
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
                        Logger.e(TAG, error);
                    }
                }));
    }


    private void updateLogistics(JSONObject response, int index, int currentInitCount)
            throws JSONException {
        if (!executorService.isTerminated()) {
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
    }




    public void postDeliveredOrder(final int orderID, final int eventID) {

        MyApplication.addToRequestQueue(new PostDeliveredOrderRequest(orderID, eventID,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Logger.d(TAG, "postDeliveredOrder() response: " + response);
                        try {
                            int status = response.getInt("status");
                            if (status == 0) {
                                OrderDBUtils.setNeedRequest(orderID, OrderItem.REQUEST_LOGISTICS_UPDATE);
                                Toaster.showShort(context, R.string.success_post);
                                reloadData(false);
                            } else {
                                Logger.d(TAG, "postDeliveredOrder fail, response = " + response);
                                Toaster.showShort(context, R.string.error_fail_post_delivered);
                            }

                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString());
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
     * post sign in or sign out info
     */
    private class SignInJobsRequest extends AutoLoginRequest {

        private static final String TAG = "SignInJobsRequest";

        private String qrCode;
        private boolean mSignIn;

        public SignInJobsRequest(String qrCode, boolean signIn,
                                 Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener) {
            super(context, Method.POST, SERVER_PATH, listener, errorListener);

            this.qrCode = qrCode;
            mSignIn = signIn;
        }

        @Override
        protected void setParams(Map<String, String> params) {
            try {
                JSONObject json = new JSONObject(qrCode);
                int region_id = json.getInt("region_id");
                params.put("region_id", "" + region_id);
                params.put("sys", "job");
                params.put("ctrl", "job_epl");
                params.put("action", mSignIn? "sign_in" : "sign_out");
            } catch (JSONException e) {
                Logger.e(TAG, e.toString(), e);
            }
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
                Logger.e(TAG, e.toString());
                Logger.e(TAG, orderJson.toString());
            }
        }

        private void updateOrderItem(int index, OrderItem orderItem) {
            switch (OrderDBUtils.getOrderExistsState(orderItem.orderCode)) {
                case OrderDBUtils.ORDER_STATE_EXIST:
                    OrderItem.updateLogistics(orderItem.goodsItems);
                    OrderDBUtils.setNeedRequest(orderItem.orderCode, OrderItem.REQUEST_NO_NEED);
                    break;
                case OrderDBUtils.ORDER_STATE_EXIST_BUT_NOT_DATA:
                    OrderDBUtils.fillOrderData(orderItem);
                    break;
            }


            Message message = handler.obtainMessage(
                    WHAT_UPDATE_ORDER_ITEM,
                    index,
                    currentInitCount,
                    orderItem);
            Logger.d(TAG, "message.arg2 = " + message.arg2);
            message.sendToTarget();

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
