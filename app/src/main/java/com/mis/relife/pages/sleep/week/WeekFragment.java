package com.mis.relife.pages.sleep.week;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mis.relife.R;
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
import com.mis.relife.pages.sleep.diary.RecylerviewSleepAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class WeekFragment extends Fragment {

//    private int create = 0;
//    private LinearLayout test1,test2,test3;

    Context context;
    private static TextView tv_sleep_hour_average,tv_go_bed_average,tv_get_up_average;
    //圖表資料區
    private BarChart barChart;
    private  XAxis xAxis;
    private  YAxis leftAxis;
    private  YAxis rightAxis;
    static BarData barData;
    //計算資料區
    private  int mYear,mMonth,mDay,mway;
    private  int ch_year,ch_month,ch_day;
    private  float user_input_cnt = 0;
    private  float sleep_average = 0;
    private  float go_bed_average = 0f,get_up_average = 0f;

    private Date date;
    private SimpleDateFormat sdf;
    //圖表外觀資料區
    private  String[] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private  int[] colors = {Color.rgb(178,34,34),Color.rgb(255,69,0)
    ,Color.rgb(240,230,140),Color.rgb(60,179,113),Color.rgb(176,196,222)
    ,Color.rgb(30,144,255),Color.rgb(147,112,219)};

    private  int have = 0;

    public WeekFragment(Context context){
        this.context = context;
    }

    public WeekFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_viewpager_week_analysis,container,false);

            barChart = view.findViewById(R.id.bar_chart);
            tv_sleep_hour_average = view.findViewById(R.id.tv_sleep_time);
            tv_go_bed_average = view.findViewById(R.id.tv_sleep_bed);
            tv_get_up_average = view.findViewById(R.id.tv_sleep_get);


            sdf = new SimpleDateFormat("yyyy-MM-dd");
            //拿到星期一的day
            get_monday();

            //設置圖表
            barData = getBarData();
            barChart.setData(barData);
            setBarchar();
//        }
        return view;
    }

    //拿到monday的day
    private void get_monday(){
        nowdate();
        try {
            date = sdf.parse(String.valueOf(mYear) + "-" + String.valueOf(mMonth) + "-" + String.valueOf(mDay));
            date = getThisWeekMonday(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
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

        /***XY軸的設置***/
        //X軸設置顯示位置在底部
        xAxis = barChart.getXAxis();
        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        //不顯示XY軸的線條
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(x_value);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }

    //拿到圖表資料
    private BarData getBarData(){
        user_input_cnt = 0;
        sleep_average = 0;
        go_bed_average = 0;
        get_up_average = 0;

        int maxX = 7;
        List<BarEntry> list = new ArrayList<>();
        String go_bed_time,get_up_time;
        have = 0;
        int bed_hour = 0,get_hour = 0;
        float sleephour = 0,sleepmin = 0,sleeptotal = 0,bed_min = 0,get_min = 0;
        int day = ch_day;
        for(int i = 0;i < maxX;i++){
            //先將X軸設好七天
            BarEntry barEntry = new BarEntry(i,0);
            //再來比對有對應到日記的日期
            for(int a = 0; a < RecylerviewSleepAdapter.day.size(); a++){
                int gv_day = Integer.valueOf(RecylerviewSleepAdapter.day.get(a));
                //有對應到就不會是0
                if(day == gv_day){
                    go_bed_time = RecylerviewSleepAdapter.go_bed_time.get(a);
                    bed_hour = Integer.valueOf(go_bed_time.substring(0,2));
                    if(bed_hour == 0){bed_hour = 24;}
                    bed_min = Integer.valueOf(go_bed_time.substring(3,5));
                    go_bed_average += bed_hour + bed_min/60;

                    get_up_time = RecylerviewSleepAdapter.get_up_time.get(a);
                    get_hour = Integer.valueOf(get_up_time.substring(0,2));
                    if(get_hour == 0){get_hour = 24;}
                    get_min = Integer.valueOf(get_up_time.substring(3,5));
                    get_up_average += get_hour + get_min / 60;

                    int today = 0;
                    //判斷是否睡覺和起床都是在同一天
                    for(int l = bed_hour;l <= 24;l++){
                        if(l == get_hour){
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
                    sleeptotal = sleephour + sleepmin / 24;
                    barEntry.setY(sleeptotal);
                    have = 1;
                    user_input_cnt++;
                    sleep_average += sleeptotal;
                    break;
                }
            }
            day++;
            list.add(barEntry);
        }
        //將資料放進去
        BarDataSet barset = new BarDataSet(list,"");

        barset.setColors(colors);
        barset.setDrawValues(false);
        barset.setBarShadowColor(Color.GRAY);

        List<IBarDataSet> datasets = new ArrayList<>();
        datasets.add(barset);

        BarData barData = new BarData(barset);

        sleep_average = sleep_average / user_input_cnt;
        go_bed_average = go_bed_average / user_input_cnt;
        get_up_average = get_up_average / user_input_cnt;
        int go_hour = (int)go_bed_average;
        int go_min = (int)((go_bed_average - go_hour) * 60);
        int up_hour = (int)get_up_average;
        int up_min = (int)((get_up_average - up_hour) * 60);
        if(go_min / 10 <= 0) {
            tv_go_bed_average.setText(String.valueOf(go_hour) + ":" + "0" + String.valueOf(go_min));
        }
        else{
            tv_go_bed_average.setText(String.valueOf(go_hour) + ":" + String.valueOf(go_min));
        }

        tv_sleep_hour_average.setText(String.format("%.1f", sleep_average) + "小時");

        if (up_min / 10 <= 0) {
            tv_get_up_average.setText(String.valueOf(up_hour) + ":" + "0" +String.valueOf(up_min));
        }
        else {
            tv_get_up_average.setText(String.valueOf(up_hour) + ":" + String.valueOf(up_min));
        }
        return barData;
    }



    //取得現在時間
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH) + 1;
        mDay = now.get(Calendar.DAY_OF_MONTH);
        mway = now.get(Calendar.DAY_OF_WEEK);
        ch_day = mDay;
        ch_month = mMonth;
        ch_year = mYear;
    }

    //計算星期一是幾號
    private Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
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
        ch_day = cal.get(Calendar.DAY_OF_MONTH);
        //System.out.println("DAY !!!!!!!!!!!!!!!!!!" + cal.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

}
