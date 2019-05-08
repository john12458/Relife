package com.mis.relife.pages.sleep.New_Delete;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airsaid.pickerviewlibrary.TimePickerView;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Sleep;
import com.mis.relife.pages.sleep.Adapter.recylerview_sleep_adapter;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class sleep_plus extends AppCompatActivity {

    private TextView tv_date;
    private Button tv_finish;
    private Button bt_finish;
    public ImageButton ib_mood;
    private EditText et_go_bed_time;
    private EditText et_get_up_time;
    private EditText et_sleep_content;

    private Bundle bundle = null;

    private String go_ed_time = "";
    private String get_up_time = "";
    private String dream = "";
    private String date = "";
    public String mood = "firt4";
    private int bool = 0,position;
    private int mYear,mMonth,mDay;

    private String key = "";
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_sleep_plus);
        fm = getSupportFragmentManager();
        tv_date = findViewById(R.id.tv_date);
        tv_finish  =findViewById(R.id.tv_finish);
        bt_finish = findViewById(R.id.bt_finish);
        ib_mood = findViewById(R.id.ib_moood_image);
        et_go_bed_time = findViewById(R.id.et_go_bed_time);
        et_get_up_time = findViewById(R.id.et_get_up_time);
        et_sleep_content  =findViewById(R.id.et_sleep_content);
        et_go_bed_time.setInputType(InputType.TYPE_NULL);
        et_get_up_time.setInputType(InputType.TYPE_NULL);

        et_get_up_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker(et_get_up_time);
            }
        });

        et_go_bed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker(et_go_bed_time);
            }
        });
        ib_mood.setOnClickListener(ibMoodClick);

        //三種狀況
        //第一種 設定鬧鐘
        if( getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            bool = bundle.getInt("bool");
            if(bool == 0) {
                clock();
            }
            //第二種 修改資訊
            else if(bool == 1){
                edit_info();
            }
        }
        //第三種 直接新增日記
        else{
            plus_diary();
        }

        //設置date_picker方法
        tv_date.setOnClickListener(date_pick);

        tv_finish.setOnClickListener(finish);
        bt_finish.setOnClickListener(finish);
    }

    private Button.OnClickListener ibMoodClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SleepChooseMood chooseMood = new SleepChooseMood(sleep_plus.this,sleep_plus.this);
            chooseMood.show(fm,"choose");
        }
    };

    //三種傳值過來的情況
    //第一 設定鬧鐘
    private void clock(){
        go_ed_time = bundle.getString("bed");
        get_up_time = bundle.getString("get");
        et_go_bed_time.setText(go_ed_time);
        et_get_up_time.setText(get_up_time);

        nowdate();
        date = setDateFormat(mYear, mMonth, mDay);
        tv_date.setText(date);
    }
    //第二種 修改日記資訊
    private void edit_info(){
        nowdate();
        //取得傳過來的資料
        go_ed_time = bundle.getString("bed");
        get_up_time = bundle.getString("get");
        date = bundle.getString("date");
        key = bundle.getString("key");
        mood = bundle.getString("mood");
        //切割字串
        String sleepTime = "";
        String wakeTime = "";
        sleepTime = getTime(sleepTime,go_ed_time);
        wakeTime = getTime(wakeTime,get_up_time);
        //設定畫面
        et_sleep_content.setText(bundle.getString("sleep_content"));
        et_go_bed_time.setText(sleepTime);
        et_get_up_time.setText(wakeTime);
        tv_date.setText(date);
        if(mood.charAt(0) == '/'){
            Bitmap bitmap= BitmapFactory.decodeFile(mood);
            ib_mood.setImageBitmap(bitmap);
        }
        else {
            int resId = this.getResources()
                    .getIdentifier(mood
                            , "drawable"
                            , "com.mis.relife");
            ib_mood.setImageResource(resId);
        }
    }
    //第三種 直接新增日記
    private void plus_diary(){
        et_go_bed_time.setText(go_ed_time);
        et_get_up_time.setText(get_up_time);

        nowdate();
        date = setDateFormat(mYear, mMonth, mDay);
        tv_date.setText(date);
    }

    //拿到切割過後的時間
    private String getTime(String Time,String date){
        Time = date.substring(11,16);
        return  Time;
    }

    //取得現在日期
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    //日期格式設定
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        String month = "";
        String day = "";
        if((monthOfYear + 1) / 10 == 0){
            month = "0" + String.valueOf(monthOfYear + 1);
        }
        else {
            month = String.valueOf(monthOfYear + 1);
        }
        if(dayOfMonth / 10 == 0){
            day = "0" + String.valueOf(dayOfMonth);
        }
        else {
            day = String.valueOf(dayOfMonth);
        }
        String choose_date = String.valueOf(year) + "/" + month + "/" + day;
        return choose_date;
    }

    private Button.OnClickListener date_pick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(sleep_plus.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    date = setDateFormat(year,month,dayOfMonth);
                    mYear = year;
                    mMonth = month;
                    mDay = dayOfMonth;
                    tv_date.setText(date);
                }
            }, mYear,mMonth, mDay).show();
        }
    };

    //完成點擊功能
    private Button.OnClickListener finish = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            if(et_get_up_time.getText().length() == 0 || et_go_bed_time.getText().length() == 0){
                Toast.makeText(sleep_plus.this,"請輸入時間",Toast.LENGTH_LONG).show();
            }
            else {
                //兩種模式
                //第一種 直接新增日記
                if (bool == 0) {
                    Sleep sleep_insert = new Sleep();
                    sleep_insert.sleepTime = date + " " + et_go_bed_time.getText().toString() + ":00";
                    sleep_insert.wakeTime = date + " " + et_get_up_time.getText().toString() + ":00";
                    sleep_insert.description = et_sleep_content.getText().toString();
                    sleep_insert.recordDate = date;
                    sleep_insert.mood = mood;

                    AppDbHelper.insertSleepToFireBase(sleep_insert);
                }
                //第二種  修改日記
                else if (bool == 1) {
                    Sleep sleep_edit = new Sleep();
                    sleep_edit.sleepTime = date + " " + et_go_bed_time.getText().toString() + ":00";
                    sleep_edit.wakeTime = date + " " + et_get_up_time.getText().toString() + ":00";
                    sleep_edit.description = et_sleep_content.getText().toString();
                    sleep_edit.mood = mood;
                    sleep_edit.recordDate = date;

                    System.out.println("key!!!!!!!!!!!!!!!!!!" + key);
                    AppDbHelper.updateSleepToFireBase(key,sleep_edit);
                }
                finish();
            }
        }
    };

    //設置時間選擇器
    private void initTimePicker(final EditText et_choose) {

        TimePickerView mTimePickerView = new TimePickerView(this, TimePickerView.Type.HOURS_MINS);
        // 设置是否循环
        mTimePickerView.setCyclic(true);
        // 设置滚轮文字大小
        mTimePickerView.setTextSize(TimePickerView.TextSize.SMALL);
        // 设置时间可选范围(结合 setTime 方法使用,必须在)
        //Calendar calendar = Calendar.getInstance();
        // mTimePickerView.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));
        // 设置选中时间
        // mTimePickerView.setTime(new Date());
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                et_choose.setText(format.format(date));
            }
        });
        mTimePickerView.show();
    }

}
