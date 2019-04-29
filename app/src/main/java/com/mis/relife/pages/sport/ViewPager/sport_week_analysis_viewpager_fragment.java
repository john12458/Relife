package com.mis.relife.pages.sport.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mis.relife.R;
import com.mis.relife.pages.sport.SportData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link sport_week_analysis_viewpager_fragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link sport_week_analysis_viewpager_fragment#newInstance} factory method to
// * create an instance of this fragment.
// */
@SuppressLint("ValidFragment")
public class sport_week_analysis_viewpager_fragment extends Fragment {

    /*目前沒有用的參數
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //    private OnFragmentInteractionListener mListener;
     */


    Context context;
    private SportData sportData;
    private TextView tvSportAvgTime,tvSportAvgCal;
    private Button btPicker;

    //圖表資料區
    private BarChart barChart;
    private XAxis xAxis;
    private YAxis leftAxis;
    private  YAxis rightAxis;
    private BarData barData;

    private int mYear,mMonth,mDay,monday;
    //    private int bar_mYear,bar_mMonth,bar_mDay;
    private String date_format;
    float avgSportTime;
    float avgSportCal;
    private int week_sport_time,week_sport_time_cnt;

    //圖表外觀資料區
    private  String[] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private  int[] colors = {Color.rgb(178,34,34),Color.rgb(255,69,0)
            ,Color.rgb(240,230,140),Color.rgb(60,179,113),Color.rgb(176,196,222)
            ,Color.rgb(30,144,255),Color.rgb(147,112,219)};

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private String Monday,cal_date;
    private Date date,bar_date;
    private Calendar cal;

    private int first = 0;


    public sport_week_analysis_viewpager_fragment(Context context, SportData sportData) {
        this.context = context;
        this.sportData = sportData;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_week_analysis_viewpager_fragment,container,false);
        tvSportAvgTime = view.findViewById(R.id.tv_sport_time);
        tvSportAvgCal = view.findViewById(R.id.tv_sport_cal);
        barChart = view.findViewById(R.id.bar_chart);
        btPicker = view.findViewById(R.id.bt_datepicker);
        btPicker.setOnClickListener(datepicker);

        if(first == 0){
            nowdate();
            btPicker.setText(date_format);
            first++;
        }
        //拿到星期一的day
        get_monday();
        monday = cal.get(Calendar.DAY_OF_MONTH);

        //設置圖表
        week_sport_time = 0;
        week_sport_time_cnt = 0;
        barData = getBarData();
        barChart.setData(barData);
        setBarchar();

        //計算平均time 和 cal
        tvSportAvgTime.setText(String.format("%.1f", getAvgSportTime()) + "小時");
        tvSportAvgCal.setText(String.valueOf((int)getAvgSportCal()) + "卡路里");

