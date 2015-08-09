package com.lifeisle.jekton.order.updater;

import com.lifeisle.jekton.bean.OrderItem;
import com.lifeisle.jekton.util.OrderDBUtils;

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
