package com.boshu.db;

import android.util.Log;

import com.boshu.domain.PushItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amou on 6/9/2015.
 */
public class PushDataHandle {
    public String PushDataHandle = "PushDataHandle";

    public List<List<PushItem>> changePush(List<PushItem> list) {
        List<List<PushItem>> lists = new ArrayList<List<PushItem>>();
        List<PushItem> list_contains = null;
        int i = -1;
        int x=0;
        for (PushItem item : list) {
            if (i != item.getS_item_id()) {
                if (i != -1) {
                    Log.e(PushDataHandle, "运行了吗");
                    lists.add(list_contains);
                }


                list_contains = new ArrayList<PushItem>();
                list_contains.add(item);
            } else {
                list_contains.add(item);

            }
            if(x==list.size()-1){
                lists.add(list_contains);
            }
            x++;
            i = item.getS_item_id();


        }
        return lists;
    }
}
