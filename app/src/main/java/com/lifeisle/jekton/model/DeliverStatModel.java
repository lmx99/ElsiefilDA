package com.lifeisle.jekton.model;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.bean.DeliverLogisticsStatItem;
import com.lifeisle.jekton.bean.MotorLogisticsStatItem;
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
    private int mType;
    private List<Object> deliverLogisticsStatItems;

    private String[] interval;

    public DeliverStatModel(DeliverStatFragment fragment, int type) {
        this.fragment = fragment;
        mType = type;
        deliverLogisticsStatItems = new ArrayList<>();

        interval = DateUtils.getDefaultDeliverStatInterval();
        requestStat();
        Logger.d(LOG_TAG, "start = " + interval[0] + ", end = " + interval[1]);
    }




    public int getCount() {
        return deliverLogisticsStatItems.size();
    }


    public Object getItem(int position) {
        return deliverLogisticsStatItems.get(position);
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
                                deliverLogisticsStatItems.clear();
                                readResponse(itemArray);
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

    private void readResponse(JSONArray array) throws JSONException {
        switch (mType) {
            case DeliverStatFragment.STAT_TYPE_DELIVER:
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject item = array.getJSONObject(i);
                    deliverLogisticsStatItems.add(DeliverLogisticsStatItem.newInstance(item));
                }
                break;
            case DeliverStatFragment.STAT_TYPE_MOTOR:
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject item = array.getJSONObject(i);
                    deliverLogisticsStatItems.add(MotorLogisticsStatItem.newInstance(item));
                }
                break;
        }

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

            switch (mType) {
                case DeliverStatFragment.STAT_TYPE_DELIVER:
                    params.put("action", "edate_data");
                    break;
                case DeliverStatFragment.STAT_TYPE_MOTOR:
                    params.put("action", "motor_date_data");
                    break;
            }
        }
    }
}
