package com.lifeisle.jekton.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.DeliverStatCategoryActivity;
import com.lifeisle.jekton.activity.JobsActivity;
import com.lifeisle.jekton.activity.MyPositionActivity;
import com.lifeisle.jekton.activity.OrderSearchActivity;
import com.lifeisle.jekton.activity.QRCodeScanActivity;
import com.lifeisle.jekton.activity.ScheduleCategoryActivity;

/**
 * @author Jekton
 * @version 0.01 7/11/2015
 */
public class LogisticsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "LogisticsFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics, container, false);
        return view;
    }
    private void init() {
        getView().findViewById(R.id.schedule).setOnClickListener(this);
        getView().findViewById(R.id.myPosition).setOnClickListener(this);
        getView().findViewById(R.id.occupationAuth).setOnClickListener(this);

        View schedule = getView().findViewById(R.id.schedule);
        schedule.setOnClickListener(this);
        schedule.setVisibility(View.GONE);
        View myPosition = getView().findViewById(R.id.myPosition);
        myPosition.setOnClickListener(this);
        myPosition.setVisibility(View.GONE);
        getView().findViewById(R.id.occupationAuth).setOnClickListener(this);

        getView().findViewById(R.id.deliver_stat).setOnClickListener(this);
        View orderSearch = getView().findViewById(R.id.order_search);
        orderSearch.setOnClickListener(this);
        orderSearch.setVisibility(View.GONE);

        getView().findViewById(R.id.scanQRCode).setOnClickListener(this);

        getView().findViewById(R.id.scanQRCode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schedule: {
                startActivity(ScheduleCategoryActivity.class);
                break;
            }
            case R.id.myPosition: {
                startActivity(MyPositionActivity.class);
                break;
            }
            case R.id.occupationAuth: {
                startActivity(JobsActivity.class);
                break;
            }
            case R.id.deliver_stat: {
                startActivity(DeliverStatCategoryActivity.class);
                break;
            }
            case R.id.order_search: {
                startActivity(OrderSearchActivity.class);
                break;
            }
            case R.id.scanQRCode: {
                startActivity(QRCodeScanActivity.class);
                break;
            }
        }
    }


    private <T extends Activity> void startActivity(Class<T> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }
}
