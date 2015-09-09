package com.boshu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.boshu.adapter.Adapter_Boshu_Push;
import com.boshu.db.PushDataHandle;
import com.boshu.db.PushItemDao;
import com.boshu.domain.Push;
import com.boshu.domain.PushItem;
import com.lifeisle.android.R;

import java.util.List;

/**
 * Created by amou on 9/8/2015.
 */
public class Activity_boshu_push extends Activity {
    private String PushActivity = "PushActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_push);
        ListView mListView = (ListView) findViewById(R.id.m_listView);

     //   List<Push> list = new ArrayList<Push>();
        for (int i = 0; i < 10; i++) {
            Push push = new Push();

            int a = i % 2;
            if (a == 0) {
                push.setType("0");
            } else {
                push.setType("1");
            }
           // list.add(push);


        }
        PushItemDao dao = new PushItemDao(this);
        List<PushItem> list = dao.findAll("行政部");
        PushDataHandle dataHandle = new PushDataHandle();
        List<List<PushItem>> str_list = dataHandle.changePush(list);
        Adapter_Boshu_Push adapter = new Adapter_Boshu_Push(this, str_list, mListView);
        mListView.setAdapter(adapter);
        mListView.setSelection(mListView.getAdapter().getCount() - 1);
      //  testPush();

    }

    public void back(View view) {
        finish();
    }

    public void testPush() {
        PushItem item = new PushItem();
        item.setTopic("行政部美女多，下个星期一晚上七点狂魔乱舞晚会来袭！");
        item.setUserName("行政部");
        item.setType("1");
        item.setS_item_id(2);
        item.setL_item_id(3);
        item.setImg_url("http://www.baidu.com");
        item.setUrl("http://www.google.com");
        PushItemDao dao = new PushItemDao(this);
        for(int i=0;i<4;i++) {
            dao.addItemt(item);
        }
        List<PushItem> list = dao.findAll("行政部");
        PushDataHandle dataHandle = new PushDataHandle();
        List<List<PushItem>> str_list = dataHandle.changePush(list);
       List<PushItem> push_list=str_list.get(0);
        for(List<PushItem> li:str_list){
            for(PushItem item1:li){
                Log.i(PushActivity, item1.getTopic() + "");
            }
        }

    }
}
