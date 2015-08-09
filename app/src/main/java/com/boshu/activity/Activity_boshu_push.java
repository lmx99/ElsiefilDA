package com.boshu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.lifeisle.android.R;

/**
 * Created by amou on 9/8/2015.
 */
public class Activity_boshu_push extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_boshu_push);
       ListView mListView=(ListView)findViewById(R.id.m_listView);

      //  mListView.setAdapter(mAdapter);
    }
}
