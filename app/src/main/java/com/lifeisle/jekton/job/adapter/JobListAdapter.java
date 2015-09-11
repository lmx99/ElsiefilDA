package com.lifeisle.jekton.job.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lifeisle.jekton.job.JobsModel;
import com.lifeisle.jekton.job.data.bean.JobInfo;
import com.lifeisle.jekton.job.widget.JobItem;


/**
 * @author Jekton
 * @version 0.01 8/1/2015
 */
public class JobListAdapter extends BaseAdapter {

    private Context context;
    private JobsModel jobsModel;

    public JobListAdapter(Context context, JobsModel jobsModel) {
        this.context = context;
        this.jobsModel = jobsModel;
    }


    @Override
    public int getCount() {
        return jobsModel.size();
    }

    @Override
    public JobInfo getItem(int position) {
        return jobsModel.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JobItem item;
        if (convertView == null)
            item = new JobItem(context, jobsModel);
        else
            item = (JobItem) convertView;

        item.setJobInfo(getItem(position));

        return item;
    }


}
