package com.lifeisle.jekton.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.DeliverStatItem;
import com.lifeisle.jekton.fragment.DeliverStatFragment;
import com.lifeisle.jekton.util.DateUtils;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Jekton
 * @version 0.1 8/5/2015
 */
public class DeliverStatModel {

    private static final String LOG_TAG = "DeliverStatModel";

    private DeliverStatFragment fragment;
    private List<DeliverStatItem> deliverStatItems;

    private String[] interval;

    public DeliverStatModel(DeliverStatFragment fragment) {
        this.fragment = fragment;
        deliverStatItems = new ArrayList<>();

        interval = DateUtils.getDefaultDeliverStatInterval();
        requestStat();
        Logger.d(LOG_TAG, "start = " + interval[0] + ", end = " + interval[1]);
    }




    public int getCount() {
        return deliverStatItems.size();
    }


    public DeliverStatItem getItem(int position) {
        return deliverStatItems.get(position);
    }


    public String[] getInterval() {
        return interval;
    }

    public void setInterval(String[] interval) {
        this.interval = interval;
        Logger.d(LOG_TAG, "setInterval, interval = " + Arrays.toString(interval));
        requestStat();
    }

    private void requestStat() {
        MyApplication.addToRequestQueue(new DeliverStatRequest(interval[0], interval[1],
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Logger.d(LOG_TAG, "response = " + jsonObject);
                        try {
                            if (jsonObject.getInt("status") == 0) {
                                JSONArray itemArray = jsonObject.getJSONArray("st_data");
                                deliverStatItems.clear();
                                for (int i = 0; i < itemArray.length(); ++i) {
                                    JSONObject item = itemArray.getJSONObject(i);
                                    deliverStatItems.add(DeliverStatItem.newInstance(item));
                                }
                                fragment.notifyDataSetChanged();
                                return;
                            }
                        } catch (JSONException e) {
                            Logger.e(LOG_TAG, e.toString(), e);
                        }
                        Toaster.showShort(fragment.getActivity(), R.string.error_network_fail);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Logger.e(LOG_TAG, volleyError);
                        Toaster.showShort(fragment.getActivity(), R.string.error_network_fail);
                    }
                }));
    }







    private class DeliverStatRequest extends AutoLoginRequest {

        private String startTime;
        private String endTime;

        public DeliverStatRequest(String startTime, String endTime,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
            super(fragment.getActivity(), Method.POST, StringUtils.getServerPath(), listener, errorListener);

            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        protected void setParams(Map<String, String> params) {
            params.put("start_time", startTime);
            params.put("end_time", endTime);
            params.put("sys", "fn");
            params.put("ctrl", "fn_data");
            params.put("action", "edate_data");
        }
    }
}
