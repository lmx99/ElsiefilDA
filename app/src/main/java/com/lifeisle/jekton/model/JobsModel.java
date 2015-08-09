package com.lifeisle.jekton.model;

import android.content.Intent;
import android.net.Uri;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.DemoApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.JobsActivity;
import com.lifeisle.jekton.activity.MyBrowserActivity;
import com.lifeisle.jekton.bean.JobInfo;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class JobsModel {

    private static final String TAG = "JobsModel";

    private JobsActivity activity;
    private List<JobInfo> jobInfoList;

    public JobsModel(JobsActivity activity) {
        this.activity = activity;

        loadData();
    }

    private void loadData() {
        DemoApplication.addToRequestQueue(new OccupationInfoRequest(
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getInt("status") == 0) {
                                JSONArray occupations = jsonObject.getJSONArray("list");
                                int length = occupations.length();
                                jobInfoList = new ArrayList<>(length);
                                for (int i = 0; i < length; ++i) {
                                    jobInfoList.add(
                                            JobInfo.newInstance(occupations.getJSONObject(i)));
                                }

                                activity.onDataLoaded();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toaster.showShort(activity, R.string.error_network_fail);
                    }
                }
        ));

    }





    public int size() {
        return jobInfoList.size();
    }

    public JobInfo getItem(int index) {
        return jobInfoList.get(index);
    }


    public void apply(int reId) {
        DemoApplication.addToRequestQueue(new ApplyJobRequest(reId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 0) {
                                Toaster.showShort(activity, R.string.success_apply);
                            } else {
                                Toaster.showShort(activity, R.string.error_apply_fail);
                            }
                        } catch (JSONException e) {
                            Logger.e(TAG, e.toString());
                            Toaster.showShort(activity, R.string.error_apply_fail);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toaster.showShort(activity, R.string.error_apply_fail);
                    }
                }));
    }


    public void details(String url) {
        Intent intent = new Intent(activity, MyBrowserActivity.class);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }










    private class OccupationInfoRequest extends AutoLoginRequest {

        public OccupationInfoRequest(Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
            super(activity, Method.POST, StringUtils.getServerPath(), listener, errorListener);
        }


        @Override
        protected void setParams(Map<String, String> params) {
            params.put("sys", "job");
            params.put("ctrl", "job_epl");
            params.put("action", "get_job");
        }
    }



    private class ApplyJobRequest extends AutoLoginRequest {

        private int reId;

        public ApplyJobRequest(int reId, Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
            super(activity, Method.POST, StringUtils.getServerPath(), listener, errorListener);

            this.reId = reId;
        }

        @Override
        protected void setParams(Map<String, String> params) {
            params.put("re_id", "" + reId);
            params.put("pend_type", "1");
            params.put("sys", "job");
            params.put("ctrl", "job_epl");
            params.put("action", "want_job");
        }
    }
}
