package com.mis.relife.pages.sport;

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
public class sport_tab_viewpager extends Fragment {

    private TabLayout tb_content;
    public ViewPager vp_content;
    private List<Fragment> sport_fragments = new ArrayList<>();
    private List<String> sport_tabs = new ArrayList<>();
    private FragmentManager fm;
    public static sport_tab_viewpager_adapter sport_adapter = null;
    Context context;

    public sport_tab_viewpager(Context context,FragmentManager fm){
        this.context = context;
        this.fm = fm;
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
        vp_content.setAdapter(sport_adapter);
        tb_content.setupWithViewPager(vp_content);
        vp_content.setOffscreenPageLimit(3);

        return view;
    }

    private void initData_sleep_tablayout() {
        sport_tabs.clear();
        sport_fragments.clear();
        sport_tabs.add("運動紀錄");
        sport_tabs.add("每週分析");
        sport_fragments.add(new sport_page_activity(context,fm));
        sport_fragments.add(new sport_week_analysis_viewpager_fragment(context));
        sport_adapter = new sport_tab_viewpager_adapter(fm,sport_fragments,sport_tabs);
    }
}
