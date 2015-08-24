package com.lifeisle.jekton.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.MainActivity;
import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.JobsActivity;
import com.lifeisle.jekton.activity.MyPositionActivity;
import com.lifeisle.jekton.activity.OrderSearchActivity;
import com.lifeisle.jekton.activity.QRCodeScanActivity;
import com.lifeisle.jekton.activity.ScheduleCategoryActivity;
import com.lifeisle.jekton.order.stat.DeliverStatCategoryActivity;

public class LogisticsFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logistics, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        init();
    }

    private void init() {
        View view = getView();
        view.findViewById(R.id.schedule).setOnClickListener(this);
        view.findViewById(R.id.myPosition).setOnClickListener(this);
        view.findViewById(R.id.job_opportunity).setOnClickListener(this);

        View schedule = view.findViewById(R.id.schedule);
        schedule.setOnClickListener(this);
        schedule.setVisibility(View.GONE);
        View myPosition = view.findViewById(R.id.myPosition);
        myPosition.setOnClickListener(this);
        myPosition.setVisibility(View.GONE);
        view.findViewById(R.id.job_opportunity).setOnClickListener(this);

        view.findViewById(R.id.deliver_stat).setOnClickListener(this);
        View orderSearch = view.findViewById(R.id.order_search);
        orderSearch.setOnClickListener(this);
        orderSearch.setVisibility(View.GONE);
        view.findViewById(R.id.scanQRCode).setOnClickListener(this);

        view.findViewById(R.id.scanQRCode).setOnClickListener(this);
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
            case R.id.job_opportunity: {
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (((MainActivity) getActivity()).isConflict) {
            outState.putBoolean("isConflict", true);
        } else if (((MainActivity) getActivity()).getCurrentAccountRemoved()) {
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }
}
