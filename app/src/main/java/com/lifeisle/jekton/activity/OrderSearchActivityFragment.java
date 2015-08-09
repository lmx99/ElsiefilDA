package com.lifeisle.jekton.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lifeisle.android.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderSearchActivityFragment extends Fragment {

    public OrderSearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_search, container, false);
    }
}
