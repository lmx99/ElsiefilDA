package com.lifeisle.jekton.schedule;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lifeisle.android.R;
import com.lifeisle.jekton.schedule.data.ScheduleEvent;
import com.lifeisle.jekton.util.Logger;

/**
 * @author Jekton
 * @version 0.1 8/7/2015
 */
public class SettingRepeatDialogFragment extends DialogFragment
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String LOG_TAG = "SettingRepeatDialogFragment";

    private static final int DAYS_OF_WEEK = 7;
    
    /**
     * never repeat by default
     */
    private int repeat;
    private OnRepeatChangeListener callback;

    private CheckedTextView[] week;
    private RadioButton[] radioButtons;
    private RadioGroup radioGroup;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_repeat, null);

        initDayList(view);
        initOption(view);

        view.findViewById(R.id.ok).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);

        builder.setView(view);

        return builder.create();
    }

    private void initDayList(View parent) {
        week = new CheckedTextView[DAYS_OF_WEEK];
        week[0] = (CheckedTextView) parent.findViewById(R.id.monday);
        week[1] = (CheckedTextView) parent.findViewById(R.id.tuesday);
        week[2] = (CheckedTextView) parent.findViewById(R.id.wednesday);
        week[3] = (CheckedTextView) parent.findViewById(R.id.thursday);
        week[4] = (CheckedTextView) parent.findViewById(R.id.friday);
        week[5] = (CheckedTextView) parent.findViewById(R.id.saturday);
        week[6] = (CheckedTextView) parent.findViewById(R.id.sunday);
        for (CheckedTextView textView : week) {
            textView.setOnClickListener(this);
        }
    }

    private void initOption(View parent) {
        radioGroup = ((RadioGroup) parent.findViewById(R.id.opt_group));
        radioGroup.setOnCheckedChangeListener(this);

        // DO NOT use radioGroup.getChildAt(i) to ensure the order
        radioButtons = new RadioButton[3];
        radioButtons[0] = (RadioButton) parent.findViewById(R.id.opt_everyday);
        radioButtons[1] = (RadioButton) parent.findViewById(R.id.opt_weekdays);
        radioButtons[2] = (RadioButton) parent.findViewById(R.id.opt_never);

        refreshOptions();
    }


    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monday: {
                repeat ^= ScheduleEvent.MASK_MONDAY;
                break;
            }
            case R.id.tuesday: {
                repeat ^= ScheduleEvent.MASK_TUESDAY;
                break;
            }
            case R.id.wednesday: {
                repeat ^= ScheduleEvent.MASK_WEDNESDAY;
                break;
            }
            case R.id.thursday: {
                repeat ^= ScheduleEvent.MASK_THURSDAY;
                break;
            }
            case R.id.friday: {
                repeat ^= ScheduleEvent.MASK_FRIDAY;
                break;
            }
            case R.id.saturday: {
                repeat ^= ScheduleEvent.MASK_SATURDAY;
                break;
            }
            case R.id.sunday: {
                repeat ^= ScheduleEvent.MASK_SUNDAY;
                break;
            }
            case R.id.ok: {
                callback.onRepeatChanged(repeat);
                dismiss();
                break;
            }
            case R.id.cancel: {
                dismiss();
                break;
            }
            default:
                break;
        }

        refreshOptions();
        Logger.d(LOG_TAG, "onClick repeat = " + repeat);
    }

    /**
     * {@link RadioButton#setChecked(boolean)} might not trigger
     * {@link RadioGroup.OnCheckedChangeListener#onCheckedChanged}<br/>
     * update the value of {@link #repeat} and call {@link #refreshDayList()} to fix this bug
     */
    private void refreshOptions() {
        switch (repeat) {
            case ScheduleEvent.REPEAT_EVERYDAY:
                radioButtons[0].setChecked(true);
                repeat = ScheduleEvent.REPEAT_EVERYDAY;
                break;
            case ScheduleEvent.REPEAT_WEEKDAY:
                Logger.d(LOG_TAG, "set weekday");
                radioButtons[1].setChecked(true);
                repeat = ScheduleEvent.REPEAT_WEEKDAY;
                break;
            case ScheduleEvent.REPEAT_NEVER:
                radioButtons[2].setChecked(true);
                repeat = ScheduleEvent.REPEAT_NEVER;
                break;
            default:
                Logger.d(LOG_TAG, "set opt empty");
                radioGroup.check(-1);
                break;
        }

        refreshDayList();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.opt_everyday: {
                if (radioButtons[0].isChecked())
                    repeat = ScheduleEvent.REPEAT_EVERYDAY;
                break;
            }
            case R.id.opt_weekdays: {
                if (radioButtons[1].isChecked())
                    repeat = ScheduleEvent.REPEAT_WEEKDAY;
                break;
            }
            case R.id.opt_never: {
                if (radioButtons[2].isChecked())
                    repeat = ScheduleEvent.REPEAT_NEVER;
                break;
            }
            default:
                break;
        }

        refreshDayList();
    }

    private void refreshDayList() {
        int day = 1;
        for (int i = 0; i < 7; ++i) {
            if ((repeat & day) != 0)
                week[i].setChecked(true);
            else
                week[i].setChecked(false);

            day <<= 1;
        }
    }




    public void setRepeatChangeListener(OnRepeatChangeListener listener) {
        this.callback = listener;
    }
    
    public interface OnRepeatChangeListener {
        void onRepeatChanged(int repeat);
    }
}
