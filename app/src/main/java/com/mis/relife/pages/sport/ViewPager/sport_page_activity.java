package com.mis.relife.pages.sport.ViewPager;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import com.lilei.springactionmenu.ActionMenu;
import com.lilei.springactionmenu.OnActionItemClickListener;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Sport;
import com.mis.relife.pages.sport.Adapter.recylerview_sportpage_adapter;
import com.mis.relife.pages.sport.New_Delete.SportClockActivity;
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
import java.util.Random;


@SuppressLint("ValidFragment")
public class sport_page_activity extends Fragment {
    //建議區
    private int old = 27;
    private int a = 0,b = 0;
    private String[] sixTalkSport = {"騎自行車、跳繩","跳繩、各種球類、游泳、武術","伏地挺身、仰臥起坐、跳遠、跑步"};
    private String[] thirTalkSport = {"慢跑","爬山","有氧運動","肌力與重量訓練","瑜珈"};
    private String[] forTalkSport = {"慢走","騎自行車","健走"};
    private String[] sixTalkadvice = {"球類運動能提升反應速度、心肺耐力,有助於肌肉和骨骼發育",
            "體能消耗大的球類運動比較能滿足運動強度喔",
            "每周至少150分鐘中等強度有氧活動"};
    private String[] thirTalkadvice = {"年紀慢慢變大,新陳代謝會下降,肌肉量會逐漸流失喔",
            "建議每週3次運動,一次為30分鐘",
            "適合有氧鍛鍊或重量訓練",
            "用稍微溫和的運動方式緩解壓力",
            "工作或課業再忙也不要忘記和家人朋友出門散心喔","加油加油~"};
    private String[] forTalkadvice = {"運動應以安全、簡便，同時還要能穩定肌肉群為主"};
    private String notSport = "今天還沒運動喔";
    private String notenoughSport = "運動量不太夠喔~";

    Context context;
    private RecyclerView sport_recyclerView;
    public Button bt_datepicker;
    public ImageButton bt_sport_plus;
    public String date;
    private int mYear,mMonth,mDay;
    private int nowmYear,nowmMonth,nowmDay;
    private String dateFormat;
    public TextView tv_sport_cal,tvTalk;
    public recylerview_sportpage_adapter sportpage_adapter ;
    private FragmentManager fm;
    public int total_cal = 0;
    private EasyFlipView easyFlipView;
    private ImageView iv_sport,iv_cal,iv_talk_pet,iv_talk_place;
    private ImageButton ibv_plus,ibv_daily;
    ActionMenu actionMenu;
    private SportData sportData;
    private Bundle bundle = new Bundle();

    public List<String> recycler_sport_time = new ArrayList<>();
    public List<String> recycler_sport_cal = new ArrayList<>();
    public List<String> recycler_sport_name = new ArrayList<>();
    public List<String> recycler_sport_StartTime = new ArrayList<>();
    public List<String> recycler_sport_recordDate = new ArrayList<>();

    public AnimationDrawable anim;
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
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        actionMenu = view.findViewById(R.id.actionMenu);
        easyFlipView = view.findViewById(R.id.sport_cons_circle);
        iv_sport = view.findViewById(R.id.iv_sport);
        iv_cal = view.findViewById(R.id.iv_champion);
        tvTalk = view.findViewById(R.id.tv_talk);
        iv_talk_pet = view.findViewById(R.id.iv_talk_pet);
        iv_talk_place = view.findViewById(R.id.iv_talk_place);
        //picasso初始化圖片
        ini_img();
        //初始化actionMenu
        action_add();
        actionMenu.setItemClickListener(action_click);

        sport_recyclerView.setLayoutManager(new LinearLayoutManager(context));
        bt_datepicker.setOnClickListener(datepicker);

        if(first == 0) {
            nowdate();
            dateFormat = setDateFormat(mYear,mMonth,mDay);
            bt_datepicker.setText("今天");
            first++;
            sport_recyclerView.addItemDecoration(new recyler_item_space(0,30));
        }


