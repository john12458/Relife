package com.mis.relife.pages.sleep.viewPager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Info;
import com.mis.relife.data.model.Sleep;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekFragment extends Fragment implements Button.OnClickListener {

//    private int create = 0;
//    private LinearLayout test1,test2,test3;

    Context context;
    private TextView tv_sleep_hour_average,tv_go_bed_average,tv_get_up_average,tvTalk;
    private ImageView iv_talk_pet,iv_talk_place;
    public AnimationDrawable anim;
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

    private Calendar nowCalendar;
    private Date pickDate;
    private SimpleDateFormat sdf;
    //圖表外觀資料區
    private  String[] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private  int[] colors = {Color.rgb(178,34,34),Color.rgb(255,69,0)
    ,Color.rgb(240,230,140),Color.rgb(60,179,113),Color.rgb(176,196,222)
    ,Color.rgb(30,144,255),Color.rgb(147,112,219)};

    private  int have = 0;
    private SimpleDateFormat timeSdf;
    private SimpleDateFormat dateSdf;
    private HashMap<String, Sleep> sleepList;
    private ArrayList<Sleep> weekSleeps;
    private Button bt_datepicker;
    private float sleepAvg = 0;
    private int goBedHour =0;
    private int wakeUpHour =0;
    private int old = 27;

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
        tvTalk = view.findViewById(R.id.tv_talk);
        iv_talk_pet = view.findViewById(R.id.iv_talk_pet);
        iv_talk_place = view.findViewById(R.id.iv_talk_place);

        Picasso
                .with(context)
                .load(R.drawable.blackboard)
                .into(iv_talk_place);

        nowCalendar = Calendar.getInstance();
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        timeSdf= new SimpleDateFormat("HH:mm");
        dateSdf= new SimpleDateFormat("yyyy/MM/dd");
        bt_datepicker.setOnClickListener(this);
        setBarchar();
        getDatabase();
        return view;
    }
    private void getDatabase(){
        pickDate = new Date();
      AppDbHelper.getAllSleepFromFireBase(new MyCallBack<Map<String, Sleep>>() {
          @Override
          public void onCallback(Map<String, Sleep> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
              sleepList = new HashMap<>();
              sleepList.putAll(value);
              onDataChange(pickDate);
              AppDbHelper.getAllInfoFromFireBase(new MyCallBack<Info>() {
                  @Override
                  public void onCallback(Info value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                      old = value.old;
                  }
              });
              iv_talk_pet.setImageResource(R.drawable.anim_teach);
              anim = (AnimationDrawable) iv_talk_pet.getDrawable();
              //setTalkText();
              anim.start();
          }
      });
    }

    private void setTalkText(){
        if(old > 13 && old < 29) {
            if (goBedHour > 0 && goBedHour < 4 && sleepAvg < 8) {
                tvTalk.setText("晚上十點至半夜兩點是生長激素分泌最旺盛的黃金時段喔!!\n" + "每天要睡8小時左右喔~");
            } else if (goBedHour > 0 && goBedHour < 4 && sleepAvg > 8) {
                tvTalk.setText("晚上十點至半夜兩點是生長激素分泌最旺盛的黃金時段喔!!");
            } else if (sleepAvg < 8 && sleepAvg > 0) {
                tvTalk.setText("每天要睡8小時左右喔~");
            } else if(sleepAvg == 0){
                tvTalk.setText("沒有資料喔~");
            }else if(sleepAvg > 8){
                tvTalk.setText("睡太多搂~睡太多的人容易感到昏沉、疲倦喔");
            } else {
                tvTalk.setText("睡的很好喔~~");
            }
        }
        else if(old >= 29 && old < 60){
            if (goBedHour > 0 && goBedHour < 3 && sleepAvg < 7) {
                tvTalk.setText("晚上十點至半夜兩點是生長激素分泌最旺盛的黃金時段喔!!\n" + "成年男子需要6.49小時睡眠時間,婦女需要7.5小時左右喔");
            } else if (goBedHour > 0 && goBedHour < 3 && sleepAvg > 8) {
                tvTalk.setText("晚上十點至半夜兩點是生長激素分泌最旺盛的黃金時段喔!!");
            } else if (sleepAvg < 7 && sleepAvg > 0) {
                tvTalk.setText("成年男子需要6.49小時睡眠時間,婦女需要7.5小時左右喔");
            } else if(sleepAvg == 0){
                tvTalk.setText("沒有資料喔~");
            }
            else if(sleepAvg > 8){
                tvTalk.setText("睡太多搂~睡太多的人容易感到昏沉、疲倦喔");
            }else {
                tvTalk.setText("昨天睡的怎麼樣呢~~");
            }
        }
        else {

        }
    }

    public void onDataChange(Date pDate){
        boolean weekSleepsIsNull = true;
         List<Sleep> weekSleeps = new ArrayList<>();
         while(weekSleeps.size()<7) weekSleeps.add(null); //初始化weekSleeps
         List<Date>  weekDateList = getWeekDateList(pDate);

        for (String key : sleepList.keySet()) { //走遍睡眠
            Sleep value = sleepList.get(key);
            for (int i =0;i<weekDateList.size();i++) {
                Date wDate = weekDateList.get(i);
                if (dateSdf.format(wDate).equals(value.recordDate)) { //找到選擇的日期就更新畫面
                    weekSleeps.set(i,value);
                    weekSleepsIsNull =false;
                }
            }
        }
        if(weekSleepsIsNull) {
            try { updateViewWithNoData(weekSleeps); }
            catch (ParseException e) { e.printStackTrace(); }
        }
        else{
            try { updateView(weekSleeps); }
            catch (ParseException e) { e.printStackTrace(); }
        }

    }
    private List<Date> getWeekDateList(Date date){
        List weekDateList = new ArrayList();
        Calendar weekCal = Calendar.getInstance();
        //先加Monday 進去
        weekCal.setTime(getThisWeekMonday(date));
        weekDateList.add(weekCal.getTime());
        //再加 Tue. - Sun.
        for (int i =0;i<6;i++){
            weekCal.add(weekCal.DATE,1);
            weekDateList.add(weekCal.getTime());
        }
        return weekDateList;
    }
    private void updateViewWithNoData(List<Sleep> weekSleeps) throws ParseException {
        barDataResume(weekSleeps);
        tv_go_bed_average.setText("");
        tv_sleep_hour_average.setText("");
        tv_get_up_average.setText("");
        setTalkText();
    }
    private void updateView(List<Sleep> weekSleeps) throws ParseException { // 畫面更新
        calculateAvg(weekSleeps);
        barDataResume(weekSleeps);
    }
    private void barDataResume(List<Sleep> weekSleeps) throws ParseException {
        barChart.clear();
        List<BarEntry> entryList = new ArrayList<>();
        int i=0;
        for (Sleep value:weekSleeps){
            BarEntry barEntry = new BarEntry(i++,0);
            if(value!=null){
                Date sleepDate = sdf.parse(value.sleepTime);
                Date wakeDate = sdf.parse(value.wakeTime);
                barEntry.setY(betweenHour(sleepDate,wakeDate));
            }
            entryList.add(barEntry);
        }
        //將資料放進去
        BarDataSet barset = new BarDataSet(entryList,"");

        barset.setColors(colors);
//        barset.setDrawValues(false);
        barset.setValueTextColor(Color.WHITE);
        barset.setBarShadowColor(Color.GRAY);

        List<IBarDataSet> datasets = new ArrayList<>();
        datasets.add(barset);
        barChart.setData(new BarData(barset));
        setBarchar();
    }
    private void calculateAvg(List<Sleep> weekSleeps) throws ParseException {
        sleepAvg =0;
        goBedHour =0;
        int goBedMin=0;
        wakeUpHour =0;
        int wakeUpMin=0;
        int cnt =0;
        Calendar sleepCal = Calendar.getInstance();
        Calendar wakeCal = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (Sleep value:weekSleeps) {
            if(value!=null){
                Date sleepDate = sdf.parse(value.sleepTime);
                Date wakeDate = sdf.parse(value.wakeTime);
                float betweenHour = betweenHour(sleepDate,wakeDate);
                sleepCal.setTime(sleepDate);
                wakeCal.setTime(wakeDate);

                sleepAvg += betweenHour;

                goBedHour +=sleepCal.get(Calendar.HOUR_OF_DAY);
                goBedMin += sleepCal.get(Calendar.MINUTE);

                wakeUpHour += wakeCal.get(Calendar.HOUR_OF_DAY);
                wakeUpMin += wakeCal.get(Calendar.MINUTE);

                cnt ++;
            }
        }

        sleepAvg /= cnt;
        goBedHour = (goBedHour+goBedMin/60)/cnt;
        goBedMin  = (goBedMin%60)/cnt;

        wakeUpHour = (wakeUpHour+wakeUpMin/60)/cnt;
        wakeUpMin = (wakeUpMin%60)/cnt;

        tv_go_bed_average.setText(String.format("%02d", goBedHour) + ":" +  String.format("%02d", goBedMin));
        tv_sleep_hour_average.setText(String.format("%.1f", sleepAvg) + "小時");
        tv_get_up_average.setText(String.format("%02d", wakeUpHour) + ":" + String.format("%02d", wakeUpMin));
        setTalkText();
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

        /***XY軸的設置***/
        //X軸設置顯示位置在底部
        xAxis = barChart.getXAxis();
        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        //不顯示XY軸的線條
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(x_value);
        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setTextColor(Color.WHITE);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
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
    // 兩個Date的相差小時
    private float betweenHour(Date date1, Date date2){
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒數
        long nh = 1000 * 60 * 60;// 一小時的毫秒數
        int day = 0;
        int hour = 0;
        Calendar sleepDate = Calendar.getInstance();
        Calendar wakeDate = Calendar.getInstance();
        sleepDate.setTime(date1);
        wakeDate.setTime(date2);
        if(sleepDate.get(Calendar.HOUR_OF_DAY) > wakeDate.get(Calendar.HOUR_OF_DAY)){
            wakeDate.add(Calendar.DAY_OF_MONTH,1);
        }
        else {

        }
        date1 = sleepDate.getTime();
        date2 = wakeDate.getTime();
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        long diff = Math.abs(time2-time1);
        try {
            day = (int)(diff / nd);// 計算差多少天
            hour =  (int)(diff % nd /nh + day*24);// 計算差多少小時
            hour = ((int) (hour*100))/100; //取小數點第二位
        }catch (Exception e){
            e.printStackTrace();
        }
        return hour;
    }

    @Override
    public void onClick(View v) {
        Calendar pickCalendar = Calendar.getInstance();
        pickCalendar.setTime(pickDate);
        int pickYear = pickCalendar.get(Calendar.YEAR);
        int pickMonth = pickCalendar.get(Calendar.MONTH);
        int pickDay = pickCalendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                int nowYear = nowCalendar.get(Calendar.YEAR);
                int nowMonth = nowCalendar.get(Calendar.MONTH);
                int nowDay = nowCalendar.get(Calendar.DAY_OF_MONTH);
                Calendar pickCalendar = Calendar.getInstance();
                pickCalendar.set(year,month,day);
                pickDate = pickCalendar.getTime();
                Log.d("dateSdf_pickDate",dateSdf.format(pickDate));
                //日期變換時 資料重整
                onDataChange(pickDate);
                if(nowYear == year && nowMonth == month && nowDay == day)
                    bt_datepicker.setText("今天");
                else if(nowYear == year && nowMonth == month && nowDay - 1 == day)
                    bt_datepicker.setText("昨天");
                else bt_datepicker.setText(dateSdf.format(pickDate));
            }
        },pickYear,pickMonth,pickDay).show();
    }
}
