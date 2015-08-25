package com.lifeisle.jekton.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.OrderItem;
import com.lifeisle.jekton.order.list.OrdersDBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Jekton
 * @version 0.2 7/16/2015
 */
public class OrderDBUtils {

    public static final int ORDER_STATE_NOT_EXIST = 0;
    public static final int ORDER_STATE_EXIST = 1;
    public static final int ORDER_STATE_EXIST_BUT_NOT_DATA = 2;

    private static final String TAG = "OrderDBUtils";
    private static SQLiteDatabase ordersDB;

    private OrderDBUtils() {
        throw new AssertionError("Can not instantiate this class.");
    }

    static {
        OrdersDBHelper dbHelper = new OrdersDBHelper(MyApplication.getInstance());
        try {
            ordersDB = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            ordersDB = dbHelper.getReadableDatabase();
        }
    }




    /**
     * @param orderCode barcode of a order
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public static long insertOrderCode(String orderCode, int requestType) {
        ContentValues values = new ContentValues();
        values.put(OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE, orderCode);
        values.put(OrdersDBHelper.COLUMN_ORDERS_REQUEST_TYPE, requestType);

        return ordersDB.insert(OrdersDBHelper.TABLE_ORDERS, null, values);
    }

    public static void deleteOrderCode(String orderCode) {
        String where = OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE + "='" + orderCode + "'";
        ordersDB.delete(OrdersDBHelper.TABLE_ORDERS, where, null);
    }


    /**
     * fill a row of the database of a order
     * @param item A orderItem that holds data of a order to be filled
     */
    public static void fillOrderData(OrderItem item) {
        Logger.d(TAG, "fillOrderData");
        ContentValues order = new ContentValues();

        order.put(OrdersDBHelper.COLUMN_ORDERS_ORDER_ID, item.orderID);
        order.put(OrdersDBHelper.COLUMN_ORDERS_ORDER_NUMBER, item.orderNumber);
        long createTime = item.createTime.getTime();
        order.put(OrdersDBHelper.COLUMN_ORDERS_CREATE_TIME, createTime);
        long addTime = item.addTime.getTime();
        order.put(OrdersDBHelper.COLUMN_ORDERS_ADD_TIME, addTime);
//        Logger.d(TAG, "createTime = " + createTime + ", addTime = " + addTime);
        order.put(OrdersDBHelper.COLUMN_ORDERS_PRICE, item.price);
        order.put(OrdersDBHelper.COLUMN_ORDERS_SUBTOTAL, item.subTotal);
        order.put(OrdersDBHelper.COLUMN_ORDERS_MOBILE_PHONE, item.mobilePhone);
        order.put(OrdersDBHelper.COLUMN_ORDERS_ADDRESS, item.address);
        order.put(OrdersDBHelper.COLUMN_ORDERS_REMARKS, item.remarks);
        order.put(OrdersDBHelper.COLUMN_ORDERS_SOURCE, item.source);
        order.put(OrdersDBHelper.COLUMN_ORDERS_RESTAURANT_ID, item.restaurantID);
        order.put(OrdersDBHelper.COLUMN_ORDERS_REQUEST_TYPE, item.requestType);

        int update = ordersDB.update(OrdersDBHelper.TABLE_ORDERS, order,
                OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE + "='" + item.orderCode + "'", null);
        if (update <= 0) {
            Logger.e(TAG, StringUtils.getStringFromResource(R.string.error_fail_to_update_order) + item.orderCode);

        }
        insertGoodsItemInfo(item);
    }

    private static void insertGoodsItemInfo(OrderItem item) {
        for (int i = 0; i < item.goodsItems.length; i++) {
            ContentValues goods = new ContentValues();
            OrderItem.GoodsItem goodsItem = item.goodsItems[i];

            goods.put(OrdersDBHelper.COLUMN_GOODS_ITEM_ID, goodsItem.itemID);
            goods.put(OrdersDBHelper.COLUMN_GOODS_ORDER_ID, item.orderID);
            goods.put(OrdersDBHelper.COLUMN_GOODS_ORDER_CODE, item.orderCode);
            goods.put(OrdersDBHelper.COLUMN_GOODS_RESTAURANT, goodsItem.restaurant);
            goods.put(OrdersDBHelper.COLUMN_GOODS_COURSE_NAME, goodsItem.courseName);
            goods.put(OrdersDBHelper.COLUMN_GOODS_PRICE, goodsItem.price);
            goods.put(OrdersDBHelper.COLUMN_GOODS_QUANTITY, goodsItem.quantity);
            goods.put(OrdersDBHelper.COLUMN_GOODS_TOTAL_PRICE, goodsItem.totalPrice);

            if (ordersDB.insert(OrdersDBHelper.TABLE_GOODS, null, goods) < 0) {
                Logger.e(TAG, R.string.error_fail_to_insert_goods);
            }

            insertLogisticsInfo(goodsItem);
        }
    }

