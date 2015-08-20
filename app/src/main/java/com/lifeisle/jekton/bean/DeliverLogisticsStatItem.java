package com.lifeisle.jekton.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class DeliverLogisticsStatItem {

    public String fee;
    public String num;
    public String allowance;
    public String cash;
    public String date;


    private static final String OWM_DELIVER_FEE = "dlv_fee";
    private static final String OWM_DELIVER_NUM = "dlv_num";
    private static final String OWM_DELIVER_CASH = "dlv_cash";
    private static final String OWM_DELIVER_ALLOWANCE = "dlv_alow";
    private static final String OWM_DELIVER_DATE = "date_time";



    public static DeliverLogisticsStatItem newInstance(JSONObject jsonObject) throws JSONException {
        DeliverLogisticsStatItem item = new DeliverLogisticsStatItem();

        item.fee = jsonObject.getString(OWM_DELIVER_FEE);
        item.num = jsonObject.getString(OWM_DELIVER_NUM);
        item.cash = jsonObject.getString(OWM_DELIVER_CASH);
        item.allowance = jsonObject.getString(OWM_DELIVER_ALLOWANCE);
        item.date = jsonObject.getString(OWM_DELIVER_DATE);

        return item;
    }
}
