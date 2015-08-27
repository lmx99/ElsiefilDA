package com.boshu.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.boshu.image.ImageDowloader;
import com.boshu.utils.Model;
import com.lifeisle.android.R;

import java.util.List;

/**
 * Created by amou on 25/8/2015.
 */
public class ActivityPictureShow extends Activity{
    private ImageView img_show;
    private ProgressBar progressbar;
    private TextView tv_page;

    private int currentPosition;
    private float pointX;
    private float currentX;
    private final int MAXDISTANCE = 50;
    private int position;
    private  List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boshu_pcitureshow);
        initView();

    }
    private void initView() {

        img_show = (ImageView) findViewById(R.id.img_show);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        tv_page= (TextView) this.findViewById(R.id.page);
        position=this.getIntent().getIntExtra("position", -2);
        list=this.getIntent().getStringArrayListExtra("list");
        currentPosition=position;
        loadData(currentPosition);
    }
    private void loadData(int position) {
        progressbar.setVisibility(View.VISIBLE);
        int numPage=list.size();
        int numPosition=position+1;
        tv_page.setText(numPosition+"/"+numPage);
        ImageDowloader dowloader=new ImageDowloader(this);
        dowloader.downloadImageNew(480, 480, list.get(position) + Model.APPNAME, new ImageDowloader.OnImageDownloadListener() {
            @Override
            public void onImageDownload(String url, Bitmap bitmap) {
                if (bitmap != null) {
                    img_show.setImageBitmap(bitmap);
                    progressbar.setVisibility(View.GONE);

                } else {
                    img_show.setImageResource(R.drawable.default_show_picture);
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
               pointX= event.getX();
                currentX=pointX;
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float x = currentX - pointX;
                if(x < -MAXDISTANCE){
                    //下一张
                    if(currentPosition == (list.size()-1)){
                        Toast.makeText(this, "当前是最后一张", 0).show();
                    }else{
                        currentPosition++;
                        this.loadData(currentPosition);

                    }
                }
                else if(x > MAXDISTANCE){
                    //上一张
                    if(currentPosition == 0){
                        Toast.makeText(this, "当前是第一张", 0).show();
                    }else{

                        currentPosition--;
                        loadData(currentPosition);
                    }
                }
                break;
        }
        return  true;
    }
    public void back(View view){
        finish();
    }
}
