package com.lifeisle.jekton.order.list.updater;

import com.lifeisle.jekton.order.list.OrderItem;

import java.util.List;

/**
 * @author Jekton
 * @version 0.01 7/27/2015
 */
public class AllOrderUpdater extends OrderListUpdater {

    @Override
    public List<OrderItem> update(List<OrderItem> oldList) {
        return oldList;
    }
}
