package com.lifeisle.jekton.order.list.sorter;

import com.lifeisle.jekton.bean.OrderItem;

import java.util.List;

/**
 * @author Jekton
 * @version 0.01 7/27/2015
 */
public interface OrderSorter {
    List<OrderItem> sort(List<OrderItem> oldList);
}
