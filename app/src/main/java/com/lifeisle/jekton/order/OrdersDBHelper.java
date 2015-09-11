package com.lifeisle.jekton.order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lifeisle.jekton.util.Logger;

/**
 * @author Jekton
 * @version 0.01 7/16/2015
 */
public class OrdersDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_ORDERS = "orders";

    public static final String TABLE_GOODS = "goods";
    public static final String TABLE_LOGISTICS = "logistics";
    public static final String INDEX_ORDERS = "orders_index";

    public static final String INDEX_GOODS = "goods_index";
    public static final String COLUMN_ORDERS_ID = "_id";

    public static final String COLUMN_ORDERS_ORDER_ID = "order_id";
    public static final String COLUMN_ORDERS_ORDER_NUMBER = "order_number";
    public static final String COLUMN_ORDERS_CREATE_TIME = "create_time";
    public static final String COLUMN_ORDERS_ADD_TIME = "add_time";
    public static final String COLUMN_ORDERS_PRICE = "price";
    public static final String COLUMN_ORDERS_SUBTOTAL = "subtotal";
    public static final String COLUMN_ORDERS_MOBILE_PHONE = "mobile_phone";
    public static final String COLUMN_ORDERS_ADDRESS = "address";
    public static final String COLUMN_ORDERS_REMARKS = "remarks";
    public static final String COLUMN_ORDERS_SOURCE = "source";
    public static final String COLUMN_ORDERS_ORDER_CODE = "order_code";
    public static final String COLUMN_ORDERS_RESTAURANT_ID = "restaurant_id";
    public static final String COLUMN_ORDERS_REQUEST_TYPE = "request_type";
    public static final String COLUMN_GOODS_ID = "_id";


    public static final String COLUMN_GOODS_ORDER_ID = "order_id";
    public static final String COLUMN_GOODS_ITEM_ID = "item_id";
    public static final String COLUMN_GOODS_ORDER_CODE = "order_code";
    public static final String COLUMN_GOODS_RESTAURANT = "restaurant";
    public static final String COLUMN_GOODS_COURSE_NAME = "course_name";
    public static final String COLUMN_GOODS_PRICE = "price";
    public static final String COLUMN_GOODS_QUANTITY = "quantity";
    public static final String COLUMN_GOODS_TOTAL_PRICE = "total_price";
    public static final String COLUMN_LOGISTICS_ID = "_id";


    public static final String COLUMN_LOGISTICS_ITEM_ID = "item_id";
    public static final String COLUMN_LOGISTICS_STAGE_ID = "stage_id";
    public static final String COLUMN_LOGISTICS_DLV_NAME = "dlv_name";
    public static final String COLUMN_LOGISTICS_DLV_TIME = "dlv_time";
    public static final String COLUMN_LOGISTICS_EVENT_ID = "event_id";
    public static final String COLUMN_LOGISTICS_EVENT_DESC = "event_desc";
    public static final String COLUMN_LOGISTICS_MOBILE_PHONE = "mobile_phone";
    public static final String COLUMN_LOGISTICS_REAL_NAME = "real_name";


    private static final String TAG = "OrdersDBHelper";

    private static final int DB_VERSION = 4;

    private static final String DB_NAME = "orderItems";


    private static final String CREATE_TABLE_ORDERS = "create table " + TABLE_ORDERS + " (" +
            COLUMN_ORDERS_ID + " integer primary key autoincrement," +
            COLUMN_ORDERS_ORDER_ID + " int default -1," +
            COLUMN_ORDERS_ORDER_NUMBER + " int default -1," +
            COLUMN_ORDERS_CREATE_TIME + " integer default 10," +
            COLUMN_ORDERS_ADD_TIME + " integer default 10," +
            COLUMN_ORDERS_PRICE + " decimal(6, 2) default 0.00," +
            COLUMN_ORDERS_SUBTOTAL + " decimal(6, 2) default 0.00," +
            COLUMN_ORDERS_MOBILE_PHONE + " varchar(20) default '0'," +
            COLUMN_ORDERS_ADDRESS + " varchar(255) default ''," +
            COLUMN_ORDERS_REMARKS + " text default ''," +
            COLUMN_ORDERS_SOURCE + " varchar(100) default ''," +
            COLUMN_ORDERS_ORDER_CODE + " varchar(18) not null unique default ''," +
            COLUMN_ORDERS_RESTAURANT_ID + " integer default -1," +
            COLUMN_ORDERS_REQUEST_TYPE + " integer default " + OrderItem.REQUEST_ALL_DATA + ");";

    private static final String CREATE_TABLE_GOODS = "create table " + TABLE_GOODS + " (" +
            COLUMN_GOODS_ID + " integer primary key autoincrement," +
            COLUMN_GOODS_ORDER_ID + " int default -1," +
            COLUMN_GOODS_ITEM_ID + " integer default -1," +
            COLUMN_GOODS_ORDER_CODE + " varchar(18) default ''," +
            COLUMN_GOODS_RESTAURANT + " varchar(60) default ''," +
            COLUMN_GOODS_COURSE_NAME + " varchar(60) default ''," +
            COLUMN_GOODS_PRICE + " decimal(6, 2) default 0.00," +
            COLUMN_GOODS_QUANTITY + " int default 0," +
            COLUMN_GOODS_TOTAL_PRICE + " decimal(10, 2) default 0.00);";

    private static final String CREATE_TABLE_LOGISTICS = "create table " + TABLE_LOGISTICS + " (" +
            COLUMN_LOGISTICS_ID + " integer primary key autoincrement," +
            COLUMN_LOGISTICS_ITEM_ID + " int default -1," +
            COLUMN_LOGISTICS_STAGE_ID + " int default -1," +
            COLUMN_LOGISTICS_DLV_NAME + " varchar(20) default ''," +
            COLUMN_LOGISTICS_DLV_TIME + " varchar(20) default ''," +
            COLUMN_LOGISTICS_EVENT_ID + " int default -1," +
            COLUMN_LOGISTICS_EVENT_DESC + " text default ''," +
            COLUMN_LOGISTICS_MOBILE_PHONE + " varchar(20) default ''," +
            COLUMN_LOGISTICS_REAL_NAME + " varchar(20) default '');";


    private static final String CREATE_INDEX_ORDERS = "create index if not exists "
            + INDEX_ORDERS + " on " + TABLE_ORDERS
            + " (" + COLUMN_ORDERS_ORDER_CODE + ");";

    private static final String CREATE_INDEX_GOODS = "create index if not exists "
            + INDEX_GOODS + " on " + TABLE_GOODS
            + " (" + COLUMN_GOODS_ORDER_CODE + ");";


    public OrdersDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

//    public OrdersDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                             int version, DatabaseErrorHandler errorHandler) {
//        super(context, name, factory, version, errorHandler);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_GOODS);
        db.execSQL(CREATE_TABLE_LOGISTICS);

        db.execSQL(CREATE_INDEX_ORDERS);
        db.execSQL(CREATE_INDEX_GOODS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.w(TAG, "upgrade from version " + oldVersion + " to version " + newVersion
                + ", which will destroy all old data");
        db.execSQL("drop table if exists " + TABLE_ORDERS);
        db.execSQL("drop table if exists " + TABLE_GOODS);
        db.execSQL("drop table if exists " + TABLE_LOGISTICS);

        db.execSQL("drop index if exists " + INDEX_ORDERS);
        db.execSQL("drop index if exists " + INDEX_GOODS);

        onCreate(db);
    }
}
