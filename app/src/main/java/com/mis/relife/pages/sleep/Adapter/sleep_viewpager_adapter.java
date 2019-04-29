package com.mis.relife.pages.sleep.Adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class sleep_viewpager_adapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;
    private List<String> tabs;

    public sleep_viewpager_adapter(FragmentManager fm, List<Fragment> fragments, List<String> tabs) {
        super(fm);
        this.fragments = fragments;
        this.tabs = tabs;
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
        return POSITION_NONE;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
//        super.restoreState(state, loader);
    }
}