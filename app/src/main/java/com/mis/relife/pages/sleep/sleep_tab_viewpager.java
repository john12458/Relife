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

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class sleep_tab_viewpager extends Fragment{


    private TabLayout tb_content;
    public ViewPager vp_content;
    private List<Fragment> sleep_fragments = new ArrayList<>();
    private List<String> sleep_tabs = new ArrayList<>();
    private FragmentManager fm;
    public static sleep_viewpager_adapter sleep_adapter = null;
    Context context;


    static int i = 0;

    public sleep_tab_viewpager(Context context){
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_tab_viewpager,container,false);
        fm = getChildFragmentManager();
        initData_sleep_tablayout();
        tb_content = view.findViewById(R.id.tb_content);
        vp_content = view.findViewById(R.id.vp_content);
        tb_content.setTabMode(TabLayout.MODE_FIXED);
        vp_content.setAdapter(sleep_adapter);
        tb_content.setupWithViewPager(vp_content);
        vp_content.setOffscreenPageLimit(3);

        return view;
    }


    private void initData_sleep_tablayout() {
        sleep_tabs.clear();
        sleep_fragments.clear();
        sleep_tabs.add("睡眠日記");
        sleep_tabs.add("每日分析");
        sleep_tabs.add("每週分析");
        sleep_fragments.add(new sleep_viewpager_diary(context,fm));
        sleep_fragments.add(new sleep_viewpager_day(context));
        sleep_fragments.add(new sleep_viewpager_week(context));
        sleep_adapter = new sleep_viewpager_adapter(fm,sleep_fragments,sleep_tabs);
    }



}
