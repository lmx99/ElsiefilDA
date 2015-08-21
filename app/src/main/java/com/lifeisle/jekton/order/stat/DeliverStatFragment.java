package com.lifeisle.jekton.order.stat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.order.stat.controller.DeliverLogisticsStatController;
import com.lifeisle.jekton.order.stat.controller.MotorLogisticsStatController;
import com.lifeisle.jekton.order.stat.controller.StatController;
import com.lifeisle.jekton.util.Logger;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class DeliverStatFragment extends Fragment {

    public static final int REQUEST_CODE_SET_INTERVAL = 1;
    public static final String EXTRA_STAT_INTERVAL = "DeliverStatFragment.EXTRA_STAT_INTERVAL";
    public static final String EXTRA_STAT_TYPE = "DeliverStatFragment.EXTRA_STAT_TYPE";

    public static final int STAT_TYPE_DELIVER = 0;
    public static final int STAT_TYPE_MOTOR = 1;

    private static final String LOG_TAG = "DeliverStatFragment";

    private DeliverStatModel mModel;
    private BaseAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        int type = intent.getIntExtra(EXTRA_STAT_TYPE, -1);

        StatController controller;
        switch (type) {
            case DeliverStatFragment.STAT_TYPE_DELIVER:
                controller = new DeliverLogisticsStatController(this);
                break;
            case DeliverStatFragment.STAT_TYPE_MOTOR:
                controller = new MotorLogisticsStatController(this);
                break;
            default:
                throw new IllegalArgumentException("unknown type " + type);
        }

        mModel = controller.getDeliverStatModel();
        mAdapter = controller.getAdapter();

        View view = inflater.inflate(controller.getResId(), container, false);
        ListView listView = (ListView) view.findViewById(R.id.deliver_stat_list);
        listView.setAdapter(mAdapter);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_deliver_stat, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_deliver_stat_option) {
            Intent intent = new Intent(getActivity(), DeliverStatOptionActivity.class);
            intent.putExtra(EXTRA_STAT_INTERVAL, mModel.getInterval());
            startActivityForResult(intent, REQUEST_CODE_SET_INTERVAL);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(LOG_TAG, "onActivityResult, requestCode = " + requestCode +
                ", resultCode = " + resultCode);

        if (requestCode == REQUEST_CODE_SET_INTERVAL && resultCode == Activity.RESULT_OK) {
            String[] interval = data.getStringArrayExtra(EXTRA_STAT_INTERVAL);
            Logger.d(LOG_TAG, "onActivityResult, interval = " + Arrays.toString(interval));

            mModel.setInterval(interval);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {      // fix bug when constructing DeliverStatModel
            mAdapter.notifyDataSetChanged();
        }
    }


}
