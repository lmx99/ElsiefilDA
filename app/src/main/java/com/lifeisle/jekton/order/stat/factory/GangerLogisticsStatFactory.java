package com.lifeisle.jekton.order.stat.factory;

import com.lifeisle.jekton.order.stat.bean.GangerLogisticsStatItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public class GangerLogisticsStatFactory implements StatItemFactory {

    @Override
    public Object createFromJSON(JSONObject jsonObject) throws JSONException {
        return GangerLogisticsStatItem.newInstance(jsonObject);
    }
}
