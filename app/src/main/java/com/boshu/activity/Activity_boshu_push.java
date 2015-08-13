package com.boshu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.boshu.adapter.Adapter_Bosh_Push;
import com.boshu.domain.Push;
import com.lifeisle.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amou on 9/8/2015.
 */
public class Activity_boshu_push extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_push);
        ListView mListView = (ListView) findViewById(R.id.m_listView);
        Push push= new Push();
        List<Push> list = new ArrayList<Push>();
        list.add(push);
        list.add(push);
        Adapter_Bosh_Push adapter=new Adapter_Bosh_Push(this,list,mListView);
         mListView.setAdapter(adapter);
    }
}
