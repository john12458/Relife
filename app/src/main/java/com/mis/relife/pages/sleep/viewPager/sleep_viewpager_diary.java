package com.mis.relife.pages.sleep.viewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.ClockActivity;
import com.mis.relife.pages.sleep.New_Delete.SleepDialogFragment_choose_way;
import com.mis.relife.pages.sleep.New_Delete.SleepDialogFragment_delete;
import com.mis.relife.pages.sleep.Adapter.recylerview_sleep_adapter;
import com.mis.relife.pages.sleep.New_Delete.sleep_plus;
import com.mis.relife.pages.sleep.SleepData;
import com.mis.relife.useful.recyler_item_space;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressLint("ValidFragment")
public class sleep_viewpager_diary extends Fragment {


    Context context;
    private RecyclerView recycler_View;
    private ImageButton bt_clock,bt_test;
    private TextView bt_datepicker;

    public recylerview_sleep_adapter recylerview_sleep_adapter = null;
    public List<String> recycler_sleep_time = new ArrayList<>();
    public List<String> recycler_wake_time = new ArrayList<>();
    public List<String> recycler_recordate = new ArrayList<>();
    public List<String> recycler_description = new ArrayList<>();
    public List<String> recycler_mood = new ArrayList<>();

    private int mYear,mMonth,mDay;
    private String date,titledate;
    int i = 0;
    private int back = 0;
    private int bool = 1;
    private FragmentManager fm;
    private SleepData sleepData;

    SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy/MM/dd");
    private Calendar cal;

