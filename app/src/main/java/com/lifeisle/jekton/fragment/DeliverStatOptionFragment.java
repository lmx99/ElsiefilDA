package com.lifeisle.jekton.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.activity.DeliverStatActivity;
import com.lifeisle.jekton.util.DateUtils;
import com.lifeisle.jekton.util.Toaster;

/**
 * A placeholder fragment containing a simple view.
 */
public class DeliverStatOptionFragment extends Fragment
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private TextView startTimeTextView;
    private TextView endTimeTextView;

    private String[] interval;

    public DeliverStatOptionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deliver_stat_option, container, false);

        startTimeTextView = (TextView) view.findViewById(R.id.start_time);
        endTimeTextView = (TextView) view.findViewById(R.id.end_time);

        Intent intent = getActivity().getIntent();
        interval = intent.getStringArrayExtra(DeliverStatFragment.EXTRA_STAT_INTERVAL);
        if (interval == null) {
            interval = DateUtils.getDefaultDeliverStatInterval();
        }

        startTimeTextView.setText(interval[0]);
        endTimeTextView.setText(interval[1]);

        view.findViewById(R.id.set_start_time).setOnClickListener(this);
        view.findViewById(R.id.set_end_time).setOnClickListener(this);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_deliver_stat_option, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ok) {

            if (interval[0].compareTo(interval[1]) > 0) {
                Toaster.showShort(getActivity(), R.string.error_start_greater_end);
                interval[1] = interval[0];
                endTimeTextView.setText(interval[1]);
                return true;
            }
            Intent data = new Intent(getActivity(), DeliverStatActivity.class);
            data.putExtra(DeliverStatFragment.EXTRA_STAT_INTERVAL, interval);
            getActivity().setResult(Activity.RESULT_OK, data);
            getActivity().finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_start_time: {
                showDialog(interval[0], R.id.set_start_time);
                break;
            }
            case R.id.set_end_time: {
                showDialog(interval[1], R.id.set_end_time);
                break;
            }
            default:
                break;
        }
    }

    private void showDialog(String date, int id) {
        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                this,
                DateUtils.extractYear(date),
                DateUtils.extractMonth(date),
                DateUtils.extractDay(date)
        );
        dialog.getDatePicker().setId(id);
        dialog.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        switch (view.getId()) {
            case R.id.set_start_time: {
                interval[0] = DateUtils.formatDate(year, monthOfYear, dayOfMonth);
                startTimeTextView.setText(interval[0]);
                break;
            }
            case R.id.set_end_time: {
                interval[1] = DateUtils.formatDate(year, monthOfYear, dayOfMonth);
                endTimeTextView.setText(interval[1]);
                break;
            }
            default:
                break;
        }

    }
}
