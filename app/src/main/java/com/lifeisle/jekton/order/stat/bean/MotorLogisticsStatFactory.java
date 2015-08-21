package com.lifeisle.jekton.order.stat.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public class MotorLogisticsStatFactory implements StatItemFactory {

    @Override
    public Object createFromJSON(JSONObject jsonObject) throws JSONException {
        return MotorLogisticsStatItem.newInstance(jsonObject);
    }
}