        return view;
    }

    //拿到平均運動時間
    private float getAvgSportTime(){
        if(week_sport_time_cnt == 0){
            avgSportTime = 0;
            return avgSportTime;
        }
        else {
            avgSportTime = week_sport_time / 60 / week_sport_time_cnt;
            return avgSportTime;
        }


    }
    //拿到平均運動消耗卡路里
    private float getAvgSportCal(){
        if(week_sport_time_cnt == 0){
            avgSportCal = 0;
            return avgSportCal;
        }
        else {
            avgSportCal = week_sport_time / 24 / week_sport_time_cnt;
            return avgSportCal;
        }
    }

    //設定X軸的value
    private IAxisValueFormatter x_value = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            try{
                return week[(int) value];
            }
            catch (Exception e) {
                Log.d("ERROR TAG: " , "xAxis.setValueFormatter() -- " + e.toString());
                return "";
            }
        }
    };

    //設定圖表外觀
    private void setBarchar(){
        //不顯示圖表網格
        barChart.setDrawGridBackground(false);
        //不顯示description
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        //背景陰影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //顯示邊框
        barChart.setDrawBorders(false);

        barChart.animateY(2000);
        barChart.animateX(2000);

        barChart.setTouchEnabled(false);

        /***XY軸的設置***/
        xAxis = barChart.getXAxis();
        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        //不顯示XY軸的線條
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        //X軸設置顯示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        //設定X的值
        xAxis.setValueFormatter(x_value);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(0.5f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }

    //拿到圖表資料
    private BarData getBarData() {

        int maxX = 7;
        int have ;
        List<BarEntry> list = new ArrayList<>();
        float total_sporttime;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < maxX; i++) {
            //先將X軸設好七天
            BarEntry barEntry = new BarEntry(i, 0);
            //用來計算有哪幾天是有運動的
            have = 0;
            total_sporttime = 0;
            //再來比對有對應到日記的日期 從資料庫裡的 recordDate 進行比對
            for (int a = 0; a < sportData.sport_recordDate.size(); a++) {
                //將字串轉成date
                try {
                    bar_date =sdf.parse(sportData.sport_recordDate.get(a));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //用calendar 拿到資料庫 recordDate 的 day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(bar_date);
                int record_day = calendar.get(Calendar.DAY_OF_MONTH);
                //拿到後可以對應指定的day
                if (day == record_day) {
                    int sport_time = Integer.valueOf(sportData.sport_time.get(a));
                    total_sporttime += sport_time;
                    week_sport_time += sport_time;
                    if(have == 0) {
                        week_sport_time_cnt++;
                        have++;
                    }
                }
            }
            total_sporttime = total_sporttime / 60;
            System.out.println("total !!!!!!!!!!!!!!!!!!" + total_sporttime);
            barEntry.setY(total_sporttime);
            cal.add(Calendar.DAY_OF_MONTH,1);
            //System.out.println("BAR DAY !!!!!!!!!!!!!!!!!!" + cal.get(Calendar.DAY_OF_MONTH));
            day = cal.get(Calendar.DAY_OF_MONTH);
            list.add(barEntry);
        }
        //將資料放進去
        BarDataSet barset = new BarDataSet(list, "");

        barset.setColors(colors);
        barset.setDrawValues(false);
        barset.setBarShadowColor(Color.GRAY);

        List<IBarDataSet> datasets = new ArrayList<>();
        datasets.add(barset);

        BarData barData = new BarData(barset);
        return  barData;
    }

    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
        date_format = setDateFormat(mYear,mMonth,mDay);
    }

    //計算星期一是幾號
    private Date getThisWeekMonday(Date date) {
        cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        System.out.println("DAY !!!!!!!!!!!!!!!!!!" + cal.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    //拿到monday的day
    private void get_monday(){
        try {
            date = sdf.parse(date_format);
            date = getThisWeekMonday(date);
            Monday = sdf.format(date);
            System.out.println("Monday!!!!!!!!" + Monday);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    date_format = setDateFormat(year,month,day);
                    btPicker.setText(date_format);
                    get_monday();
                    System.out.println("monday!!!!!!!!" + monday);
                    if(cal.get(Calendar.DAY_OF_MONTH) != monday){
                        monday = cal.get(Calendar.DAY_OF_MONTH);
                        //設置圖表
                        week_sport_time = 0;
                        week_sport_time_cnt = 0;
                        barData = getBarData();
                        barChart.setData(barData);
                        setBarchar();

                        //計算平均time 和 cal
                        tvSportAvgTime.setText(String.format("%.1f", getAvgSportTime()) + "小時");
                        tvSportAvgCal.setText(String.valueOf((int)getAvgSportCal()) + "卡路里");
                        System.out.println("monday!!!!!!!!" + monday);
                    }
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    //設定日期格式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "/"
                + String.valueOf(monthOfYear + 1) + "/"
                + String.valueOf(dayOfMonth);
    }





    /*
    底下是目前沒有用的東西
     */


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    //    // TODO: Rename and change types and number of parameters
//    public static sport_week_analysis_viewpager_fragment newInstance(String param1, String param2) {
//        sport_week_analysis_viewpager_fragment fragment = new sport_week_analysis_viewpager_fragment(context);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
