package com.lifeisle.jekton.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lifeisle.android.R;
import com.lifeisle.jekton.data.ScheduleContract;
import com.lifeisle.jekton.fragment.SettingRepeatDialogFragment;
import com.lifeisle.jekton.schedule.ScheduleController;
import com.lifeisle.jekton.schedule.ScheduleDetailView;
import com.lifeisle.jekton.schedule.ScheduleInsertController;
import com.lifeisle.jekton.schedule.ScheduleModel;
import com.lifeisle.jekton.util.DateUtils;
import com.lifeisle.jekton.util.Toaster;

public class ScheduleDetailActivity extends AppCompatActivity implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        SettingRepeatDialogFragment.OnRepeatChangeListener, ScheduleDetailView {

    /**
     * Stored as a field to prevent being reclaimed
     */
    private ScheduleController controller;

    private EditText mTitle;

    private TextView startTimeDay;
    private TextView startTimeClock;
    private TextView endTimeDay;
    private TextView endTimeClock;

    private TextView repeat;
    private CheckBox notify;

    /**
     * Current textView that has been clicked to show the {@link TimePickerDialog}.<br />
     * Used in {@link #onTimeSet(TimePicker, int, int)} as a bookkeeping
     */
    private TextView currentSettingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        init();

        ScheduleModel scheduleModel = new ScheduleModel(this);
        controller = new ScheduleInsertController(this, scheduleModel);
    }

    private void init() {
        mTitle = (EditText) findViewById(R.id.event_title);

        initTime();

        repeat = (TextView) findViewById(R.id.repeat);
        repeat.setTag(0);
        notify = (CheckBox) findViewById(R.id.notify);

        repeat.setOnClickListener(this);
        notify.setOnClickListener(this);

        findViewById(R.id.cancel).setOnClickListener(this);
    }

    private void initTime() {
        startTimeDay = (TextView) findViewById(R.id.start_time_day);
        startTimeClock = (TextView) findViewById(R.id.start_time_clock);
        endTimeDay = (TextView) findViewById(R.id.end_time_day);
        endTimeClock = (TextView) findViewById(R.id.end_time_clock);

        startTimeDay.setOnClickListener(this);
        startTimeClock.setOnClickListener(this);
        endTimeDay.setOnClickListener(this);
        endTimeClock.setOnClickListener(this);
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
                setupRepeat();
                break;
            }
            case R.id.cancel: {
                finish();
                break;
            }
            default:
                break;
        }
    }


    private void pickTime(TextView textView) {
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


    private void pickDate(TextView textView) {
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


    private void setupRepeat() {
        SettingRepeatDialogFragment dialogFragment = new SettingRepeatDialogFragment();
        dialogFragment.setRepeat(getRepeat());
        dialogFragment.setRepeatChangeListener(this);
        dialogFragment.show(getSupportFragmentManager(), "setRepeat");
    }

    @Override
    public void onRepeatChanged(int repeat) {
        setRepeat(repeat);
    }


    @Override
    public void setEventTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void setStartTime(String day, String clockTime) {
        startTimeDay.setText(day);
        startTimeClock.setText(clockTime);
    }

    @Override
    public void setEndTime(String day, String clockTime) {
        endTimeDay.setText(day);
        endTimeClock.setText(clockTime);
    }

    @Override
    public void setRepeat(int repeat) {
        this.repeat.setText(DateUtils.formatRepeatOfWeekString(repeat));
        // store the int repeat to the tag of repeatTextView
        this.repeat.setTag(repeat);
    }

    @Override
    public void setNotify(int notify) {
        if (notify == ScheduleContract.EventEntry.NOTIFY_ONCE) {
            this.notify.setChecked(true);
        }
    }

    @Override
    public String getEventTitle() {
        return mTitle.getText().toString();
    }

    @Override
    public String[] getStartTime() {
        return new String[] {
                startTimeDay.getText().toString(),
                startTimeClock.getText().toString()
        };
    }

    @Override
    public String[] getEndTime() {
        return new String[] {
                endTimeDay.getText().toString(),
                endTimeClock.getText().toString()
        };
    }

    @Override
    public int getRepeat() {
        return (int) repeat.getTag();
    }

    @Override
    public int getNotify() {
        return notify.isChecked() ? ScheduleContract.EventEntry.NOTIFY_ONCE
                                  : ScheduleContract.EventEntry.NOTIFY_NONE;
    }

    @Override
    public void showErrMsg(int msgId) {
        Toaster.showShort(this, msgId);
    }

    @Override
    public void close() {
        finish();
    }
}
