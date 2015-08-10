package com.lifeisle.jekton.activity;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class OrderSearchActivityFragment extends Fragment {

    private EditText mOrderNumEditText;
    private EditText mOrderCodeEditText;
    private EditText mPhoneEditText;
    private EditText mAddressEditText;
    private EditText mSourceEditText;
    private Spinner mClassifySpinner;

    public OrderSearchActivityFragment() {
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.order_search_classification, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mClassifySpinner.setAdapter(adapter);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_order_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