    private static void insertLogisticsInfo(OrderItem.GoodsItem item) {
        for (OrderItem.Logistics logistics : item.logistics) {
            insertLogistics(item.itemID, logistics);
        }
    }



    public static void insertLogistics(int itemID, OrderItem.Logistics logistics) {
        if (isLogisticsExists(logistics.stageID)) return;

        ContentValues values = new ContentValues();

        values.put(OrdersDBHelper.COLUMN_LOGISTICS_ITEM_ID, itemID);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_STAGE_ID, logistics.stageID);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_DLV_NAME, logistics.dlvName);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_DLV_TIME, logistics.dlvTime);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_EVENT_ID, logistics.eventID);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_EVENT_DESC, logistics.eventDesc);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_MOBILE_PHONE, logistics.mobilePhone);
        values.put(OrdersDBHelper.COLUMN_LOGISTICS_REAL_NAME, logistics.deliverName);

        if (ordersDB.insert(OrdersDBHelper.TABLE_LOGISTICS, null, values) < 0) {
            Logger.e(TAG, "fail to insert logistics info, values = " + values);
        }

    }

    private static boolean isLogisticsExists(int stageID) {
        String[] columns = { OrdersDBHelper.COLUMN_LOGISTICS_STAGE_ID };
        String selection = OrdersDBHelper.COLUMN_LOGISTICS_STAGE_ID + "=" + stageID;

        Cursor cursor = ordersDB.query(OrdersDBHelper.TABLE_LOGISTICS, columns, selection,
                null, null, null, null);

        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }





    public static OrderItem getOrderItem(String orderCode) {
        OrderItem orderItem = new OrderItem();

        String[] orderColumns = {
                OrdersDBHelper.COLUMN_ORDERS_ID,
                OrdersDBHelper.COLUMN_ORDERS_ORDER_ID,
                OrdersDBHelper.COLUMN_ORDERS_ORDER_NUMBER,
                OrdersDBHelper.COLUMN_ORDERS_CREATE_TIME,
                OrdersDBHelper.COLUMN_ORDERS_ADD_TIME,
                OrdersDBHelper.COLUMN_ORDERS_PRICE,
                OrdersDBHelper.COLUMN_ORDERS_SUBTOTAL,
                OrdersDBHelper.COLUMN_ORDERS_MOBILE_PHONE,
                OrdersDBHelper.COLUMN_ORDERS_ADDRESS,
                OrdersDBHelper.COLUMN_ORDERS_REMARKS,
                OrdersDBHelper.COLUMN_ORDERS_SOURCE,
                OrdersDBHelper.COLUMN_ORDERS_RESTAURANT_ID,
                OrdersDBHelper.COLUMN_ORDERS_REQUEST_TYPE,
        };
        String orderSelection = OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE + "='" + orderCode + "'";
        Cursor orderCursor = ordersDB.query(OrdersDBHelper.TABLE_ORDERS, orderColumns, orderSelection,
                null, null, null, null);
        fillOrderItem(orderItem, orderCursor);
        orderItem.orderCode = orderCode;
        orderCursor.close();

        String[] goodsColumns = {
                OrdersDBHelper.COLUMN_GOODS_ID,
                OrdersDBHelper.COLUMN_GOODS_ITEM_ID,
                OrdersDBHelper.COLUMN_GOODS_RESTAURANT,
                OrdersDBHelper.COLUMN_GOODS_COURSE_NAME,
                OrdersDBHelper.COLUMN_GOODS_PRICE,
                OrdersDBHelper.COLUMN_GOODS_QUANTITY,
                OrdersDBHelper.COLUMN_GOODS_TOTAL_PRICE,
        };
        String goodsSelection = OrdersDBHelper.COLUMN_GOODS_ORDER_ID + "=" + orderItem.orderID;
        Cursor goodsCursor = ordersDB.query(OrdersDBHelper.TABLE_GOODS, goodsColumns, goodsSelection,
                null, null, null, null);
        fillGoodsItems(orderItem, goodsCursor);
        goodsCursor.close();


        String[] logisticsColumns = {
                OrdersDBHelper.COLUMN_LOGISTICS_STAGE_ID,
                OrdersDBHelper.COLUMN_LOGISTICS_DLV_NAME,
                OrdersDBHelper.COLUMN_LOGISTICS_DLV_TIME,
                OrdersDBHelper.COLUMN_LOGISTICS_EVENT_ID,
                OrdersDBHelper.COLUMN_LOGISTICS_EVENT_DESC,
                OrdersDBHelper.COLUMN_LOGISTICS_MOBILE_PHONE,
                OrdersDBHelper.COLUMN_LOGISTICS_REAL_NAME,
        };
        for (OrderItem.GoodsItem goodsItem : orderItem.goodsItems) {
            String logisticsSelection = OrdersDBHelper.COLUMN_LOGISTICS_ITEM_ID + "=" + goodsItem.itemID;
            Cursor logisticsCursor = ordersDB.query(OrdersDBHelper.TABLE_LOGISTICS, logisticsColumns,
                    logisticsSelection, null, null, null, OrdersDBHelper.COLUMN_LOGISTICS_DLV_TIME);
            fillLogistics(goodsItem, logisticsCursor);

            logisticsCursor.close();
        }

        return orderItem;
    }

    private static void fillOrderItem(OrderItem orderItem, Cursor cursor) {

        if (cursor.moveToNext()) {
            int orderID = cursor.getInt(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_ORDER_ID));
            int orderNumber = cursor.getInt(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_ORDER_NUMBER));
            long milliCreateTime = cursor.getLong(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_CREATE_TIME));
            Date createTime = new Date(milliCreateTime);
            long milliAddTime = cursor.getLong(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_ADD_TIME));
            Date addTime = new Date(milliAddTime);
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_PRICE));
            double subTotal = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_SUBTOTAL));
            String mobilePhone = cursor.getString(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_MOBILE_PHONE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_ADDRESS));
            String remarks = cursor.getString(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_REMARKS));
            String source = cursor.getString(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_SOURCE));
            int restaurantID = cursor.getInt(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_RESTAURANT_ID));
            int requestType = cursor.getInt(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_REQUEST_TYPE));

            orderItem.orderID = orderID;
            orderItem.orderNumber = orderNumber;
            orderItem.createTime = createTime;
            orderItem.addTime = addTime;
            orderItem.price = price;
            orderItem.subTotal = subTotal;
            orderItem.mobilePhone = mobilePhone;
            orderItem.address = address;
            orderItem.remarks = remarks;
            orderItem.source = source;
            orderItem.restaurantID = restaurantID;
            orderItem.requestType = requestType;
        }
    }

    private static void fillGoodsItems(OrderItem orderItem, Cursor cursor) {
        int count = cursor.getCount();
        orderItem.goodsItems = new OrderItem.GoodsItem[count];
        if (count == 0) return;

        int i = 0;
        while (cursor.moveToNext()) {
            int itemID = cursor.getInt(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_GOODS_ITEM_ID));
            String restaurant = cursor.getString(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_GOODS_RESTAURANT));
            String courseName = cursor.getString(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_GOODS_COURSE_NAME));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_GOODS_PRICE));
            double totalPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_GOODS_TOTAL_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_GOODS_QUANTITY));

            OrderItem.GoodsItem goodsItem = new OrderItem.GoodsItem();
            goodsItem.itemID = itemID;
            goodsItem.restaurant = restaurant;
            goodsItem.courseName = courseName;
            goodsItem.price = price;
            goodsItem.totalPrice = totalPrice;
            goodsItem.quantity = quantity;

            orderItem.goodsItems[i++] = goodsItem;
        }
    }

    private static void fillLogistics(OrderItem.GoodsItem goodsItem, Cursor cursor) {
        goodsItem.logistics = new OrderItem.Logistics[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            int stateID = cursor.getInt(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_STAGE_ID));
            int eventID = cursor.getInt(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_EVENT_ID));
            String eventDesc = cursor.getString(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_EVENT_DESC));
            String deliverName = cursor.getString(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_REAL_NAME));
            String dlvName = cursor.getString(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_DLV_NAME));
            String dlvTime = cursor.getString(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_DLV_TIME));
            String mobilePhone = cursor.getString(
                    cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_LOGISTICS_MOBILE_PHONE));

            OrderItem.Logistics logistics = new OrderItem.Logistics();
            logistics.stageID = stateID;
            logistics.eventID = eventID;
            logistics.eventDesc = eventDesc;
            logistics.deliverName = deliverName;
            logistics.dlvName = dlvName;
            logistics.dlvTime = dlvTime;
            logistics.mobilePhone = mobilePhone;

            goodsItem.logistics[i++] = logistics;
        }
    }



    public static int setNeedRequest(String orderCode, int requestType) {
        ContentValues values = new ContentValues();
        values.put(OrdersDBHelper.COLUMN_ORDERS_REQUEST_TYPE, requestType);

        String where = OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE + "='" + orderCode + "'";
        return ordersDB.update(OrdersDBHelper.TABLE_ORDERS, values, where, null);
    }

    public static int setNeedRequest(int orderId, int requestType) {
        ContentValues values = new ContentValues();
        values.put(OrdersDBHelper.COLUMN_ORDERS_REQUEST_TYPE, requestType);

        String where = OrdersDBHelper.COLUMN_ORDERS_ORDER_ID + "=" + orderId;
        return ordersDB.update(OrdersDBHelper.TABLE_ORDERS, values, where, null);
    }


    public static List<String> getAllOrderCodes() {

        String[] columns = {
            OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE,
        };

        Cursor cursor = ordersDB.query(OrdersDBHelper.TABLE_ORDERS, columns, null, null, null, null, null);

        List<String> orderCodes = new ArrayList<>();
        int columnIndex = cursor.getColumnIndexOrThrow(OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE);
        while (cursor.moveToNext()) {
            String orderCode = cursor.getString(columnIndex);
            orderCodes.add(orderCode);
        }
        cursor.close();

        return orderCodes;
    }





    public static List<OrderItem> getOrderItems(List<String> orderCodes) {
        List<OrderItem> orderItems = new ArrayList<>(orderCodes.size());
        for (String orderCode : orderCodes) {
            orderItems.add(getOrderItem(orderCode));
        }

        return orderItems;
    }



    public static int getOrderExistsState(String orderCode) {
        String[] columns = { OrdersDBHelper.COLUMN_GOODS_ITEM_ID };
        String selection = OrdersDBHelper.COLUMN_GOODS_ORDER_CODE + "='" + orderCode + "'";
        Cursor cursor = ordersDB.query(OrdersDBHelper.TABLE_GOODS, columns, selection,
                null, null, null, null);

        boolean result = cursor.getCount() > 0;
        cursor.close();

        if (result) return ORDER_STATE_EXIST;

        return isOrderCodeExists(orderCode) ? ORDER_STATE_EXIST_BUT_NOT_DATA : ORDER_STATE_NOT_EXIST;

    }

    public static boolean isOrderCodeExists(String orderCode) {
        String[] columns = { OrdersDBHelper.COLUMN_ORDERS_ORDER_ID };
        String selection = OrdersDBHelper.COLUMN_ORDERS_ORDER_CODE + "='" + orderCode + "'";
        Logger.d(TAG, "isOrderCodeExists() selection = " + selection);
        Cursor cursor = ordersDB.query(OrdersDBHelper.TABLE_ORDERS, columns, selection,
                null, null, null, null);

        boolean result = cursor.getCount() > 0;
        cursor.close();

        return result;
    }


    public static void clearOrders() {
        ordersDB.execSQL("delete from " + OrdersDBHelper.TABLE_ORDERS);
        ordersDB.execSQL("delete from " + OrdersDBHelper.TABLE_GOODS);
        ordersDB.execSQL("delete from " + OrdersDBHelper.TABLE_LOGISTICS);
    }
}
