package com.mis.relife.pages.eat.EatViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;

import com.mis.relife.R;
import com.mis.relife.pages.eat.EatData;
import com.mis.relife.pages.eat.eat_new_activity;
import com.mis.relife.pages.eat.Adapter.eat_page_gridview_button;
import com.mis.relife.pages.eat.Adapter.eat_page_gridview_record;

import java.util.Calendar;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link EatRecordViewpager.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link EatRecordViewpager#newInstance} factory method to
// * create an instance of this fragment.
// */
@SuppressLint("ValidFragment")
public class EatRecordViewpager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    //有用的地方
    private Context context;
    private GridView gv_data_record,gv_data_button;
    public Button bt_datepicker,bt_week,bt_day;
    private eat_page_gridview_record eat_gridview_record_adapter;
    private eat_page_gridview_button eat_gridview_button_adapter;
    private int mYear,mMonth,mDay;
    public String date;
    private EatData eatData;

    public EatRecordViewpager(EatData eatData) {
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
        View view = inflater.inflate(R.layout.eat_record_viewpager_fragment,container,false);
        gv_data_record = view.findViewById(R.id.gv_data1);
        gv_data_button = view.findViewById(R.id.gv_data2);
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        bt_datepicker.setOnClickListener(datepicker);
        nowdate();

        date = setDateFormat(mYear,mMonth,mDay);
        bt_datepicker.setText(date);

        eat_gridview_record_adapter = new eat_page_gridview_record(getContext());
        eat_gridview_button_adapter = new eat_page_gridview_button(getContext());
        gv_data_record.setAdapter(eat_gridview_record_adapter);
        gv_data_button.setAdapter(eat_gridview_button_adapter);

        gv_data_button.setOnItemClickListener(gv_eat);
        return view;
    }

    private AdapterView.OnItemClickListener gv_eat = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position != 5) {
                Intent intent_eat_new = new Intent();
                intent_eat_new.setClass(getContext(), eat_new_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eat_name", eat_gridview_button_adapter.menu[position]);
                intent_eat_new.putExtras(bundle);
                startActivity(intent_eat_new);
            }
        }
    };

    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String format = setDateFormat(year,month,day);
                    bt_datepicker.setText(format);
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }



    //沒有用得地方


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EatRecordViewpager.
     */
    // TODO: Rename and change types and number of parameters
//    public static EatRecordViewpager newInstance(String param1, String param2) {
//        EatRecordViewpager fragment = new EatRecordViewpager();
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