    public sleep_viewpager_diary(Context context,SleepData sleepData){
        this.context = context;
        this.sleepData = sleepData;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_viewpager_diary,container,false);
        fm =  getFragmentManager();
        System.out.println("哈哈哈哈哈哈哈哈哈");
        bt_clock = view.findViewById(R.id.bt_clock);
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        recycler_View = view.findViewById(R.id.recyler_view);
        bt_test = view.findViewById(R.id.bt_sleep_plus);
        bt_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(), ClockActivity.class);
                startActivity(intent);
            }
        });

        Picasso
                .with(context)
                .load(R.drawable.clock)
                .into(bt_clock);

        //設定今天日期
        nowdate();
        date = setDateFormat(mYear, mMonth, mDay);
        titledate = setdiarydateFormat(mYear, mMonth);
        bt_datepicker.setText(titledate);

        getNowDate();
        recylerview_sleep_adapter = new recylerview_sleep_adapter(getContext(),recycler_recordate,recycler_sleep_time,recycler_wake_time,recycler_description,recycler_mood);
        recycler_View.setLayoutManager(new LinearLayoutManager(context));
        recycler_View.addItemDecoration(new recyler_item_space(0, 30));
        recycler_View.setAdapter(recylerview_sleep_adapter);
        //設定點擊功能
        recylerview_sleep_adapter.setOnItemClickListener(sleep_diary_adapter_click);
        //如題
        bt_datepicker.setOnClickListener(date_pick);
        //去睡覺 設鬧鐘
        bt_clock.setOnClickListener(plus);
        return view;
    }

    //方法區

    //拿到現在datepicker所指定的日期
    private void getNowDate(){
        //把所有東西都清空
        recycler_sleep_time.clear();
        recycler_wake_time.clear();
        recycler_recordate.clear();
        recycler_description.clear();
        recycler_mood.clear();
        //建立 比對的month 和 要存取切割字串的睡眠和起床時間
        int recordMonth = 0;
        for(int i = 0;i < sleepData.record_date.size(); i++){
            //拿到month
            recordMonth = getMonth(recordMonth,sleepData.record_date.get(i));
            System.out.println("DATE!!!!!" + sleepData.record_date.get(i));
            System.out.println("recodeMonth!!!!!!!!!!!" + recordMonth);
            //比對到
            if(recordMonth == mMonth){
                //新增
                recycler_sleep_time.add(sleepData.sleep_time.get(i));
                recycler_wake_time.add(sleepData.wake_time.get(i));
                recycler_recordate.add(sleepData.record_date.get(i));
                recycler_description.add(sleepData.description.get(i));
                recycler_mood.add(sleepData.mood.get(i));
            }
        }
    }

    //拿到轉換過後的月份
    private int getMonth(int Month,String date){
        try {
            Date recorDate = sdf_date.parse(date);
            cal = Calendar.getInstance();
            cal.setTime(recorDate);
            Month = cal.get(Calendar.MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  Month;
    }

    //選擇日記的輸入方式
    private Button.OnClickListener plus = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            SleepDialogFragment_choose_way sleepDialogFragment_choose_way = new SleepDialogFragment_choose_way(context);
            sleepDialogFragment_choose_way.show(fm,"dialogment_choose_way");
        }
    };

    //拿到現在日期
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    //選擇日期
    private Button.OnClickListener date_pick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String format = setdiarydateFormat(year,month);
                    bt_datepicker.setText(format);
                    System.out.println(mMonth + "!!!!!!!!!!!!!!!!!" + month);
                    if(mMonth != month || mYear != year){
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                        getNowDate();
                        recylerview_sleep_adapter = new recylerview_sleep_adapter(getContext(),recycler_recordate,recycler_sleep_time,recycler_wake_time,recycler_description,recycler_mood);
                        recycler_View.setAdapter(recylerview_sleep_adapter);
                        recylerview_sleep_adapter.setOnItemClickListener(sleep_diary_adapter_click);
                    }
                    else {
                        mYear = year;
                        mMonth = month;
                        mDay = day;
                    }
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    //設定日期顯示方式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        String choose_date = String.valueOf(year) + " / " + String.valueOf(monthOfYear + 1) + " / " + String.valueOf(dayOfMonth);
        return choose_date;
    }

    //設定幾月日記
    private String setdiarydateFormat(int year,int monthOfYear){
        String choose_date = String.valueOf(year) + " / " + String.valueOf(monthOfYear + 1);
        return choose_date;
    }


    //日記的點擊功能 分為 點及修改 長按刪除
    private recylerview_sleep_adapter.OnItemClickListener sleep_diary_adapter_click = new recylerview_sleep_adapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            String sleepTime = recylerview_sleep_adapter.sleep_time.get(position);
            String wakeTime = recylerview_sleep_adapter.wake_time.get(position);
            String recordDate = recylerview_sleep_adapter.recordDate.get(position);
            String description = recylerview_sleep_adapter.description.get(position);
            String key = sleepData.get_key(sleepTime,wakeTime,recordDate,description);

            Intent sleep_edit = new Intent();
            sleep_edit.setClass(context, sleep_plus.class);
            Bundle bundle = new Bundle();
            bundle.putInt("bool",bool);
            bundle.putInt("position",position);
            bundle.putString("bed",sleepTime);
            bundle.putString("get",wakeTime);
            bundle.putString("date",recordDate);
            bundle.putString("sleep_content",description);
            bundle.putString("key",key);
            sleep_edit.putExtras(bundle);
            startActivity(sleep_edit);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            String sleepTime = recylerview_sleep_adapter.sleep_time.get(position);
            String wakeTime = recylerview_sleep_adapter.wake_time.get(position);
            String recordDate = recylerview_sleep_adapter.recordDate.get(position);
            String description = recylerview_sleep_adapter.description.get(position);
            String key = sleepData.get_key(sleepTime,wakeTime,recordDate,description);
            System.out.println("key!!!!!!!!!!!!!!!!" + key);
            SleepDialogFragment_delete sleepDialogFragment_delete = new SleepDialogFragment_delete(position,key,recylerview_sleep_adapter);
            sleepDialogFragment_delete.show(fm,"dialogment_delete");
        }
    };
}
