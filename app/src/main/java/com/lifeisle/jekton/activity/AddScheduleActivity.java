package com.lifeisle.jekton.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lifeisle.android.R;
import com.lifeisle.jekton.fragment.SettingRepeatDialogFragment;
import com.lifeisle.jekton.model.ScheduleAdder;
import com.lifeisle.jekton.util.DateUtils;

public class AddScheduleActivity extends AppCompatActivity implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        SettingRepeatDialogFragment.OnRepeatChangeListener {

    private ScheduleAdder scheduleAdder;

    private TextView startTimeDay;
    private TextView startTimeClock;
    private TextView endTimeDay;
    private TextView endTimeClock;
    
    private TextView repeat;
    private TextView notify;
    
    private TextView ok;
    private TextView cancel;
    private TextView delete;

    /**
     * Current textView that has been clicked to show the {@link TimePickerDialog}.<br />
     * Used in {@link #onTimeSet(TimePicker, int, int)} and {@link #setRepeat(TextView)}
     * as a bookkeeping
     */
    private TextView currentSettingTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        scheduleAdder = new ScheduleAdder(this);
        init();
    }

    private void init() {
        startTimeDay = (TextView) findViewById(R.id.start_time_day);
        startTimeClock = (TextView) findViewById(R.id.start_time_clock);
        endTimeDay = (TextView) findViewById(R.id.end_time_day);
        endTimeClock = (TextView) findViewById(R.id.end_time_clock);

        repeat = (TextView) findViewById(R.id.repeat);
        notify = (TextView) findViewById(R.id.notify);

        ok = (TextView) findViewById(R.id.ok);
        cancel = (TextView) findViewById(R.id.cancel);
        delete = (TextView) findViewById(R.id.delete);


        startTimeDay.setOnClickListener(this);
        startTimeClock.setOnClickListener(this);
        endTimeDay.setOnClickListener(this);
        endTimeClock.setOnClickListener(this);

        repeat.setOnClickListener(this);
        notify.setOnClickListener(this);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_time_day: {
                pickDate(startTimeDay);
                break;
            }
            case R.id.start_time_clock: {
                pickTime(startTimeClock);
                break;
            }
            case R.id.end_time_day: {
                pickDate(endTimeDay);
                break;
            }
            case R.id.end_time_clock: {
                pickTime(endTimeClock);
                break;
            }
            case R.id.repeat: {
                setRepeat(repeat);
                break;
            }
            case R.id.notify: {
                break;
            }
            case R.id.ok: {
                break;
            }
            case R.id.cancel: {
                break;
            }
            case R.id.delete: {
                break;
            }
            default:
                break;
        }
    }





    public void pickTime(TextView textView) {
        currentSettingTextView = textView;
        String time = textView.getText().toString();
        TimePickerDialog dialog = new TimePickerDialog(this,
                                                       this,
                                                       DateUtils.extractHour(time),
                                                       DateUtils.extractMin(time),
                                                       true);
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        currentSettingTextView.setText(DateUtils.formatTime(hourOfDay, minute));
    }


    public void pickDate(TextView textView) {
        String date = textView.getText().toString();
        DatePickerDialog dialog = new DatePickerDialog(this,
                                                       this,
                                                       DateUtils.extractYear(date),
                                                       DateUtils.extractMonth(date),
                                                       DateUtils.extractDay(date));
        dialog.getDatePicker().setTag(textView);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        TextView textView = (TextView) view.getTag();
        textView.setText(DateUtils.formatDate(year, monthOfYear, dayOfMonth));
    }


    public void setRepeat(TextView textView) {
        currentSettingTextView = textView;

        SettingRepeatDialogFragment dialogFragment = new SettingRepeatDialogFragment();
        dialogFragment.setRepeatChangeListener(this);
        dialogFragment.show(getSupportFragmentManager(), "setRepeat");
    }

    @Override
    public void onRepeatChanged(int repeat) {
        currentSettingTextView.setText(Integer.toBinaryString(repeat));
    }
}
