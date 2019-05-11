package com.mis.relife.pages.sleep.viewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
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
import com.mis.relife.pages.sleep.Adapter.sleep_viewpager_adapter;
import com.mis.relife.pages.sleep.SleepData;

import java.util.ArrayList;
import java.util.List;



@SuppressLint("ValidFragment")
public class sleep_tab_viewpager extends Fragment{


    private TabLayout tb_content;
    public ViewPager vp_content;
    private List<Fragment> sleep_fragments = new ArrayList<>();
    private List<String> sleep_tabs = new ArrayList<>();
    private FragmentManager fm;
    public sleep_viewpager_adapter sleep_adapter = null;
    Context context;

    private OnFragmentInteractionListener mListener;
    private SleepData sleepData;


//    static int i = 0;

    public sleep_tab_viewpager(Context context){
        this.context = context;
        sleepData = new SleepData();
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
        vp_content.setOffscreenPageLimit(4);

        return view;
    }

    private void initData_sleep_tablayout() {
        sleep_tabs.clear();
        sleep_fragments.clear();
        sleep_tabs.add("睡眠日記");
        sleep_tabs.add("每日分析");
        sleep_tabs.add("每週分析");
        sleep_fragments.add(new sleep_viewpager_diary(context,sleepData));
        sleep_fragments.add(new DayFragment());
        sleep_fragments.add(new WeekFragment());
        sleep_adapter = new sleep_viewpager_adapter(fm,sleep_fragments,sleep_tabs);
    }

    @Override
    public void onResume() {
        super.onResume();
        //sleep_adapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
