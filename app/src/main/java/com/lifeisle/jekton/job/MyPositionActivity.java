package com.lifeisle.jekton.job;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.job.adapter.SubordinateListAdapter;
import com.lifeisle.jekton.job.widget.FlowLayout;

/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class MyPositionActivity extends AppCompatActivity {

    private FlowLayout peerGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job);

        peerGroup = (FlowLayout) findViewById(R.id.peers);

        String[] peers = { " aa ", " bb ", " cc ", " aa ", " bb ", " cc ", " aa ", " bb ", " cc ",
                " aa ", " bb ", " cc ",  " aa ", " bb ", " cc ", " aa ", " bb ", " cc ",
                " aa ", " bb ", " cc ", " aa ", " bb ", " cc ", " aa ", " bb ", " cc ",
                " aa ", " bb ", " cc ", " aa ", " bb ", " cc ", " aa ", " bb ", " cc ",
        };

        for (String peer : peers) {
            TextView textView = new TextView(this);
            textView.setText(peer);
            peerGroup.addView(textView);
        }


        ListView listView = (ListView) findViewById(R.id.subordinateList);
        listView.setAdapter(new SubordinateListAdapter(this));

    }
}
