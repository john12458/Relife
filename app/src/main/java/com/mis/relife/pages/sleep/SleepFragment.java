package com.mis.relife.pages.sleep;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.day.DayFragment;
import com.mis.relife.pages.sleep.diary.DiaryFragment;
import com.mis.relife.pages.sleep.week.WeekFragment;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class SleepFragment extends Fragment{


    private TabLayout tb_content;
    public ViewPager vp_content;
    private List<Fragment> sleep_fragments;
    private SleepViewPagerAdapter sleepViewPagerAdapter;
    private DiaryFragment diaryFragment;
    private DayFragment dayFragment;
    private WeekFragment weekFragment;

    public SleepFragment(){
        initInstanceObject();
    }

    private void initInstanceObject(){
        sleep_fragments = new ArrayList<Fragment>();
        diaryFragment = new DiaryFragment();
        dayFragment = new DayFragment();
        weekFragment = new WeekFragment();
        sleep_fragments.add(diaryFragment);
        sleep_fragments.add(dayFragment);
        sleep_fragments.add(weekFragment);
        sleepViewPagerAdapter = new SleepViewPagerAdapter(getChildFragmentManager(),sleep_fragments);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_fragment,container,false);
        tb_content = view.findViewById(R.id.tb_content);
        vp_content = view.findViewById(R.id.vp_content);
        tb_content.setTabMode(TabLayout.MODE_FIXED);
        vp_content.setAdapter(sleepViewPagerAdapter);
        tb_content.setupWithViewPager(vp_content);
        vp_content.setOffscreenPageLimit(3);
        return view;
    }




}
