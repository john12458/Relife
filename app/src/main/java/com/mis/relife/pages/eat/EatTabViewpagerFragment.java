package com.mis.relife.pages.eat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mis.relife.R;
import com.mis.relife.pages.eat.EatData;
import com.mis.relife.pages.eat.EatDayAnalysisViewpager;
import com.mis.relife.pages.eat.EatWeekAnalysisViewpager;
import com.mis.relife.pages.eat.eat_page_activity;
import com.mis.relife.pages.eat.eat_viewpager_adapter;
import com.mis.relife.pages.sport.SportData;

import java.util.ArrayList;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link EatTabViewpagerFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link EatTabViewpagerFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class EatTabViewpagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    //開始有用的地方
    private TabLayout tb_content;
    public ViewPager vp_content;
    private List<Fragment> eat_fragments = new ArrayList<>();
    private List<String> eat_tabs = new ArrayList<>();
    private FragmentManager fm;
    public eat_viewpager_adapter eat_adapter = null;
    private EatData eatData;
    private SportData sportData;



    public EatTabViewpagerFragment() {
        // Required empty public constructor
        eatData = new EatData();
        sportData = new SportData();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EatTabViewpagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EatTabViewpagerFragment newInstance(String param1, String param2) {
        EatTabViewpagerFragment fragment = new EatTabViewpagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eat_fragment_tab_viewpager,container,false);
        fm = getChildFragmentManager();
        System.out.println("tab initial!!!!!!!!!!!!!");
        initData_sleep_tablayout();
        tb_content = view.findViewById(R.id.tb_content);
        vp_content = view.findViewById(R.id.vp_content);
        tb_content.setTabMode(TabLayout.MODE_FIXED);
        vp_content.setAdapter(eat_adapter);
        tb_content.setupWithViewPager(vp_content);
        vp_content.setOffscreenPageLimit(3);

        return view;
    }

    private void initData_sleep_tablayout() {
        eat_tabs.clear();
        eat_fragments.clear();
        eat_tabs.add("飲食紀錄");
        eat_tabs.add("每日分析");
        eat_tabs.add("每週分析");
        eat_fragments.add(new eat_page_activity(getContext(),sportData));
        eat_fragments.add(new EatDayAnalysisViewpager(eatData,sportData, getContext()));
        eat_fragments.add(new EatWeekAnalysisViewpager(eatData,sportData,getContext()));
        eat_adapter = new eat_viewpager_adapter(fm,eat_fragments,eat_tabs);
    }

    //沒有用到的地方

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
