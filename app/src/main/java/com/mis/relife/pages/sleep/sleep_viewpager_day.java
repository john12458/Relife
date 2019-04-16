package com.mis.relife.pages.sleep;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.pages.sport.recylerview_sportpage_adapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("ValidFragment")
public class sleep_viewpager_day extends Fragment {

    private PieChart pieChart;
    private Button bt_datepick;
    private  TextView tv_sleep_clock_time;
    private  TextView tv_sleep_length;
    private  EditText et_dream;

    Context context;
    private  int mYear,mMonth,mDay;
    private  int ch_year,ch_month,ch_day;
    private  List<PieEntry> entries = new ArrayList<>();
    private  float sleep_percent;
    private  float clear_percent;
    private  int have = 0;

    public sleep_viewpager_day(Context context){
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_viewpager_day_analysis, container, false);

            pieChart = view.findViewById(R.id.pie_chart);
            bt_datepick = view.findViewById(R.id.bt_datepicker);
            tv_sleep_clock_time = view.findViewById(R.id.tv_sleep_clock_time);
            tv_sleep_length = view.findViewById(R.id.tv_sleep_clock_time_num);
            et_dream = view.findViewById(R.id.et_sleep_dream);
            et_dream.setSaveEnabled(false);

            nowdate();
            bt_datepick.setText("今天");
            bt_datepick.setOnClickListener(datepicker);
            if (pieChart != null) {
                pieChart.clear();
            }
            get_pie_percent(mDay);
            if (have == 1) {
                pie_info();
                pie_initial();
            }

            return view;

    }


    //取得圖表的percent
    private void get_pie_percent(int day){
        tv_sleep_clock_time.setText("");
        tv_sleep_length.setText("");
        et_dream.setText("");
        String go_bed_time,get_up_time;
        have = 0;
        int bed_hour = 0,bed_min = 0,get_hour = 0,get_min = 0;
        float sleephour = 0,sleepmin = 0,sleeptotal = 0,cleartotal = 0;
        //走遍睡眠日記
        for(int i = 0;i < recylerview_sleep_adapter.day.size();i++){
            //如果有找到符合的天 就取出他的就寢時間和起床時間
            if(day == Integer.valueOf(recylerview_sleep_adapter.day.get(i))){
                go_bed_time = recylerview_sleep_adapter.go_bed_time.get(i);
                bed_hour = Integer.valueOf(go_bed_time.substring(0,2));
                if(bed_hour == 0){bed_hour = 24;}
                bed_min = Integer.valueOf(go_bed_time.substring(3,5));

                get_up_time = recylerview_sleep_adapter.get_up_time.get(i);
                get_hour = Integer.valueOf(get_up_time.substring(0,2));
                if(get_hour == 0){get_hour = 24;}
                get_min = Integer.valueOf(get_up_time.substring(3,5));
                tv_sleep_clock_time.setText(go_bed_time + "~" + get_up_time);
                String dream = recylerview_sleep_adapter.content.get(i);
                et_dream.setText(dream);
                have = 1;
                break;
            }
        }
        if(have == 1) {
            int today = 0;
            //判斷是否睡覺和起床都是在同一天
            for(int i = bed_hour;i <= 24;i++){
                if(i == get_hour){
                    today = 1;
                    break;
                }
            }
            //不是同一天 起床時間的小時就要加24
            if(today == 0) {
                if (bed_min > get_min) {
                    get_hour = get_hour - 1;
                    sleepmin = get_min + 60 - bed_min;
                    sleephour = get_hour + 24 - bed_hour;
                } else if (get_min >= bed_min) {
                    sleepmin = get_min - bed_min;
                    sleephour = get_hour + 24 - bed_hour;
                }
            }
            //同一天的話就不用加24
            else if(today == 1){
                if (bed_min > get_min) {
                    get_hour = get_hour - 1;
                    sleepmin = get_min + 60 - bed_min;
                    sleephour = get_hour - bed_hour;
                } else if (get_min >= bed_min) {
                    sleepmin = get_min - bed_min;
                    sleephour = get_hour - bed_hour;
                }
            }
            tv_sleep_length.setText(String.valueOf((int)sleephour) + "時" + String.valueOf((int)sleepmin) + "分");
            sleeptotal = sleephour + sleepmin / 24;
            cleartotal = 24 - sleeptotal;
            sleep_percent = sleeptotal / 24 * 100;
            clear_percent = cleartotal / 24 * 100;
        }

    }
    //設置圖表資訊
    private void pie_info(){
        entries.clear();
        entries.add(new PieEntry(sleep_percent, "睡眠"));
        entries.add(new PieEntry(clear_percent, "清醒"));
    }

    //初始化圖表
    private void pie_initial(){
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        set.setColors(new int[] {Color.rgb(250,250,210),Color.rgb(222,184,135)});
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

    //設置日期按鈕
    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String format = setDateFormat(year,month,day);
                    ch_year = year;
                    ch_month = month;
                    ch_day = day;
                    ch_day = day;

                    //日期變換時 會把圖表重整
                    pieChart.clear();
                    get_pie_percent(day);
                    if(have == 1) {
                        pie_info();
                        pie_initial();
                    }

                    if(mYear == year && mMonth == month && mDay == day) {
                        bt_datepick.setText("今天");
                    }
                    else if(mYear == year && mMonth == month && mDay - 1 == day){
                        bt_datepick.setText("昨天");
                    }
                    else {
                        bt_datepick.setText(format);
                    }
                    recylerview_sportpage_adapter.inidata_sport_diary("300");
                }

            }, mYear,mMonth, mDay).show();
        }
    };

//    private Button.OnClickListener date_left_right = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//        }
//    };

    //設置日期格式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "/"
                + String.valueOf(monthOfYear + 1) + "/"
                + String.valueOf(dayOfMonth);
    }

    //取得現在時間
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
        ch_day = mDay;
        ch_month = mMonth;
        ch_year = mYear;
    }
}
