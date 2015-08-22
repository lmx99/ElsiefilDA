package com.lifeisle.jekton.order.updater;

import com.lifeisle.jekton.bean.OrderItem;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Jekton
 * @version 0.01 7/28/2015
 */
public class UndeliveredOrderUpdater extends OrderListUpdater {

    @Override
    public List<OrderItem> init() {
        List<OrderItem> allOrderItems = super.init();

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem item : allOrderItems) {
            if (!item.isPostFail() && !item.isDelivered() && !item.isAbnormal()) {
                orderItems.add(item);
            }
        }

        return orderItems;
    }

    @Override
    public List<OrderItem> update(List<OrderItem> oldList) {
        ListIterator<OrderItem> iterator = oldList.listIterator();
        while (iterator.hasNext()) {
            OrderItem item = iterator.next();
            if (item.isDelivered()) {
                iterator.remove();
                break;
            }
        }

        return oldList;
    }
}
