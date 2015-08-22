package com.lifeisle.jekton.order;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.sorter.AscendingCreateTimeSorter;
import com.lifeisle.jekton.order.sorter.DescendingCreateTimeSorter;
import com.lifeisle.jekton.order.updater.AbnormalOrderUpdater;
import com.lifeisle.jekton.order.updater.AllOrderUpdater;
import com.lifeisle.jekton.order.updater.DeliveredOrderUpdater;
import com.lifeisle.jekton.order.updater.PostFailUpdater;
import com.lifeisle.jekton.order.updater.UndeliveredOrderUpdater;
import com.lifeisle.jekton.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 * @version 0.3 8/10/2015
 */
public class OrderController {

    public static final int OPTION_ORDER_ALL = 0;
    public static final int OPTION_ORDER_UNDELIVERED = 1;
    public static final int OPTION_ORDER_DELIVERED = 2;
    public static final int OPTION_ORDER_ABNORMAL = 3;
    public static final int OPTION_ORDER_POST_FAIL = 4;

    public static final int OPTION_SORT_BY_CREATE_TIME = 0;

    private static final int TYPE_QR_CODE_SING_IN = 0;
    private static final int TYPE_QR_CODE_SING_OUT = 1;
    private static final int TYPE_QR_CODE_ENTER_JOB = 2;


    private static final String LOG_TAG = "OrderController";


    private OrderView mOrderView;
    protected OrderModel orderModel;

    public OrderController(OrderView orderView, OrderModel orderModel) {
        mOrderView = orderView;
        this.orderModel = orderModel;
    }


    public void notifyDataSetChanged(boolean showDialog) {
        if (showDialog) {
            orderModel.reloadData(showDialog);
        } else {  // pull down
            orderModel.retrieveAllScannedData();
        }
    }


    public void setOptions(int orderOption, int sortOption, boolean ascending) {
        setOrderOption(orderOption);
        setSortOption(sortOption, ascending);
        orderModel.reloadData(true);
    }

    private void setOrderOption(int orderOption) {
        switch (orderOption) {
            case OPTION_ORDER_ALL:
                orderModel.setOrderListUpdater(new AllOrderUpdater());
                break;
            case OPTION_ORDER_UNDELIVERED:
                orderModel.setOrderListUpdater(new UndeliveredOrderUpdater());
                break;
            case OPTION_ORDER_DELIVERED:
                orderModel.setOrderListUpdater(new DeliveredOrderUpdater());
                break;
            case OPTION_ORDER_ABNORMAL:
                orderModel.setOrderListUpdater(new AbnormalOrderUpdater());
                break;
            case OPTION_ORDER_POST_FAIL:
                orderModel.setOrderListUpdater(new PostFailUpdater());
                break;
        }
    }

    private void setSortOption(int sortOption, boolean ascending) {
        switch (sortOption) {
            case OPTION_SORT_BY_CREATE_TIME:
                if (ascending)
                    orderModel.setOrderSorter(new AscendingCreateTimeSorter());
                else
                    orderModel.setOrderSorter(new DescendingCreateTimeSorter());
                break;
        }
    }



    public void postQRCode(String data) {
        if (data.startsWith("{")) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                switch (jsonObject.getInt("type")) {
                    case TYPE_QR_CODE_SING_IN:
                        orderModel.signInAJob(jsonObject);
                        break;
                    case TYPE_QR_CODE_SING_OUT:
                        orderModel.signOutJob(jsonObject);
                        break;
                    case TYPE_QR_CODE_ENTER_JOB:
                        orderModel.enterJob(jsonObject);
                        break;
                    default:
                        mOrderView.showErrMsg(R.string.error_qr_code_invalid);
                        Logger.e(LOG_TAG, "unexpected qr code, data = " + data);
                        break;
                }
            } catch (JSONException e) {
                mOrderView.showErrMsg(R.string.error_qr_code_invalid);
                Logger.e(LOG_TAG, "unexpected qr code, data = " + data, e);
            }
        } else {
            orderModel.addOrder(data);
        }
    }





    public void postDeliveredOrder(int orderID, int eventID, int position) {
        orderModel.postDeliveredOrder(orderID, eventID, position);
    }
}
