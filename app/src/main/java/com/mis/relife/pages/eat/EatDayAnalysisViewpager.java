package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Diet;
import com.mis.relife.pages.sport.SportData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
    private String date_format;
    private eat_week_gridview week_adapter = null;
    private EatData eatData;
    //介面區
    private Button btDatepicker;
    private GridView gv_menu;
    private TextView tvCalAll;
    private TextView tvCalLoss;

    //圖表區
    private PieChart pieChart = null;
    //圖表資料區
    private int mYear,mMonth,mDay;
    private int nowmYear,nowmMonth,nowmDay;
    private List<PieEntry> entries = new ArrayList<>();
    private List<Integer> color = new ArrayList<>();
    //每餐總卡路里初始化
    private float breakfastCalTotal = 0;
    private float lunchCalTotal = 0;
    private float dinnerCalTotal = 0;
    private float nightSnackCalTotal = 0;
    private float snackCalTotal = 0;
    private float Totalcal = 0;
    private float lossTotalCal = 0;
    //每餐%數宣告
    private float breakfastPercent;
    private float lunchPercent;
    private float dinnerPercent;
    private float nightSnackPercent;
    private float snackPercent;
    private SportData sportData;

    private int first = 0;
    public EatDayAnalysisViewpager(EatData eatData,SportData sportData) {
        // Required empty public constructor
        this.eatData = eatData;
        this.sportData = sportData;
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
        View view = inflater.inflate(R.layout.eat_day_analysis_viewpager_fragment,container,false);
        gv_menu = view.findViewById(R.id.gv_data2);
        pieChart = view.findViewById(R.id.pie_chart);
        btDatepicker = view.findViewById(R.id.bt_datepicker);
        tvCalAll = view.findViewById(R.id.tv_cal_all_num);
        tvCalLoss = view.findViewById(R.id.tv_cal_num);
        //取得現在日期
        if(first == 0){
            nowdate();
            date_format = setDateFormat(mYear,mMonth,mDay);
            btDatepicker.setText("今天");
            first++;
        }
        date_format = setDateFormat(mYear,mMonth,mDay);
        btDatepicker.setText("今天");
        btDatepicker.setOnClickListener(datepicker);

        //圖表區
        myInit();
        //底下gridview區
        week_adapter = new eat_week_gridview(getActivity().getApplicationContext(),data,cal,percent);
        gv_menu.setAdapter(week_adapter);

        return view;
    }

    // 從資料庫取資料，這個會持續監聽新的資料，每當有資料改變，就會執行這個區塊
    private void myInit(){
        AppDbHelper.getAllDietFromFireBase(new MyCallBack<Map<String, Diet>>() {
            @Override
            public void onCallback(Map<String, Diet> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                if(!value.isEmpty()) {
                    //繪製圖表
                    if (pieChart != null) {
                        pieChart.clear();
                    }
                    getPiePercentData();
                    pie_info();
                    pie_initial();

                    tvCalAll.setText(String.valueOf((int)Totalcal) + "cal");
                    tvCalLoss.setText(String.valueOf(lossTotalCal - Totalcal));
                    //底下gridview區
                    inidata();
                    if(week_adapter != null) {
                        week_adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    //得到圖表資料
    private void getPiePercentData(){
        //初始化每餐卡路里總量
        breakfastCalTotal = 0;
        lunchCalTotal = 0;
        dinnerCalTotal = 0;
        nightSnackCalTotal = 0;
        snackCalTotal = 0;
        Totalcal = 0;
        lossTotalCal = 0;
        //跑迴圈抓出當日每一餐的總卡路里
        for(int i = 0;i < eatData.eatDate.size();i++){
            //比對到日期後 比對是哪一餐
            if(date_format.equals(eatData.eatDate.get(i))){
                //比對完哪一餐後進行卡路里加總
                mealCalTotal(i);
            }
        }
        //跑迴圈抓出當日運動消耗的總卡路里
        for(int l = 0;l < sportData.sport_recordDate.size();l++){
            //比對到日期後做加總
            if(date_format.equals(sportData.sport_recordDate.get(l))){
                lossTotalCal += Integer.valueOf(sportData.sport_cal.get(l));
            }
        }
        Totalcal = breakfastCalTotal + lunchCalTotal + dinnerCalTotal + nightSnackCalTotal + snackCalTotal;
        getPercent(Totalcal);

    }

    //算出每一餐的總卡路里
    private void mealCalTotal(int i){
        if(eatData.category.get(i) == 1){
            breakfastCalTotal = getCalTotal(breakfastCalTotal,i);
        }
        else if(eatData.category.get(i) == 2){
            lunchCalTotal = getCalTotal(lunchCalTotal,i);
        }
        else if(eatData.category.get(i) == 3){
            dinnerCalTotal = getCalTotal(dinnerCalTotal,i);
        }
        else if(eatData.category.get(i) == 4){
            nightSnackCalTotal = getCalTotal(nightSnackCalTotal,i);
        }
        else if(eatData.category.get(i) == 5){
            snackCalTotal = getCalTotal(snackCalTotal,i);
        }
    }


    //得到每一餐的%數
    private void getPercent(float totalcal){
        breakfastPercent =  breakfastCalTotal / totalcal * 100;
        lunchPercent = lunchCalTotal / totalcal * 100;
        dinnerPercent = dinnerCalTotal / totalcal * 100;
        nightSnackPercent = nightSnackCalTotal / totalcal * 100;
        snackPercent = snackCalTotal / totalcal * 100;
       // System.out.println("!!!!!!!!!!!!!!!!!!早餐" + breakfastPercent);
    }

    //卡路里加總
    private float getCalTotal(float calTotal,int datePosition){
        if(eatData.foods!=null){
            for(int l = 0;l < eatData.foods.get(datePosition).size();l++){
                calTotal += eatData.foods.get(datePosition).get(l).cal * eatData.foods.get(datePosition).get(l).number;
                System.out.println("!!!!!!!!!!!cal"+eatData.foods.get(datePosition).get(l).cal);
            }
        }else calTotal=0;
        return  calTotal;
    }

    //設置圖表資訊
    private void pie_info(){
        entries.clear();
        pieDeleteZero(breakfastPercent, "早餐",Color.rgb(250,128,114));
        pieDeleteZero(lunchPercent, "午餐",Color.rgb(144,238,144));
        pieDeleteZero(dinnerPercent, "晚餐",Color.rgb(255,182,193));
        pieDeleteZero(nightSnackPercent, "宵夜",Color.rgb(176,196,222));
        pieDeleteZero(snackPercent, "點心",Color.rgb(222,184,135));
    }

    //%數是零的就不加
    private void pieDeleteZero(float percent,String meal,int rgb){
        if(percent == 0 ){
        }
        else {
            entries.add(new PieEntry((int)percent,meal));
            color.add(rgb);
        }
    }

    //初始化圖表
    private void pie_initial(){
        //設置圖表資料
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        //設置圖表外觀
        set.setColors(color);
        set.setSliceSpace(5f);
        set.setValueTextSize(14f);
        set.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setFormSize(18);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void inidata(){
        data.clear();
        percent.clear();
        cal.clear();
        data.add("早餐");
        data.add("午餐");
        data.add("晚餐");
        data.add("宵夜");
        data.add("點心");
        percent.add((int) (breakfastPercent + 0.5));
        percent.add((int)(lunchPercent+ 0.5));
        percent.add((int)(dinnerPercent + 0.5));
        percent.add((int)(nightSnackPercent+ 0.5));
        percent.add((int)(snackPercent+ 0.5));
        cal.add((int)breakfastCalTotal);
        cal.add((int)lunchCalTotal);
        cal.add((int)dinnerCalTotal);
        cal.add((int)nightSnackCalTotal);
        cal.add((int)snackCalTotal);
    }

    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    date_format = setDateFormat(year,month,day);
                    if(nowmYear == year && nowmMonth == month && nowmDay == day)
                        btDatepicker.setText("今天");
                    else if(nowmYear == year && nowmMonth == month && nowmDay - 1 == day)
                        btDatepicker.setText("昨天");
                    else btDatepicker.setText(date_format);
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                    myInit();

                }

            }, mYear,mMonth, mDay).show();
        }
    };

    //設定日期格式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        String month,day;
        //判斷如果是個位數 日期前面要加個零
        if(monthOfYear / 10 == 0){
            month = "0" + String.valueOf(monthOfYear + 1);
        }
        else {
            month = String.valueOf(monthOfYear + 1);
        }
        if (dayOfMonth / 10 == 0){
            day = "0" + String.valueOf(dayOfMonth);
        }
        else {
            day = String.valueOf(dayOfMonth);
        }
        //設定日期格式
        return String.valueOf(year) + "/"
                + month + "/"
                + day;
    }

        //取得現在時間
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        nowmYear = now.get(Calendar.YEAR);
        nowmMonth = now.get(Calendar.MONTH);
        nowmDay = now.get(Calendar.DAY_OF_MONTH);
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
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
