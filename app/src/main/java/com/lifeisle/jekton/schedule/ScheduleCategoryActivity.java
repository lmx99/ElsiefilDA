package com.lifeisle.jekton.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lifeisle.android.R;


public class ScheduleCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_category);

        findViewById(R.id.schedule_table).setOnClickListener(this);
        findViewById(R.id.sync_courses).setOnClickListener(this);
        findViewById(R.id.add_schedule).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.schedule_table: {
                startActivity(ScheduleTableActivity.class);
                break;
            }
            case R.id.sync_courses: {
                break;
            }
            case R.id.add_schedule: {
                startActivity(ScheduleDetailActivity.class);
                break;
            }
            default:
                break;
        }
    }

    private <T extends Activity> void startActivity(Class<T> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
