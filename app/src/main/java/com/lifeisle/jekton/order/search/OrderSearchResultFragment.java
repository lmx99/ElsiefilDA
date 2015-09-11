package com.lifeisle.jekton.order.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.OrderItem;

import java.util.List;

/**
 * @author Jekton
 */
public class OrderSearchResultFragment extends Fragment {

    private ListView mListView;

    public OrderSearchResultFragment() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_search_result, container, false);
        mListView = (ListView) view.findViewById(R.id.result_list);
        return view;
    }


    public void setOrderItems(List<OrderItem> orderItems) {
        BaseAdapter adapter = new ResultListAdapter(getActivity(), orderItems);
        mListView.setAdapter(adapter);
    }
}
