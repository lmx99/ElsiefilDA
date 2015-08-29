package com.lifeisle.jekton.job;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lifeisle.android.R;
import com.lifeisle.jekton.job.adapter.SubordinateListAdapter;
import com.lifeisle.jekton.job.adapter.SubordinatePeriodsAdapter;
import com.lifeisle.jekton.job.data.bean.SubordinateItem;

/**
 * @author Jekton
 */
public class SubordinateActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subordinate);

        Intent intent = getIntent();
        String subordinateString = intent.getStringExtra(SubordinateListAdapter.EXTRA_SUBORDINATE);
        Gson gson = new Gson();
        SubordinateItem item = gson.fromJson(subordinateString, SubordinateItem.class);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(title.getText().toString() + item.title);

        TextView address = (TextView) findViewById(R.id.address);
        address.setText(address.getText().toString() + item.address);

        ListView listView = (ListView) findViewById(R.id.periods);
        BaseAdapter adapter = new SubordinatePeriodsAdapter(this, item);
        listView.setAdapter(adapter);
    }
}
