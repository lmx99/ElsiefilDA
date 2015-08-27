package com.lifeisle.jekton.order.list.updater;

import com.lifeisle.jekton.order.list.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jekton
 * @version 0.01 7/30/2015
 */
public class PostFailUpdater extends OrderListUpdater {

    @Override
    public List<OrderItem> init() {
        List<OrderItem> allOrderItems = super.init();

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem orderItem : allOrderItems) {
            if (orderItem.isPostFail()) {
                orderItems.add(orderItem);
            }
        }

        return orderItems;
    }

    @Override
    public List<OrderItem> update(List<OrderItem> oldList) {
        return oldList;
    }
}
