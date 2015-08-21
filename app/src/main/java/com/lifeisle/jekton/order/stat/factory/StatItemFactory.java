package com.lifeisle.jekton.order.stat.factory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Jekton
 */
public interface StatItemFactory {

    Object createFromJSON(JSONObject jsonObject) throws JSONException;

}
