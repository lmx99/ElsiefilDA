package com.lifeisle.jekton.order;

import com.lifeisle.jekton.order.sorter.AscendingCreateTimeSorter;
import com.lifeisle.jekton.order.sorter.DescendingCreateTimeSorter;
import com.lifeisle.jekton.order.updater.AbnormalOrderUpdater;
import com.lifeisle.jekton.order.updater.AllOrderUpdater;
import com.lifeisle.jekton.order.updater.DeliveredOrderUpdater;
import com.lifeisle.jekton.order.updater.PostFailUpdater;
import com.lifeisle.jekton.order.updater.UndeliveredOrderUpdater;

/**
 * @author Jekton
 * @version 0.2 8/5/2015
 */
public class OrderController {

    public static final int OPTION_ORDER_ALL = 0;
    public static final int OPTION_ORDER_UNDELIVERED = 1;
    public static final int OPTION_ORDER_DELIVERED = 2;
    public static final int OPTION_ORDER_ABNORMAL = 3;
    public static final int OPTION_ORDER_POST_FAIL = 4;

    public static final int OPTION_SORT_BY_CREATE_TIME = 0;



    protected OrderModel orderModel;

    public OrderController(OrderModel orderModel) {
        this.orderModel = orderModel;
    }


    public void notifyDataSetChanged(boolean showDialog) {
        orderModel.reloadData(showDialog);
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



    public void postQRCode(String qrCode) {
        orderModel.signInAJob(qrCode);
    }

    public boolean addOrderCode(String orderCode) {
        return orderModel.addOrder(orderCode);
    }

    public void postDeliveredOrder(int orderID, int eventID) {
        orderModel.postDeliveredOrder(orderID, eventID);
    }
}
