package com.lifeisle.jekton.job;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.job.adapter.MyJobPeriodsAdapter;
import com.lifeisle.jekton.job.adapter.SubordinateListAdapter;
import com.lifeisle.jekton.job.data.bean.MyJobItem;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class MyJobActivity extends AppCompatActivity {

    private static final String LOG_TAG = MyJobActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);

//        String testData = "{\"status\": \"0\",\"under_job\": [{\"job_id\": \"3\",\"job_sn\": \"job1440732584529\",\"his_id\": \"2\",\"user_name\": \"maozi\",\"title\": \"领班\",\"introduce\": \"\",\"country\": \"3409\",\"province\": \"3410\",\"city\": \"3443\",\"district\": \"3446\",\"address\": \"\",\"map\": \"\",\"contacts\": \"\",\"tel_phone\": \"\",\"mobile_phone\": \"\",\"add_time\": \"1440744182\",\"view_desc\": \"\",\"jcat_id\": \"4\",\"infos\": [{\"info_id\": \"3\",\"job_id\": \"3\",\"start_time\": \"2015-08-28 11:30\",\"end_time\": \"2015-08-29 12:45\",\"salary\": \"8.00\",\"bonus\": \"7.00\",\"attachment\": \"\",\"pay_way\": \"0\",\"add_time\": \"1440744182\",\"epls\": [{\"status_id\": \"18\",\"info_id\": \"3\",\"user_name\": \"youmylove\",\"status\": \"1\",\"add_time\": \"1440744182\"},{\"status_id\": \"18\",\"info_id\": \"3\",\"user_name\": \"134\",\"status\": \"1\",\"add_time\": \"1440744182\"}]},{\"info_id\": \"3\",\"job_id\": \"3\",\"start_time\": \"2015-08-28 11:30\",\"end_time\": \"2015-08-29 12:45\",\"salary\": \"8.00\",\"bonus\": \"7.00\",\"attachment\": \"\",\"pay_way\": \"0\",\"add_time\": \"1440744182\",\"epls\": [{\"status_id\": \"18\",\"info_id\": \"3\",\"user_name\": \"aha\",\"status\": \"1\",\"add_time\": \"1440744182\"}]}],\"rights\": [{\"his_id\": \"2\",\"right_id\": \"8\",\"source\": \"job1440732680952\",\"target\": \"job1440732584529\",\"cat_id\": \"6\",\"cat_name\": \"给签到\",\"introduce\": \"\",\"add_time\": \"1440744182\"},{\"his_id\": \"2\",\"right_id\": \"9\",\"source\": \"job1440732680952\",\"target\": \"job1440732584529\",\"cat_id\": \"7\",\"cat_name\": \"给签退\",\"introduce\": \"\",\"add_time\": \"1440744182\"}]},{\"job_id\": \"3\",\"job_sn\": \"job1440732584529\",\"his_id\": \"2\",\"user_name\": \"maozi\",\"title\": \"领班2\",\"introduce\": \"\",\"country\": \"3409\",\"province\": \"3410\",\"city\": \"3443\",\"district\": \"3446\",\"address\": \"\",\"map\": \"\",\"contacts\": \"\",\"tel_phone\": \"\",\"mobile_phone\": \"\",\"add_time\": \"1440744182\",\"view_desc\": \"\",\"jcat_id\": \"4\",\"infos\": [{\"info_id\": \"3\",\"job_id\": \"3\",\"start_time\": \"2015-08-31 11:30\",\"end_time\": \"2015-08-32 12:45\",\"salary\": \"8.00\",\"bonus\": \"7.00\",\"attachment\": \"\",\"pay_way\": \"0\",\"add_time\": \"1440744182\",\"epls\": [{\"status_id\": \"18\",\"info_id\": \"3\",\"user_name\": \"abc\",\"status\": \"1\",\"add_time\": \"1440744182\"}]},{\"info_id\": \"3\",\"job_id\": \"3\",\"start_time\": \"2015-08-28 11:30\",\"end_time\": \"2015-08-29 12:45\",\"salary\": \"8.00\",\"bonus\": \"7.00\",\"attachment\": \"\",\"pay_way\": \"0\",\"add_time\": \"1440744182\",\"epls\": [{\"status_id\": \"18\",\"info_id\": \"3\",\"user_name\": \"def\",\"status\": \"1\",\"add_time\": \"1440744182\"}]}],\"rights\": [{\"his_id\": \"2\",\"right_id\": \"8\",\"source\": \"job1440732680952\",\"target\": \"job1440732584529\",\"cat_id\": \"6\",\"cat_name\": \"给签到\",\"introduce\": \"\",\"add_time\": \"1440744182\"},{\"his_id\": \"2\",\"right_id\": \"9\",\"source\": \"job1440732680952\",\"target\": \"job1440732584529\",\"cat_id\": \"7\",\"cat_name\": \"给签退\",\"introduce\": \"\",\"add_time\": \"1440744182\"}]}],\"my_job\": {\"title\": \"1\",\"intro\": \"1\",\"address\": \"1\",\"infos\": [{\"start_time\": \"1\",\"end_time\": \"1\",\"epls\": [{\"user_name\": \"1\",\"estatus\": \"1\",\"status_id\": \"1\"},{\"user_name\": \"2\",\"estatus\": \"1\",\"status_id\": \"1\"}]},{\"start_time\": \"1234-15-45\",\"end_time\": \"1234-15-09\",\"epls\": [{\"user_name\": \"3\",\"estatus\": \"1\",\"status_id\": \"1\"},{\"user_name\": \"4\",\"estatus\": \"1\",\"status_id\": \"1\"}]}]}}";

        MyApplication.addToRequestQueue(
                new AutoLoginRequest(
                        this, Request.Method.POST, StringUtils.getServerPath(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {
                                    if (jsonObject.getInt("status") == 0) {
                                        processResponse(jsonObject);
                                    } else {
                                        onRequestFail();
                                    }
                                } catch (JSONException e) {
                                    onRequestFail();
                                    Logger.e(LOG_TAG, "search order fail", e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                onRequestFail();
                                Logger.e(LOG_TAG, "search order fail", volleyError);
                            }
                        }) {

                    @Override
                    protected void setParams(Map<String, String> params) {
                        params.put("sys", "job");
                        params.put("ctrl", "job_arrg");
                        params.put("action", "my_pos");
                    }
                }
        );

    }

    private void processResponse(JSONObject response) throws JSONException {
        MyJobItem myJobItem = MyJobItem.makeInstance(response);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(title.getText().toString() + myJobItem.title);

        TextView address = (TextView) findViewById(R.id.address);
        address.setText(address.getText().toString() + myJobItem.address);

        TextView info = (TextView) findViewById(R.id.info);
        info.setText(info.getText().toString() + myJobItem.intro);

        ListView listView = (ListView) findViewById(R.id.periods);
        listView.setAdapter(new MyJobPeriodsAdapter(this, myJobItem));

        listView = (ListView) findViewById(R.id.subordinateList);
        listView.setAdapter(new SubordinateListAdapter(this, myJobItem));
    }

    private void onRequestFail() {

    }
}
