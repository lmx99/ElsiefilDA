package com.lifeisle.jekton.order.stat.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public class GangerLogisticsStatItem {

    public String gangFee;
    public String allowance;
    public String date;

    public static final String OWM_GANGER_FEE = "gang_fee";
    public static final String OWM_ALLOWANCE = "alow_fee";
    public static final String OWM_DATE = "date_time";


    public static GangerLogisticsStatItem newInstance(JSONObject jsonObject) throws JSONException {
        GangerLogisticsStatItem item = new GangerLogisticsStatItem();

        item.gangFee = jsonObject.getString(OWM_GANGER_FEE);
        item.allowance = jsonObject.getString(OWM_ALLOWANCE);
        item.date = jsonObject.getString(OWM_DATE);

        return item;
    }
}
