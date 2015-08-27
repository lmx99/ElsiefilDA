package com.lifeisle.jekton.order.list.sorter;

import com.lifeisle.jekton.order.list.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Jekton
 * @version 0.01 7/28/2015
 */
public class DescendingCreateTimeSorter implements OrderSorter {

    @Override
    public List<OrderItem> sort(List<OrderItem> oldList) {
        List<OrderItem> newList = new ArrayList<>(oldList);
        Collections.sort(newList, new Comparator<OrderItem>() {
            @Override
            public int compare(OrderItem lhs, OrderItem rhs) {
                return -lhs.createTime.compareTo(rhs.createTime);
            }
        });

        return newList;
    }
}
