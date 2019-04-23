package com.mis.relife.pages.sleep.diary;

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
import com.mis.relife.useful.recyler_item_space;

import java.time.LocalDateTime;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class DiaryFragment extends Fragment {

    private int create = 0;

    private RecyclerView recycler_View;
    private ImageButton bt_clock,bt_sleep_plus;
    private TextView bt_datepicker;

    public static RecylerviewSleepAdapter recylerview_sleep_adapter = null;

    private int mYear,mMonth,mDay;
    private String date,titledate;
    static int i = 0;
    private static int back = 0;
    private int bool = 1;

    public DiaryFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sleep_viewpager_diary,container,false);

//        SleepFragment.sleep_adapter.notifyDataSetChanged();

//        if(create == 1) {
            System.out.println("哈哈哈哈哈哈哈哈哈");
            bt_clock = view.findViewById(R.id.bt_clock);
            bt_datepicker = view.findViewById(R.id.bt_datepicker);
//            bt_sleep_plus = view.findViewById(R.id.bt_sleep_plus);
            recycler_View = view.findViewById(R.id.recyler_view);
            //設定今天日期
            nowdate();
            date = setDateFormat(mYear, mMonth, mDay);
            titledate = setdiarydateFormat(mYear, mMonth);
            bt_datepicker.setText(titledate);
            //初始化一開始的日記
            if (i == 0) {
                recylerview_sleep_adapter.initial_sleep_diary("9");
                i = 2;
            }
            recylerview_sleep_adapter.notifyDataSetChanged();
            recycler_View.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_View.addItemDecoration(new recyler_item_space(0, 30));
            recycler_View.setAdapter(recylerview_sleep_adapter);
            //設定點擊功能
            recylerview_sleep_adapter.setOnItemClickListener(sleep_diary_adapter_click);
            //如題
            bt_datepicker.setOnClickListener(date_pick);
            //去睡覺 設鬧鐘
            bt_clock.setOnClickListener(plus);
            //去手動增加日記
//            bt_sleep_plus.setOnClickListener(plus_diary_self);
//        }
        return view;
    }

    //方法區

    //選擇日記的輸入方式
    private Button.OnClickListener plus = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            SleepDialogFragment_choose_way sleepDialogFragment_choose_way = new SleepDialogFragment_choose_way();
            sleepDialogFragment_choose_way.show(getChildFragmentManager(),"dialogment_choose_way");
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
            new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String format = setdiarydateFormat(year,month);
                    if(mMonth != month || mYear != year){
                        recylerview_sleep_adapter.initial_sleep_diary("10");
                        recycler_View.setAdapter(recylerview_sleep_adapter);
                        recylerview_sleep_adapter.setOnItemClickListener(sleep_diary_adapter_click);
                    }
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                    bt_datepicker.setText(format);
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
    private RecylerviewSleepAdapter.OnItemClickListener sleep_diary_adapter_click = new RecylerviewSleepAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            String choose_date = setDateFormat(mYear,mMonth,Integer.valueOf(recylerview_sleep_adapter.day.get(position)));
            Intent sleep_edit = new Intent();
            sleep_edit.setClass(getContext(), DiaryEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("bool",bool);
            bundle.putInt("position",position);
            bundle.putString("bed",recylerview_sleep_adapter.go_bed_time.get(position));
            bundle.putString("get",recylerview_sleep_adapter.get_up_time.get(position));
            bundle.putString("date",choose_date);
            bundle.putString("sleep_content",recylerview_sleep_adapter.content.get(position));
            sleep_edit.putExtras(bundle);
            startActivity(sleep_edit);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            SleepDialogFragment_delete sleepDialogFragment_delete = new SleepDialogFragment_delete(position);
            sleepDialogFragment_delete.show(getChildFragmentManager(),"dialogment_delete");
        }
    };
}
