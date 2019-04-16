package com.mis.relife.pages.sport;

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

import java.util.Calendar;



@SuppressLint("ValidFragment")
public class sport_page_activity extends Fragment {

    static Context context;
    private RecyclerView sport_recyclerView;
    public Button bt_datepicker;
    public ImageButton bt_sport_plus;
    public String date;
    private int mYear,mMonth,mDay;
    public static TextView tv_sport_cal;
    public TextView tv_sport_walk;
    public static recylerview_sportpage_adapter sportpage_adapter = null;
    private FragmentManager fm;
    public static int total_cal = 0;


    public sport_page_activity(Context context,FragmentManager fm){
        this.context = context;
        recylerview_sportpage_adapter.inidata_sport_diary("200");
        this.fm = fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sport_page,container,false);
        sport_recyclerView = view.findViewById(R.id.sport_recyler_view);
        tv_sport_cal = view.findViewById(R.id.tv_sport_cal);
        tv_sport_walk = view.findViewById(R.id.tv_walk_number);
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        bt_sport_plus = view.findViewById(R.id.bt_sport_plus);
        bt_datepicker.setOnClickListener(datepicker);

        totalcal();

        nowdate();
        date = setDateFormat(mYear,mMonth,mDay);
        bt_datepicker.setText(date);
        sport_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        sport_recyclerView.addItemDecoration(new recyler_item_space(0,30));
        sport_recyclerView.setAdapter(sportpage_adapter);

        sportpage_adapter.setOnItemClickListener(sport_record_click);

        bt_sport_plus.setOnClickListener(sport_record_plus);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle back_bundle = data.getExtras();
        String start = back_bundle.getString("start_time");
        String name = back_bundle.getString("sport_type");
        String time = back_bundle.getString("sport_time");
        String cal = back_bundle.getString("cal");

        recylerview_sportpage_adapter.insetdata_sport_diary(name,time,cal,start);
        sportpage_adapter.notifyDataSetChanged();
    }

    //寫運動日記
    private Button.OnClickListener sport_record_plus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent_sport_plus = new Intent();
            intent_sport_plus.setClass(context, Sport_Plus_Activity.class);
            startActivity(intent_sport_plus);
        }
    };

    //運動日記 點擊編輯 或長按刪除
    private recylerview_sportpage_adapter.OnItemClickListener sport_record_click = new recylerview_sportpage_adapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent_sport_edit = new Intent();
            intent_sport_edit.setClass(context, Sport_Plus_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sport_name",recylerview_sportpage_adapter.sport_name.get(position));
            bundle.putString("sport_start",recylerview_sportpage_adapter.sport_StartTime.get(position));
            bundle.putString("sport_time" ,recylerview_sportpage_adapter.sport_time.get(position));
            bundle.putString("sport_cal",recylerview_sportpage_adapter.sport_cal.get(position));
            bundle.putInt("position",position);

            intent_sport_edit.putExtras(bundle);
            startActivity(intent_sport_edit);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            SportDialogFragment sportDialogFragment = new SportDialogFragment(position);
            sportDialogFragment.show(fm,"dialogment");
        }
    };

    //點擊選擇日期
    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String format = setDateFormat(year,month,day);
                    bt_datepicker.setText(format);
                    recylerview_sportpage_adapter.inidata_sport_diary("300");
                    sport_recyclerView.setAdapter(sportpage_adapter);
                    totalcal();
                    sportpage_adapter.setOnItemClickListener(sport_record_click);
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    //計算總卡路里
    static void totalcal(){
        total_cal = 0;
        for (int i = 0; i < recylerview_sportpage_adapter.sport_cal.size(); i++) {
            total_cal = total_cal + Integer.valueOf(recylerview_sportpage_adapter.sport_cal.get(i));
        }
        tv_sport_cal.setText(String.valueOf(total_cal) + "cal");
    }

    //得到現在日期
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH );
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    //設定日期格式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "/"
                + String.valueOf(monthOfYear + 1) + "/"
                + String.valueOf(dayOfMonth);
    }

}
