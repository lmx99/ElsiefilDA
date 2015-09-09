package com.boshu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boshu.domain.User;
import com.boshu.image.ImageDowloader;
import com.boshu.utils.Model;
import com.lifeisle.android.R;

import java.util.List;

/**
 * Created by amou on 7/9/2015.
 */
public class Adapter_Boshu_AddContacht extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<User> list;
    private Context context;
    private ImageDowloader dowloader;
    private ListView listView;
    private boolean isFirstEnter = true;
    private int mFirstVisibleItem;
    private int mVisibleItemCount;

    public Adapter_Boshu_AddContacht(Context context,List<User> list,ListView listView){
        this.listView=listView;
        this.list=list;
        this.context=context;
       dowloader =new ImageDowloader(context);
        listView.setOnScrollListener(this);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_boshu_findconstact, null);
        }
        viewHolder holder= (viewHolder) convertView.getTag(R.id.addContactList);
        if(holder==null){
            holder=new viewHolder();
            holder.img_head= (ImageView) convertView.findViewById(R.id.head_img);
            holder.img_sex= (ImageView) convertView.findViewById(R.id.img_sex);
            holder.tv_nickName= (TextView) convertView.findViewById(R.id.find_nickname);
            holder.tv_hight= (TextView) convertView.findViewById(R.id.tv_find_hight);
            holder.tv_school= (TextView) convertView.findViewById(R.id.tv_find_school);
            convertView.setTag(R.id.addContactList,holder);
        }
        holder.tv_nickName.setText(list.get(position).getUserName());
        holder.tv_hight.setText(list.get(position).getHight());
        holder.tv_school.setText(list.get(position).getSchool());
        if(list.get(position).getSex().equals("0")){
            holder.img_sex.setImageResource(R.drawable.friend_woman);
        }else{
            holder.img_sex.setImageResource(R.drawable.friend_man);
        }
        String url= Model.PitureLoad + list.get(position).getHeadImage();
        holder.img_head.setTag(url);
        Bitmap bitmap=null;
        bitmap=dowloader.showCacheBitmap(url.replaceAll(
                "[^\\w]", ""));
        if(bitmap==null){
            holder.img_head.setImageResource(R.drawable.default_avatar);

        }else{
            holder.img_head.setImageBitmap(bitmap);
        }
       bitmap=dowloader.downloadImage(45, 45,url,new ImageDowloader.OnImageDownloadListener() {
            @Override
            public void onImageDownload(String url, Bitmap bitmap) {
                ImageView img= (ImageView) listView.findViewWithTag(url);
                if(img!=null&&bitmap!=null){
                    img.setImageBitmap(bitmap);

                }
                  // img.setImageResource(R.drawable.default_avatar);


            }
        });
        if(bitmap==null){
            holder.img_head.setImageResource(R.drawable.default_avatar);
        }
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            showImage(mFirstVisibleItem, mVisibleItemCount);
        } else {
            cancellTask();
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mFirstVisibleItem = firstVisibleItem;
        mVisibleItemCount = visibleItemCount;
        if (isFirstEnter && visibleItemCount > 0) {
            showImage(firstVisibleItem, visibleItemCount);
            isFirstEnter = false;
        }
    }

    public static class viewHolder{
        ImageView img_head;
        ImageView img_sex;
        TextView  tv_nickName;
        TextView  tv_hight;
        TextView  tv_school;
    }
    public void cancellTask() {
        dowloader.canoellTask();
    }
    private void showImage(int firstVisibleItem, int visibleItemCount) {
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
           String mImageUrl =Model.PitureLoad+list.get(i).getHeadImage();
            // 得到标记的item
            Log.i("bb",mImageUrl);
            dowloader.downloadImage(480, 480, mImageUrl, new ImageDowloader.OnImageDownloadListener() {
                @Override
                public void onImageDownload(String url, Bitmap bitmap) {
                   ImageView imageView= (ImageView) listView.findViewWithTag(url);
                    if(imageView!=null&&bitmap!=null){
                        imageView.setImageBitmap(bitmap);

                    }
                }
            });
          /*  mImageDownloader.downloadImage(mImageUrl,
                    new ImageDowloader.OnImageDownloadListener() {
                        @Override
                        public void onImageDownload(String url, Bitmap bitmap) {
                            // TODO Auto-generated method stub
                            if (mImageView != null && bitmap != null) {
                                //改了这里
                                *//** 在设置图片的时候判断ImageView的tag是不是一致，
                                 * 只有一致的时候才显示该图片。其实这个步骤应该放在图片下载完后执行的。
                                 * 图片的设置也不放在这里，因为将图片设置放在这里，
                                 * 及时从缓存中加载图片也要判断tag，没必要。*//*
                                if (currentUrl == ensureUrl) {
                                    mImageView.setImageBitmap(bitmap);
                                }
                            }
                        }
                    });*/
        }
    }
}
