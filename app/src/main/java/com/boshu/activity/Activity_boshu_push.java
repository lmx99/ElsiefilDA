package com.boshu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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


        List<Push> list = new ArrayList<Push>();
        for(int i=0;i<10;i++){
            Push push= new Push();

            int a=i%2;
            if(a==0){
                push.setType("0");
            }else{
                push.setType("1");
            }
            list.add(push);


        }
        Adapter_Bosh_Push adapter=new Adapter_Bosh_Push(this,list,mListView);
         mListView.setAdapter(adapter);

    }
    public void back(View view){
        finish();
    }
}
