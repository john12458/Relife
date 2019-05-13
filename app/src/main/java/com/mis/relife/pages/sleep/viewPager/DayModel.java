package com.mis.relife.pages.sleep.viewPager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.util.Log;
import android.widget.DatePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Info;
import com.mis.relife.data.model.Sleep;
import com.mis.relife.databinding.SleepDayFragmentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DayModel {
    private final SimpleDateFormat sdf;
    private final SimpleDateFormat timeSdf;
    private final SimpleDateFormat dateSdf;
    private final Calendar nowCalendar;
    public Date pickDate;
    private int old = 27;
    private final SleepDayFragmentBinding binding;
    private final MyPieChart myPieChart;
    private Context context;
    public Map<String,Sleep> sleepList;
    public final ObservableField<Sleep> sleep = new ObservableField<>();
    public final ObservableField<String> sleepTime = new ObservableField<>();
    public final ObservableField<String> date = new ObservableField<>();
    public final ObservableField<String> sleepClockTime = new ObservableField<>();
    public final ObservableField<String> sleepTimeBetween = new ObservableField<>();
    public final ObservableField<Float> sleepPercent = new ObservableField<>();
    public final ObservableField<Float> wakePercent = new ObservableField<>();

    public DayModel(Context context, SleepDayFragmentBinding binding) {
        this.context = context;
        this.binding = binding;
        this.myPieChart= new MyPieChart(this,binding.pieChart);
        this.sdf= new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.timeSdf= new SimpleDateFormat("HH:mm");
        this.dateSdf= new SimpleDateFormat("yyyy/MM/dd");
        this.nowCalendar = Calendar.getInstance();
        this.pickDate = nowCalendar.getTime();
        myInit();
    }
    public void myInit() {
        date.set("今天");
        // 從資料庫取資料，這個會持續監聽新的資料，每當有資料改變，就會執行這個區塊
        AppDbHelper.getAllSleepFromFireBase(new MyCallBack<Map<String, Sleep>>() {
            @Override
            public void onCallback(Map<String, Sleep> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                if(!value.isEmpty()){
                    sleepList = new HashMap<>();
                    sleepList.putAll(value);
                    onDataChange(pickDate);
                }
            }
        });
    }
    public void onDatePick(){
        Calendar pickCalendar = Calendar.getInstance();
        pickCalendar.setTime(pickDate);
        int pickYear = pickCalendar.get(Calendar.YEAR);
        int pickMonth = pickCalendar.get(Calendar.MONTH);
        int pickDay = pickCalendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
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
                        date.set("今天");
                else if(nowYear == year && nowMonth == month && nowDay - 1 == day)
                        date.set("昨天");
                else date.set(dateSdf.format(pickDate));
            }
        },pickYear,pickMonth,pickDay).show();
    }

    public void onDataChange(Date date){
        if(sleepList != null){
            for (String key : sleepList.keySet()) { //走遍睡眠
                Sleep value = sleepList.get(key);
                if (dateSdf.format(date).equals(value.recordDate)) { //找到選擇的日期就更新畫面
                    try { updateView(value); }
                    catch (ParseException e) { e.printStackTrace(); }
                    return;
                }
            }
        }
        updateViewWithNoData();
    }
    private  void updateViewWithNoData(){
        sleepClockTime.set("今天還沒紀錄喔");
        sleepTimeBetween.set("");
        sleepPercent.set((float)0);
        wakePercent.set((float)0);
        initText(0);
        sleep.set(null);
        myPieChart.resume();// 更新pieChart
    }
    private void updateView(Sleep value) throws ParseException { // 畫面更新
            sleep.set(value);
            Date sleepDate = sdf.parse(value.sleepTime);
            Date wakeDate = sdf.parse(value.wakeTime);
            sleepClockTime.set(timeSdf.format(sleepDate) + "~" + timeSdf.format(wakeDate));
            // 算好相差時間後執行接下來的動作
            betweenTime(sleepDate, wakeDate, new BtTimeCallback() {
                @Override
                public void onCallback(String message, int day, int hour, int min) {
                    sleepTimeBetween.set(message);
                    float sleepTotal = hour + min / 60;
                    System.out.println("!!!!!!!!!!!!!!!!!!" + sleepTotal);
                    AppDbHelper.getAllInfoFromFireBase(new MyCallBack<Info>() {
                        @Override
                        public void onCallback(Info value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                            old = value.old;
                        }
                    });
                    initText(sleepTotal);
                    float clearTotal = 24 - sleepTotal;
                    sleepPercent.set(sleepTotal / 24 * 100);
                    wakePercent.set(clearTotal / 24 * 100);
                }
            });
            myPieChart.resume(); // 更新pieChart
    }

    private void initText(float sleeTime){
        if(old > 13 && old < 29) {
            if (sleeTime == 0) {
                sleepTime.set("嗨~~~");
            } else if (sleeTime >= 8 && sleeTime <= 9) {
                sleepTime.set("睡眠充足~讚讚");
            } else if(sleeTime < 8){
                sleepTime.set("要睡飽一點喔~我會擔心~ \n" + "睡太少時，饑餓素可能會增加，使人更容易感覺饑餓喔");
            }
            else if(sleeTime > 9){
                sleepTime.set("睡太多搂~小豬\n" + "睡太多的人容易感到昏沉、疲倦喔");
            }
        }
        else if(old >= 29 && old < 60){
            if (sleeTime == 0) {
                sleepTime.set("嗨~~~");
            } else if (sleeTime > 7 && sleeTime < 8) {
                sleepTime.set("睡眠充足~讚讚");
            } else if(sleeTime < 7){
                sleepTime.set("要睡飽一點喔~我會擔心~\n" + "睡太少時，饑餓素可能會增加，使人更容易感覺饑餓喔");
            }
            else if(sleeTime > 9){
                sleepTime.set("睡太多搂~小豬 \n" + "睡太多的人容易感到昏沉、疲倦喔");
            }
        }
    }

    // 兩個Date的相差時間
    private void betweenTime(Date date1, Date date2, BtTimeCallback btTimeCallback){
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒數
        long nh = 1000 * 60 * 60;// 一小時的毫秒數
        long nm = 1000 * 60;// 一分鐘的毫秒數
        int day = 0;
        int hour = 0;
        int min = 0;
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
        String txt= "";
        try {
            day = (int)(diff / nd);// 計算差多少天
            hour =  (int)(diff % nd /nh );// 計算差多少小時
            min =  (int)(diff % nd % nh/ nm);// 計算差多少分鐘

            if(day!=0)txt += String.valueOf(day)+"天";
            if(hour!=0)txt += String.valueOf(hour)+"時";
            if(min!=0)txt += String.valueOf(min)+"分";
        }catch (Exception e){
            e.printStackTrace();
        }
        btTimeCallback.onCallback(txt,day,hour,min); // 算好的結果放入callback，讓大家自由運用這些值
    }

}