        myInit();
        return view;
    }

    private void myInit(){
        AppDbHelper.getAllSportFromFireBase(new MyCallBack<Map<String, Sport>>() {
            @Override
            public void onCallback(Map<String, Sport> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                if(!value.isEmpty()) {
                    getNowDate();
                    iniTalkText();
                    sportpage_adapter = new recylerview_sportpage_adapter(context,recycler_sport_name,recycler_sport_StartTime,recycler_sport_time,recycler_sport_cal);
                    sport_recyclerView.setAdapter(sportpage_adapter);

                    totalcal();
                    iv_talk_pet.setOnClickListener(petClick);
                    iv_talk_pet.setImageResource(R.drawable.anim_teach);
                    anim = (AnimationDrawable) iv_talk_pet.getDrawable();

                    sportpage_adapter.setOnItemClickListener(sport_record_click);
                }
            }
        });
    }

    private void action_add(){
        actionMenu.addView(R.drawable.write,Color.rgb(255,165,0),Color.rgb(255,215,0));
        actionMenu.addView(R.drawable.sportclock,Color.rgb(255,165,0),Color.rgb(255,215,0));
    }

    private Button.OnClickListener petClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Random ran = new Random();
            if (anim.isRunning()){
                setTalkText(ran);
            }
            else {
                setTalkText(ran);
                anim.start();
            }
        }
    };

    private void iniTalkText(){
        if(recycler_sport_name.size() == 0){
            tvTalk.setText(notSport);
        }
        else {
            int totalTime = 0;
            for(int i = 0;i < recycler_sport_time.size();i++){
                totalTime += Integer.valueOf(recycler_sport_time.get(i));
            }
            if(totalTime < 30){
                tvTalk.setText(notenoughSport);
            }
            else {
                tvTalk.setText("運動充足~讚讚~");
            }
        }
    }

    //設定建議的文字 已年齡做區分
    private void setTalkText(Random ran){
        if(old == 0) {

        }
        else if(old >= 8 && old < 25){
            six(ran);
        }
        else  if(old >= 25 && old < 45){
            thir(ran);
        }
        else  if(old >= 45 && old < 65){
            forty(ran);
        }
    }

    //如果隨機產生1~10的數字 % 2 是0的話就執行 運動type 建議 如果不是 0 那就 給 建議
    private void six(Random ran) {
        if ((ran.nextInt(9) + 1) % 2 == 0) {
            tvTalk.setText("可以考慮從事--" + sixTalkSport[a] + "--喔");
            a++;
        } else {
            tvTalk.setText(sixTalkadvice[b]);
            b++;
        }
        if (a == 3) {a = 0;}
        if (b == 3) {b = 0;}
    }

    private void thir(Random ran){
        if((ran.nextInt(9) + 1) % 2 == 0){
            tvTalk.setText("可以考慮從事--" + thirTalkSport[a] + "--喔");
            a++;
        }
        else {
            tvTalk.setText(thirTalkadvice[b]);
            b++;
        }
        if(a == 5){a = 0;}
        if(b == 5){b = 0;}
    }

    private void forty(Random ran){
        if((ran.nextInt(9) + 1) % 2 == 0){
            tvTalk.setText("可以考慮從事--" + forTalkSport[a] + "--喔");
            a++;
        }
        else {
            tvTalk.setText(forTalkadvice[b]);
            b++;
        }
        if(a == 3){a = 0;}
        if(b == 1){b = 0;}
    }

    private OnActionItemClickListener action_click = new OnActionItemClickListener() {
        @Override
        public void onItemClick(int i) {
            if(i == 1){
                Intent intent_sport_plus = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("choose","diary");
                bundle.putString("date", dateFormat);
                intent_sport_plus.setClass(context, Sport_Plus_Activity.class);
                intent_sport_plus.putExtras(bundle);
                startActivity(intent_sport_plus);
            }
            else if(i == 2){
                Intent intent_sport_plus = new Intent();
                intent_sport_plus.setClass(context, SportClockActivity.class);
                startActivity(intent_sport_plus);
            }
        }

        @Override
        public void onAnimationEnd(boolean b) {

        }
    };

    private void picasso_iv (int res,ImageView img){
        Picasso
                .with(context)
                .load(res)
                .into(img);
    }

    private void ini_img(){
        picasso_iv(R.drawable.sport,iv_sport);
        picasso_iv(R.drawable.champion,iv_cal);
        picasso_iv(R.drawable.logo_teach,iv_talk_pet);
        picasso_iv(R.drawable.blackboard,iv_talk_place);
    }

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
                    iniTalkText();
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
