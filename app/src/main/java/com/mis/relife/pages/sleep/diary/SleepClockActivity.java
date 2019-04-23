package com.mis.relife.pages.sleep.diary;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.Calendar;

public class SleepClockActivity extends Activity {


    private Long StartTime,hours,minius,seconds;
    private Handler handler = new Handler();
    private TextView tv_clock;
    private Button bt_back;
    private DiaryFragment diary;
    private String go_bed_time,get_up_time;
    int hour ,minute;
    int bool = 0;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_sleep__clock_);
        String go_bed_hour = "",go_bed_minute = "";
        tv_clock = findViewById(R.id.tv_clock);
        bt_back = findViewById(R.id.bt_back);
        c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        if(String.valueOf(hour).length() == 1){
            go_bed_hour = "0" + String.valueOf(hour);
        }
        else{
            go_bed_hour = String.valueOf(hour);
        }
        if(String.valueOf(minute).length() == 1){
            go_bed_minute = "0" + String.valueOf(minute);
        }
        else {
            go_bed_minute = String.valueOf(minute);
        }
        go_bed_time = go_bed_hour+":"+ go_bed_minute;
        StartTime = System.currentTimeMillis();
        handler.removeCallbacks(updateTimer);
        //設定Delay的時間
        handler.postDelayed(updateTimer, 1000);
        bt_back.setOnClickListener(back);
    }

    private Button.OnClickListener back = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            String get_up_hour = "",get_up_minute = "";
            Intent back_intent = new Intent();
            Calendar back = Calendar.getInstance();
            int back_hour = back.get(Calendar.HOUR);
            int back_minute = back.get(Calendar.MINUTE);
            if(String.valueOf(back_hour).length() == 1){
                get_up_hour = "0" + String.valueOf(back_hour);
            }
            else {
                get_up_hour = String.valueOf(back_hour);
            }
            if(String.valueOf(back_minute).length() == 1){
                get_up_minute = "0" + String.valueOf(back_minute);
            }
            else {
                get_up_minute = String.valueOf(back_minute);
            }
            get_up_time = get_up_hour + ":" + get_up_minute ;
            back_intent.setClass(SleepClockActivity.this, DiaryEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("bool",bool);
            bundle.putString("bed",go_bed_time);
            bundle.putString("get",get_up_time);
            back_intent.putExtras(bundle);
            startActivity(back_intent);
            finish();
        }
    };

    private Runnable updateTimer = new Runnable() {
        public void run() {
            Long spentTime = System.currentTimeMillis() - StartTime;
            //計算目前已過分鐘數
            hours = (spentTime/1000)/3600;
            minius = (spentTime/1000)/60;
            //計算目前已過秒數
            seconds = (spentTime/1000) % 60;
            tv_clock.setText(hours+":"+minius+":"+seconds);
            handler.postDelayed(this, 1000);
        }
    };
}
