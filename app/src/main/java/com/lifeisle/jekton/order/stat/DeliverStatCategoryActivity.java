package com.lifeisle.jekton.order.stat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lifeisle.android.R;

/**
 * @author Jekton
 */
public class DeliverStatCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_stat_category);

        findViewById(R.id.deliver_stat).setOnClickListener(this);
        findViewById(R.id.motor_stat).setOnClickListener(this);
        findViewById(R.id.ganger_stat).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deliver_stat:
                startActivity(DeliverStatFragment.STAT_TYPE_DELIVER);
                break;
            case R.id.motor_stat:
                startActivity(DeliverStatFragment.STAT_TYPE_MOTOR);
                break;
            case R.id.ganger_stat:
                startActivity(DeliverStatFragment.STAT_TYPE_GANGER);
                break;
            default:
                break;
        }
    }

    private <T extends Activity> void startActivity(int type) {
        Intent intent = new Intent(this, DeliverStatActivity.class);
        intent.putExtra(DeliverStatFragment.EXTRA_STAT_TYPE, type);
        startActivity(intent);
    }
}
