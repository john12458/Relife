package com.mis.relife.pages.sport.ViewPager;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Sport;
import com.mis.relife.pages.sport.Adapter.recylerview_sportpage_adapter;
import com.mis.relife.pages.sport.SportData;
import com.mis.relife.pages.sport.New_Delete.SportDialogFragment;
import com.mis.relife.pages.sport.New_Delete.Sport_Plus_Activity;
import com.mis.relife.useful.recyler_item_space;
import com.squareup.picasso.Picasso;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


@SuppressLint("ValidFragment")
public class sport_page_activity extends Fragment {

    Context context;
    private RecyclerView sport_recyclerView;
    public Button bt_datepicker;
    public ImageButton bt_sport_plus;
    public String date;
    private int mYear,mMonth,mDay;
    private int nowmYear,nowmMonth,nowmDay;
    private String dateFormat;
    public TextView tv_sport_cal;
    public TextView tv_sport_walk;
    public recylerview_sportpage_adapter sportpage_adapter ;
    private FragmentManager fm;
    public int total_cal = 0;
    private EasyFlipView easyFlipView;
    private ImageView iv_sport,iv_cal;
    private ImageButton ibv_plus,ibv_daily;
    private SportData sportData;
    private Bundle bundle = new Bundle();

    public List<String> recycler_sport_time = new ArrayList<>();
    public List<String> recycler_sport_cal = new ArrayList<>();
    public List<String> recycler_sport_name = new ArrayList<>();
    public List<String> recycler_sport_StartTime = new ArrayList<>();
    public List<String> recycler_sport_recordDate = new ArrayList<>();

    private int first = 0;

    public sport_page_activity(Context context,FragmentManager fm,SportData sportData){
        this.context = context;
        this.sportData = sportData;
        System.out.println("initial!!!!!!!!!!!!!!!!!!");
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
        easyFlipView = view.findViewById(R.id.sport_cons_circle);
        iv_sport = view.findViewById(R.id.iv_sport);
        iv_cal = view.findViewById(R.id.iv_champion);
        ibv_plus = view.findViewById(R.id.bt_sport_plus);
        ibv_daily = view.findViewById(R.id.bt_daily);
        ini_img();

        sport_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        sport_recyclerView.addItemDecoration(new recyler_item_space(0,30));

        bt_datepicker.setOnClickListener(datepicker);


        if(first == 0) {
            nowdate();
            dateFormat = setDateFormat(mYear,mMonth,mDay);
            bt_datepicker.setText("今天");
            first++;
        }

        myInit();

        bt_sport_plus.setOnClickListener(sport_record_plus);
        return view;
    }

    private void myInit(){
        AppDbHelper.getAllSportFromFireBase(new MyCallBack<Map<String, Sport>>() {
            @Override
            public void onCallback(Map<String, Sport> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                if(!value.isEmpty()) {
                    getNowDate();

                    sportpage_adapter = new recylerview_sportpage_adapter(context,recycler_sport_name,recycler_sport_StartTime,recycler_sport_time,recycler_sport_cal);
                    sport_recyclerView.setAdapter(sportpage_adapter);

                    totalcal();

                    sportpage_adapter.setOnItemClickListener(sport_record_click);
                }
            }
        });
    }

    private void picasso_iv (int res,ImageView img){
        Picasso
                .with(context)
                .load(res)
                .into(img);
    }

    private void picasso_ib (int res,ImageButton img){
        Picasso
                .with(context)
                .load(res)
                .into(img);
    }

    private void ini_img(){
        picasso_iv(R.drawable.sport,iv_sport);
        picasso_iv(R.drawable.champion,iv_cal);
        picasso_ib(R.drawable.sleep_plus02,ibv_plus);
        picasso_ib(R.drawable.daily,ibv_daily);
    }

