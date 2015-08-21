package com.lifeisle.jekton.order.stat.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public class MotorLogisticsStatItem {

    public String motorFee;
    public String allowance;
    public String date;

    public static final String OWM_MOTOR_FEE = "motor_fee";
    public static final String OWM_ALLOWANCE = "alow_fee";
    public static final String OWM_DATE = "date_time";


    public static MotorLogisticsStatItem newInstance(JSONObject jsonObject) throws JSONException {
        MotorLogisticsStatItem item = new MotorLogisticsStatItem();

        item.motorFee = jsonObject.getString(OWM_MOTOR_FEE);
        item.allowance = jsonObject.getString(OWM_ALLOWANCE);
        item.date = jsonObject.getString(OWM_DATE);

        return item;
    }
}
