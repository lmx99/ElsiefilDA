package com.lifeisle.jekton.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lifeisle.jekton.fragment.ChatsFragment;
import com.lifeisle.jekton.fragment.DiscoverFragment;
import com.lifeisle.jekton.fragment.LogisticsFragment;
import com.lifeisle.jekton.fragment.MeFragment;


/**
 * @author Jekton Luo
 * @version 0.01 6/21/2015.
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private int count;
    private Fragment[] fragments;

    public TabPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.count = count;
        fragments = new Fragment[count];
    }


    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = new LogisticsFragment();
                    break;
                case 1:
                    fragments[position] = new ChatsFragment();
                    break;
                case 2:
                    fragments[position] = new DiscoverFragment();
                    break;
                case 3:
                    fragments[position] = new MeFragment();
                    break;
                default:
                    throw new RuntimeException("Error page count");
            }
        }

        return fragments[position];
    }


    @Override
    public int getCount() {
        return count;
    }
}