    //寫運動日記
    private Button.OnClickListener sport_record_plus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent_sport_plus = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("choose","diary");
            bundle.putString("date", dateFormat);
            intent_sport_plus.setClass(context, Sport_Plus_Activity.class);
            intent_sport_plus.putExtras(bundle);
            startActivity(intent_sport_plus);
        }
    };

    //運動日記 點擊編輯 或長按刪除
    private recylerview_sportpage_adapter.OnItemClickListener sport_record_click = new recylerview_sportpage_adapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            String name = sportpage_adapter.sport_name.get(position);
            String startTime = sportpage_adapter.sport_StartTime.get(position);
            String time = sportpage_adapter.sport_time.get(position);
            String cal = sportpage_adapter.sport_cal.get(position);
            String key = sportData.get_key(name,startTime,time,cal);

            Intent intent_sport_edit = new Intent();
            intent_sport_edit.setClass(context, Sport_Plus_Activity.class);
            Bundle bundle = new Bundle();
            bundle.putString("choose","edit");
            bundle.putString("sport_name",name);
            bundle.putString("sport_start",startTime);
            bundle.putString("sport_time" ,time);
            bundle.putString("sport_cal",cal);
            bundle.putString("key",key);
            bundle.putString("date",  dateFormat);
            bundle.putInt("position",position);

            intent_sport_edit.putExtras(bundle);
            startActivity(intent_sport_edit);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            String name = sportpage_adapter.sport_name.get(position);
            String startTime = sportpage_adapter.sport_StartTime.get(position);
            String time = sportpage_adapter.sport_time.get(position);
            String cal = sportpage_adapter.sport_cal.get(position);
            String key = sportData.get_key(name,startTime,time,cal);
            System.out.println(key + "delete !!!!!!!!!!!!!!!!!!!!!!!");
            SportDialogFragment sportDialogFragment = new SportDialogFragment(key,sportpage_adapter,position);
            sportDialogFragment.show(fm,"dialogment");
        }
    };

    //拿到現在datepicker所指定的日期
    private void getNowDate(){
        recycler_sport_name.clear();
        recycler_sport_StartTime.clear();
        recycler_sport_time.clear();
        recycler_sport_cal.clear();
        recycler_sport_recordDate.clear();
        System.out.println(date);
        for(int i = 0;i < sportData.sport_recordDate.size(); i++){
            if(sportData.sport_recordDate.get(i).equals(dateFormat)){
                System.out.println("right!!!!!!!!!" + date);
                recycler_sport_name.add(sportData.sport_name.get(i));
                recycler_sport_StartTime.add(sportData.sport_StartTime.get(i));
                recycler_sport_time.add(sportData.sport_time.get(i));
                recycler_sport_cal.add(sportData.sport_cal.get(i));
                recycler_sport_recordDate.add(sportData.sport_recordDate.get(i));
            }
        }
    }

    //點擊選擇日期
    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    dateFormat = setDateFormat(year,month,day);
                    if(nowmYear == year && nowmMonth == month && nowmDay == day)
                        bt_datepicker.setText("今天");
                    else if(nowmYear == year && nowmMonth == month && nowmDay - 1 == day)
                        bt_datepicker.setText("昨天");
                    else bt_datepicker.setText(dateFormat);
                    getNowDate();
                    sportpage_adapter.notifyDataSetChanged();
                    totalcal();
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    //計算總卡路里
    public void totalcal(){
        total_cal = 0;
        for (int i = 0; i < sportpage_adapter.sport_cal.size(); i++) {
            total_cal = total_cal + Integer.valueOf(sportpage_adapter.sport_cal.get(i));
        }
        tv_sport_cal.setText(String.valueOf(total_cal) + "cal");
    }

    //得到現在日期
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        nowmYear = now.get(Calendar.YEAR);
        nowmMonth = now.get(Calendar.MONTH );
        nowmDay = now.get(Calendar.DAY_OF_MONTH);
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH );
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    //設定日期格式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        String month,day;
        //判斷如果是個位數 日期前面要加個零
        if(monthOfYear / 10 == 0){
            month = "0" + String.valueOf(monthOfYear + 1);
        }
        else {
            month = String.valueOf(monthOfYear + 1);
        }
        if (dayOfMonth / 10 == 0){
            day = "0" + String.valueOf(dayOfMonth);
        }
        else {
            day = String.valueOf(dayOfMonth);
        }
        //設定日期格式
        return String.valueOf(year) + "/"
                + month + "/"
                + day;
    }

}
