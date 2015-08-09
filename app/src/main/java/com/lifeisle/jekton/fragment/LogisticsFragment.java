package com.lifeisle.jekton.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.QRCodeScanActivity;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics, container, false);

        view.findViewById(R.id.routine).setOnClickListener(this);
        view.findViewById(R.id.myOccupation).setOnClickListener(this);
        view.findViewById(R.id.occupationAuth).setOnClickListener(this);
        view.findViewById(R.id.scanQRCode).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanQRCode:
                Intent intent = new Intent(getActivity(), QRCodeScanActivity.class);
                startActivity(intent);
                break;

        }
    }
}
