package com.lifeisle.jekton.order.list.updater;

import com.lifeisle.jekton.order.OrderDBUtils;
import com.lifeisle.jekton.order.OrderItem;

import java.util.List;

/**
 * @author Jekton
 * @version 0.01 7/27/2015
 */
public abstract class OrderListUpdater {


    public List<OrderItem> init() {
        List<String> allOrderCodes = OrderDBUtils.getAllOrderCodes();
        return OrderDBUtils.getOrderItems(allOrderCodes);
    }


    public abstract List<OrderItem> update(List<OrderItem> oldList);

}
