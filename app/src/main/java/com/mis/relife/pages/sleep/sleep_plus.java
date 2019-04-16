package com.mis.relife.pages.sleep;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airsaid.pickerviewlibrary.TimePickerView;
import com.mis.relife.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class sleep_plus extends AppCompatActivity {

    private TextView tv_date;
    private Button tv_finish;
    private Button bt_finish;
    private EditText et_go_bed_time;
    private EditText et_get_up_time;
    private EditText et_sleep_content;

    private Bundle bundle = null;

    private String go_ed_time = "";
    private String get_up_time = "";
    private String dream = "";
    private String date = "";
    private int bool = 0,position;
    private int mYear,mMonth,mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_plus);
        tv_date = findViewById(R.id.tv_date);
        tv_finish  =findViewById(R.id.tv_finish);
        bt_finish = findViewById(R.id.bt_finish);
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
        go_ed_time = bundle.getString("bed");
        get_up_time = bundle.getString("get");
        date = bundle.getString("date");
        position = bundle.getInt("position");
        mDay = Integer.valueOf(recylerview_sleep_adapter.day.get(position));
        System.out.println("位置 : " + position);
        et_sleep_content.setText(bundle.getString("sleep_content"));
        et_go_bed_time.setText(go_ed_time);
        et_get_up_time.setText(get_up_time);
        tv_date.setText(date);
    }
    //第三種 直接新增日記
    private void plus_diary(){
        et_go_bed_time.setText(go_ed_time);
        et_get_up_time.setText(get_up_time);

        nowdate();
        date = setDateFormat(mYear, mMonth, mDay);
        tv_date.setText(date);
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
        String choose_date = String.valueOf(year) + " / " + String.valueOf(monthOfYear + 1) + " / " + String.valueOf(dayOfMonth);
        return choose_date;
    }

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
                    recylerview_sleep_adapter.insert_sleep_diary(String.valueOf(et_go_bed_time.getText()), String.valueOf(et_get_up_time.getText())
                            , String.valueOf(mDay), String.valueOf(et_sleep_content.getText()));
//                    sleep_viewpager_diary.recylerview_sleep_adapter.notifyDataSetChanged();
                }
                //第二種  修改日記
                else if (bool == 1) {
                    String choose_day = String.valueOf(mDay);
                    recylerview_sleep_adapter.edit_sleep_diary(String.valueOf(et_go_bed_time.getText()), String.valueOf(et_get_up_time.getText())
                            , choose_day, String.valueOf(et_sleep_content.getText()), position);
//                    sleep_viewpager_diary.recylerview_sleep_adapter.notifyDataSetChanged();
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
