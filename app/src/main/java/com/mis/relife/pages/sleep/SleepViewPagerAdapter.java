package com.mis.relife.pages.sleep;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class SleepViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private final String[] tabs={"睡眠日記","每日分析","每週分析"};

    public SleepViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        System.out.println("哈哈哈哈!!!!!!!!!!!!!!!!!!");
        return POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
//        super.restoreState(state, loader);
    }
}