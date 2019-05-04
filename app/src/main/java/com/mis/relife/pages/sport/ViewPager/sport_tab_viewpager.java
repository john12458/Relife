package com.mis.relife.pages.sport.ViewPager;

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
import com.mis.relife.pages.sport.SportData;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class sport_tab_viewpager extends Fragment {

    private TabLayout tb_content;
    public ViewPager vp_content;
    private List<Fragment> sport_fragments = new ArrayList<>();
    private List<String> sport_tabs = new ArrayList<>();
    private FragmentManager fm;
    public sport_tab_viewpager_adapter sport_adapter = null;
    private SportData sportData;

    Context context;

    public sport_tab_viewpager(Context context,FragmentManager fm){
        this.context = context;
        this.fm = fm;
        sportData = new SportData();
    }

    private OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_tab_viewpager,container,false);
        fm = getChildFragmentManager();
        System.out.println("tab initial!!!!!!!!!!!!!");
        initData_sleep_tablayout();
        tb_content = view.findViewById(R.id.tb_content);
        vp_content = view.findViewById(R.id.vp_content);
        tb_content.setTabMode(TabLayout.MODE_FIXED);
        vp_content.setAdapter(sport_adapter);
        tb_content.setupWithViewPager(vp_content);
        vp_content.setOffscreenPageLimit(3);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void initData_sleep_tablayout() {
        sport_tabs.clear();
        sport_fragments.clear();
        sport_tabs.add("運動紀錄");
        sport_tabs.add("每週分析");
        sport_fragments.add(new sport_page_activity(context,fm,sportData));
        sport_fragments.add(new sport_week_analysis_viewpager_fragment(context,sportData));
        sport_adapter = new sport_tab_viewpager_adapter(fm,sport_fragments,sport_tabs);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public void onResume() {
        super.onResume();
//        sport_adapter.notifyDataSetChanged();
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
