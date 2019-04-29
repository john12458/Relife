package com.mis.relife.pages.eat.EatViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.mis.relife.R;
import com.mis.relife.pages.eat.Adapter.eat_week_gridview;
import com.mis.relife.pages.eat.EatData;

import java.util.ArrayList;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link EatDayAnalysisViewpager.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link EatDayAnalysisViewpager#newInstance} factory method to
// * create an instance of this fragment.
// */
@SuppressLint("ValidFragment")
public class EatDayAnalysisViewpager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    //有用的地方
    private List<String> data = new ArrayList<>();
    private List<Integer> percent = new ArrayList<>();
    private List<Integer> cal = new ArrayList<>();
    private eat_week_gridview week_adapter;
    private GridView gv_menu;
    private EatData eatData;

    public EatDayAnalysisViewpager(EatData eatData) {
        // Required empty public constructor
        this.eatData = eatData;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eat_week_analysis_viewpager_fragment,container,false);
        gv_menu = view.findViewById(R.id.gv_data2);
        inidata();
        week_adapter = new eat_week_gridview(getActivity().getApplicationContext(),data,cal,percent);
        gv_menu.setAdapter(week_adapter);
        return view;
    }

    private void inidata(){
        data.add("早餐");
        data.add("午餐");
        data.add("晚餐");
        data.add("宵夜");
        data.add("點心");
        percent.add(20);
        percent.add(20);
        percent.add(20);
        percent.add(20);
        percent.add(20);
        cal.add(200);
        cal.add(200);
        cal.add(200);
        cal.add(200);
        cal.add(200);
    }


    //無用的地方

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EatDayAnalysisViewpager.
     */
//    // TODO: Rename and change types and number of parameters
//    public static EatDayAnalysisViewpager newInstance(String param1, String param2) {
//        EatDayAnalysisViewpager fragment = new EatDayAnalysisViewpager();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }


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
