package com.lifeisle.jekton.order.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.lifeisle.android.R;


public class OrderSearchFragment extends Fragment {

    private EditText mOrderNumEditText;
    private EditText mOrderCodeEditText;
    private EditText mPhoneEditText;
    private EditText mAddressEditText;
    private EditText mSourceEditText;
    private Spinner mClassifySpinner;

    public OrderSearchFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_search, container, false);

        initView(view);

        return view;
    }

    private void initView(View parent) {
        mOrderNumEditText = (EditText) parent.findViewById(R.id.order_number);
        mOrderCodeEditText = (EditText) parent.findViewById(R.id.order_code);
        mPhoneEditText = (EditText) parent.findViewById(R.id.phone);
        mAddressEditText = (EditText) parent.findViewById(R.id.address);
        mSourceEditText = (EditText) parent.findViewById(R.id.source);
        mClassifySpinner = (Spinner) parent.findViewById(R.id.classify);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getActivity(),
                                                R.array.order_search_classification,
                                                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mClassifySpinner.setAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_order_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_search) {
            ((OnSearchOrder) getActivity()).onSearchOrder(
                    mOrderNumEditText.getText().toString(),
                    mOrderCodeEditText.getText().toString(),
                    mPhoneEditText.getText().toString(),
                    mAddressEditText.getText().toString(),
                    mSourceEditText.getText().toString(),
                    mClassifySpinner.getSelectedItemPosition()
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    interface OnSearchOrder {
        void onSearchOrder(String orderNumber, String orderCode, String phone,
                           String address, String source, int classify);
    }

}
