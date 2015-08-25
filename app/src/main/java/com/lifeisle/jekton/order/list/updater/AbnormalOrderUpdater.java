package com.lifeisle.jekton.order.list.updater;

import com.lifeisle.jekton.bean.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jekton
 * @version 0.01 7/29/2015
 */
public class AbnormalOrderUpdater extends OrderListUpdater {

    @Override
    public List<OrderItem> init() {
        List<OrderItem> allOrderItems = super.init();

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem item : allOrderItems) {
            if (item.isAbnormal()) {
                orderItems.add(item);
            }
        }

        return orderItems;
    }
    @Override
    public List<OrderItem> update(List<OrderItem> oldList) {
        return oldList;
    }
}
