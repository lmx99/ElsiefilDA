package com.lifeisle.jekton.order.search;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chatuidemo.MyApplication;
import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderItem;
import com.lifeisle.jekton.util.Logger;
import com.lifeisle.jekton.util.StringUtils;
import com.lifeisle.jekton.util.Toaster;
import com.lifeisle.jekton.util.network.AutoLoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class OrderSearchActivity extends AppCompatActivity
        implements OrderSearchFragment.OnSearchOrder {

    private static final String LOG_TAG = OrderSearchActivity.class.getSimpleName();

    private String[] mClassifications;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_search);

        mClassifications = getResources().getStringArray(R.array.order_search_classification);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentManager.findFragmentByTag("OrderSearchResultFragment");
        if (fragment != null) {
            transaction.replace(R.id.place_holder, fragment);
        } else {
            transaction.add(R.id.place_holder, new OrderSearchFragment());
        }
        transaction.commit();
    }

    @Override
    public void onSearchOrder(String orderNumber, String orderCode, String phone,
                              String address, String source, int classify) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.show();
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
                                        onSearchFail();
                                    }
                                } catch (JSONException e) {
                                    onSearchFail();
                                    Logger.e(LOG_TAG, "search order fail", e);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                onSearchFail();
                                Logger.e(LOG_TAG, "search order fail", volleyError);
                            }
                        }) {

                    @Override
                    protected void setParams(Map<String, String> params) {
                        // TODO: 8/29/2015 add search params
                    }
                }
        );
    }

    private void processResponse(JSONObject response) throws JSONException {
        List<OrderItem> orderItems = OrderItem.makeOrderItems(response);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        OrderSearchResultFragment fragment = new OrderSearchResultFragment();
        fragment.setOrderItems(orderItems);
        transaction.addToBackStack("OrderSearchResultFragment");
        transaction.replace(R.id.place_holder, fragment, "OrderSearchResultFragment");
        transaction.commit();

        mProgressDialog.dismiss();
    }

    private void onSearchFail() {
        mProgressDialog.dismiss();
        Toaster.showShort(OrderSearchActivity.this, R.string.error_search_fail);
    }
}
