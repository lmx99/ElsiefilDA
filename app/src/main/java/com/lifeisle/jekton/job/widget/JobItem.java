package com.lifeisle.jekton.job.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lifeisle.android.R;
import com.lifeisle.jekton.job.JobsModel;
import com.lifeisle.jekton.job.data.bean.JobInfo;
import com.lifeisle.jekton.util.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class JobItem extends FrameLayout implements View.OnClickListener {

    private JobsModel jobsModel;
    private JobInfo jobInfo;

    public JobItem(Context context, JobsModel jobsModel) {
        super(context);

        this.jobsModel = jobsModel;

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.widget_occupation_item, this, true);

        findViewById(R.id.apply).setOnClickListener(this);
        findViewById(R.id.details).setOnClickListener(this);

        setClickable(true);
    }


//    public OccupationItem(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    public OccupationItem(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }






    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
        initView();
    }

    private void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(StringUtils.getServerBase() + jobInfo.imageUrl, imageView);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(jobInfo.title);

        TextView briefIntro = (TextView) findViewById(R.id.brief);
        briefIntro.setText(jobInfo.briefIntro);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply:
                jobsModel.apply(jobInfo.reId);
                break;
            case R.id.details:
                jobsModel.details(StringUtils.getServerBase() + jobInfo.url);
                break;
        }
    }
}
