package com.lifeisle.jekton.bean;

import com.lifeisle.jekton.order.EventIDMapper;
import com.lifeisle.jekton.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Jekton
 * @version 0.03 7/31/2015
 */
public class OrderItem {

    public static final int REQUEST_NO_NEED = 0;
    public static final int REQUEST_ALL_DATA = 1;
    public static final int REQUEST_LOGISTICS_UPDATE = 2;


    private static final String TAG = "OrderItem";
    private static final Date DEFAULT_CREATE_TIME = new Date(10000);

    public OrderItem() {
    }

    public OrderItem(String orderCode) {
        this.orderCode = orderCode;
    }

    public int orderID = -1;
    public int orderNumber = 0;
    public Date createTime = DEFAULT_CREATE_TIME;
    public Date addTime = createTime;
    public double price = 0.0;
    public double subTotal = 0.0;
    public String mobilePhone = "";
    public String address = "";
    public String remarks = "";
    public String source = "";
    public int restaurantID = -1;
    public String orderCode;
    public GoodsItem[] goodsItems;

    public int requestType = 0;

    private String orderInfo;
    private String details;


    public static class GoodsItem {
        public int itemID;
        public String restaurant;
        public String courseName;
        public double price;
        public double totalPrice;
        public int quantity;

        public Logistics[] logistics;
    }

    public static class Logistics {
        public int stageID;
        public int eventID;
        public String eventDesc;
        public String deliverName;
        public String dlvName;     // of deliver
        public String dlvTime;
        public String mobilePhone;
    }




    public String getOrderInfo() {
        if (orderInfo == null) {
            orderInfo = orderCode + "\n" + address;
        }

        return orderInfo;
    }


    public String getDetails() {
        if (details == null) {
            if (goodsItems == null) return "";

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < goodsItems.length; i++) {
                GoodsItem item = goodsItems[i];
                builder.append(i + 1);
                builder.append(". ");
                builder.append(item.courseName);

                if (item.logistics.length > 0) {
                    Logistics lastLogistics = item.logistics[item.logistics.length - 1];
                    builder.append("\n   【");
                    builder.append(lastLogistics.deliverName);
                    builder.append(" ");
                    builder.append(lastLogistics.mobilePhone);
                    builder.append("】");
                    builder.append(lastLogistics.eventDesc);
                    builder.append("\n");
                }

            }
            details = builder.toString();
        }
        return details;
    }


    public boolean isDelivered() {
        if (isPostFail()) return false;

        for (OrderItem.GoodsItem goodsItem : goodsItems) {
            int length = goodsItem.logistics.length;
            if (length == 0) return false;
            Logistics lastLogistics = goodsItem.logistics[length - 1];
            if (lastLogistics.eventID != EventIDMapper.EVENT_DELIVERED
                    && lastLogistics.eventID != EventIDMapper.EVENT_RECEIVED_BY_AGENT) {
                return false;
            }
        }

        return true;
    }


    public boolean isAbnormal() {
        if (isPostFail()) return false;

        for (OrderItem.GoodsItem goodsItem : goodsItems) {
            int length = goodsItem.logistics.length;
            if (length == 0) return false;
            Logistics lastLogistics = goodsItem.logistics[length - 1];
            if (lastLogistics.eventID != EventIDMapper.EVENT_OTHER
                    && lastLogistics.eventID != EventIDMapper.EVENT_SUPPLEMENT
                    && lastLogistics.eventID != EventIDMapper.EVENT_RETURN_GOODS
                    && lastLogistics.eventID != EventIDMapper.EVENT_EXCHANGE) {
                return false;
            }
        }

        return true;
    }


    public boolean isPostFail() {
        return goodsItems == null || goodsItems.length == 0;
    }



    public static OrderItem newOrderItem(JSONObject order) throws JSONException {
        OrderItem item = new OrderItem();

        item.orderID = order.getInt("order_id");
        item.orderNumber = order.getInt("order_number");
        item.orderCode = order.getString("order_code");
        item.price = order.getDouble("price");
        item.subTotal = order.getDouble("subtotal");
        item.mobilePhone = order.getString("mobile_phone");
        item.address = order.getString("address");
        item.remarks = order.getString("remarks");
        item.source = order.getString("source");
        item.createTime = new Date(order.getLong("create_time") * 1000);
        item.addTime = new Date(order.getLong("add_time") * 1000);
        item.restaurantID = order.getInt("user_id");

        JSONArray items = order.getJSONArray("order_items");
        item.goodsItems = new OrderItem.GoodsItem[items.length()];
        for (int i = 0, len = items.length(); i < len; ++i) {
            item.goodsItems[i] = newGoodsItem(items.getJSONObject(i));
        }

        item.requestType = REQUEST_NO_NEED;
        return item;
    }



    public static GoodsItem newGoodsItem(JSONObject goodsJSON) throws JSONException {
        OrderItem.GoodsItem goodsItem = new OrderItem.GoodsItem();

        goodsItem.itemID = goodsJSON.getInt("item_id");
        goodsItem.restaurant = goodsJSON.getString("hotel_name");
        goodsItem.courseName = goodsJSON.getString("dish_name");
        goodsItem.price = goodsJSON.getDouble("price");
        goodsItem.quantity = goodsJSON.getInt("quantity");
        goodsItem.totalPrice = goodsJSON.getDouble("total_price");

        JSONArray logisticsArray = goodsJSON.getJSONArray("logistics");
        int logisticsLen = logisticsArray.length();
        goodsItem.logistics = new OrderItem.Logistics[logisticsLen];
        for (int j = 0; j < logisticsLen; ++j) {
            goodsItem.logistics[j] = newLogistics(logisticsArray.getJSONObject(j));
        }
        Arrays.sort(goodsItem.logistics, new Comparator<Logistics>() {
            @Override
            public int compare(Logistics lhs, Logistics rhs) {
                return lhs.dlvTime.compareTo(rhs.dlvTime);
            }
        });

        return goodsItem;
    }



    public static Logistics newLogistics(JSONObject logisticsJSON) throws JSONException {
        OrderItem.Logistics logistics = new OrderItem.Logistics();

        logistics.stageID = logisticsJSON.getInt("stage_id");
        logistics.eventID = logisticsJSON.getInt("event_id");
        logistics.eventDesc = logisticsJSON.getString("event_desc");
        logistics.deliverName = logisticsJSON.getString("real_name");
        logistics.dlvName = logisticsJSON.getString("dlv_name");
        logistics.dlvTime = logisticsJSON.getString("dlv_time");
        logistics.mobilePhone = logisticsJSON.getString("mobile_phone");

        return logistics;
    }

}
