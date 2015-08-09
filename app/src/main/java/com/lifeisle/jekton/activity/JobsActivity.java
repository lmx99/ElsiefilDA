package com.lifeisle.jekton.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.model.JobsModel;
import com.lifeisle.jekton.ui.adapter.JobListAdapter;


/**
 * @author Jekton
 * @version 0.01 7/31/2015
 */
public class JobsActivity extends AppCompatActivity {

    private JobsModel jobsModel;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupations);

        listView = (ListView) findViewById(R.id.occupationList);
        jobsModel = new JobsModel(this);
        listView.setClickable(false);
    }


    /**
     * Callback of {@link JobsModel}
     */
    public void onDataLoaded() {
        JobListAdapter adapter = new JobListAdapter(this, jobsModel);
        listView.setAdapter(adapter);
    }
}
