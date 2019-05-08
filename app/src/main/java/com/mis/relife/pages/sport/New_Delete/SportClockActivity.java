package com.mis.relife.pages.sport.New_Delete;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.print.PrinterId;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.New_Delete.sleep_plus;
import com.mis.relife.pages.sport.Adapter.recyclerview_sport_plus_adapter;
import com.mis.relife.pages.sport.count_cal;
import com.mis.relife.useful.recyler_item_space;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.Calendar;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SportClockActivity extends AppCompatActivity {

    //控制畫面區
    private EasyFlipView easyFlipView;
    private Button btStart,btBack,btEnd;
    //寵物區
    private ImageView petSport;
    private TextView tvPetTalk;
    public AnimationDrawable anim;
    //爆炸按鈕區
    private BoomMenuButton boomMenuButtonRight,boomMenuButtonLeft;
    private Drawable[] subButtonDrawablesRight,subButtonDrawablesLeft ;
    private int[][] subButtonColors = {{Color.TRANSPARENT,Color.TRANSPARENT},{Color.TRANSPARENT,Color.TRANSPARENT},{Color.TRANSPARENT,Color.TRANSPARENT},
            {Color.TRANSPARENT,Color.TRANSPARENT},{Color.TRANSPARENT,Color.TRANSPARENT}};
    //選擇運動類型區
    private recyclerview_sport_plus_adapter recyclerview_sport_type_child_adapter;
    private RecyclerView recyclerView_sport_type;
    //計算時間區
    private TextView tvTime;
    private String startSportTime,tvHour = "",tvMin = "",tvSec = "";
    private int hour ,minute;
    private Handler handler = new Handler();
    private Long StartTime,passHours,passMinius,passSeconds;
    private Calendar now;
    private int mYear,mMonth,mDay;
    private String dateFormat;
    private count_cal count_cal;
    private String cal,sportName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sport_clock_activity);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //初始化所有所需要的view
            //爆炸view
        boomMenuButtonRight = findViewById(R.id.boom_right);
        boomMenuButtonLeft = findViewById(R.id.boom_left);
            //寵物view
        petSport = findViewById(R.id.sport_pet);
        tvPetTalk = findViewById(R.id.sport_item);
            //轉換view
        easyFlipView = findViewById(R.id.sport_clock_flip);
        btStart = findViewById(R.id.sport_start);
        btBack = findViewById(R.id.sport_go_back);
        btEnd = findViewById(R.id.sport_end);
            //運動類型和時間view
        tvTime  =findViewById(R.id.tv_time);
        recyclerView_sport_type = findViewById(R.id.recycler_sport_type);

        //初始化boom按鈕
        init_img();
        ini_boom();

        //走路動畫
        petSport.setImageResource(R.drawable.anim_walk);
        anim = (AnimationDrawable) petSport.getDrawable();
        anim.start();

        //建構sport種類
        recyclerview_sport_type_child_adapter = new recyclerview_sport_plus_adapter(SportClockActivity.this);
        sportTypeLayout();
        recyclerView_sport_type.setAdapter(recyclerview_sport_type_child_adapter);

        //設置所有點擊事件
        recyclerview_sport_type_child_adapter.setOnItemClickListener(sportTypeClick);
        btStart.setOnClickListener(btStartClick);
        btBack.setOnClickListener(btBackClick);
        btEnd.setOnClickListener(btEndClick);
        boomMenuButtonRight.setOnSubButtonClickListener(boomClickRight);
        boomMenuButtonLeft.setOnSubButtonClickListener(boomClickLeft);

        //時間計算
        //第一步 拿到開始運動時間
        setNowTime();
        startSportTime = setTextTime(hour) + ":" +setTextTime(minute);
        System.out.println("!!!!!!!!!!!!!!!!!!!" + startSportTime);
        //第二步拿到 記錄時間的年月份
        nowdate();
        dateFormat = setDateFormat(mYear,mMonth,mDay);
        count_cal = new count_cal();
    }

    //拿到現在時間 小時和分鐘
    private void setNowTime(){
        now = Calendar.getInstance();
        hour = now.get(Calendar.HOUR_OF_DAY);
        minute = now.get(Calendar.MINUTE);
    }

    private String setTextPassTime(Long num){
        String text;
        if(num / 10 == 0){
            text = "0" + String.valueOf(num);
        }
        else {
            text = String.valueOf(num);
        }
        return text;
    }

    private String setTextTime(int num){
        String text;
        if(num / 10 == 0){
            text = "0" + String.valueOf(num);
        }
        else {
            text = String.valueOf(num);
        }
        return text;
    }

    private Runnable updateTimer = new Runnable() {
        public void run() {
            Long spentTime = System.currentTimeMillis() - StartTime;
            //計算目前已過分鐘數
            passHours = (spentTime/1000)/3600;
            passMinius = (spentTime/1000)/60;
            //計算目前已過秒數
            passSeconds = (spentTime/1000) % 60;
            tvHour = setTextPassTime(passHours);
            tvMin = setTextPassTime(passMinius);
            tvSec = setTextPassTime(passSeconds);
            //設定字串
            tvTime.setText(tvHour+":"+tvMin+":"+tvSec);
            handler.postDelayed(this, 1000);
        }
    };

    //設定recyclerview的外觀
    private void sportTypeLayout(){
        LinearLayoutManager ms= new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_sport_type.addItemDecoration(new recyler_item_space(25,5));
        recyclerView_sport_type.setLayoutManager(ms);
    }

    //運動種類的點擊事件 設定text內容 和 將start按鈕設成可看見
    private recyclerview_sport_plus_adapter.OnItemClickListener sportTypeClick = new recyclerview_sport_plus_adapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            tvPetTalk.setText("你選擇 --" + recyclerview_sport_type_child_adapter.sport_type_child.get(position) + "-- 搂");
            sportName = recyclerview_sport_type_child_adapter.sport_type_child.get(position);
            btStart.setVisibility(View.VISIBLE);
        }
    };

    //start按鈕的點擊事件
    private Button.OnClickListener btStartClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            easyFlipView.flipTheView();
            btStart.setVisibility(View.GONE);
            btBack.setVisibility(View.VISIBLE);
            btEnd.setVisibility(View.VISIBLE);
            StartTime = System.currentTimeMillis();
            handler.removeCallbacks(updateTimer);
            //設定Delay的時間
            handler.postDelayed(updateTimer, 1000);
        }
    };
    //back按鈕的點擊事件
    private Button.OnClickListener btBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            easyFlipView.flipTheView();
            btStart.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btEnd.setVisibility(View.GONE);
        }
    };

    //end按鈕的點擊事件
    private Button.OnClickListener btEndClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent back_intent = new Intent();
            Bundle bundle = new Bundle();
            int totalTime = Integer.valueOf(tvHour) * 60 + Integer.valueOf(tvMin);
            cal = String.valueOf(count_cal.if_else_sport(sportName,totalTime,40));
            back_intent.setClass(SportClockActivity.this, Sport_Plus_Activity.class);
            bundle.putString("sport_name",sportName);
            bundle.putString("sport_time",String.valueOf(totalTime));
            bundle.putString("sport_start",startSportTime);
            bundle.putString("sport_cal",cal);
            bundle.putString("choose","clock");
            bundle.putString("date",dateFormat);
            back_intent.putExtras(bundle);
            startActivity(back_intent);
            finish();
        }
    };

    private BoomMenuButton.OnSubButtonClickListener boomClickRight = new BoomMenuButton.OnSubButtonClickListener() {
        @Override
        public void onClick(int buttonIndex) {
            btStart.setVisibility(View.GONE);
            if(buttonIndex == 0){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_run();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 1){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_beat();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 2){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_stick();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 3){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_ball();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 4){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_Martial();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
        }
    };

    private BoomMenuButton.OnSubButtonClickListener boomClickLeft = new BoomMenuButton.OnSubButtonClickListener() {
        @Override
        public void onClick(int buttonIndex) {
            btStart.setVisibility(View.GONE);
            if(buttonIndex == 0){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_swim();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 1){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_gymnastics();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 2){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_work();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 3){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_bike();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
            else if(buttonIndex == 4){
                tvPetTalk.setText("選個喜歡的運動吧");
                recyclerview_sport_type_child_adapter.inidata_sport_child_type_other();
                recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            }
        }
    };

    private void init_img(){
        subButtonDrawablesRight = new Drawable[]{getApplicationContext().getResources().getDrawable(R.drawable.add_run),
                getApplicationContext().getResources().getDrawable(R.drawable.add_pie),
                getApplicationContext().getResources().getDrawable(R.drawable.add_beat),
                getApplicationContext().getResources().getDrawable(R.drawable.add_ball),
                getApplicationContext().getResources().getDrawable(R.drawable.add_material)};

        subButtonDrawablesLeft = new Drawable[]{getApplicationContext().getResources().getDrawable(R.drawable.add_swim),
                getApplicationContext().getResources().getDrawable(R.drawable.add_fitness),
                getApplicationContext().getResources().getDrawable(R.drawable.add_work),
                getApplicationContext().getResources().getDrawable(R.drawable.add_bike),
                getApplicationContext().getResources().getDrawable(R.drawable.add_other)};
    }

    private void ini_boom(){
        boomMenuButtonRight.init(
                subButtonDrawablesRight, // 子按钮的图标Drawable数组，不可以为null
                null,     // 子按钮的文本String数组，可以为null
                subButtonColors,    // 子按钮的背景颜色color二维数组，包括按下和正常状态的颜色，不可为null
                ButtonType.CIRCLE,     // 子按钮的类型
                BoomType.PARABOLA_2,  // 爆炸类型
                PlaceType.CIRCLE_5_1,  // 排列类型
                null,               // 展开时子按钮移动的缓动函数类型
                null,               // 展开时子按钮放大的缓动函数类型
                null,               // 展开时子按钮旋转的缓动函数类型
                null,               // 隐藏时子按钮移动的缓动函数类型
                null,               // 隐藏时子按钮缩小的缓动函数类型
                null,               // 隐藏时子按钮旋转的缓动函数类型
                null                // 旋转角度
        );

        boomMenuButtonLeft.init(
                subButtonDrawablesLeft, // 子按钮的图标Drawable数组，不可以为null
                null,     // 子按钮的文本String数组，可以为null
                subButtonColors,    // 子按钮的背景颜色color二维数组，包括按下和正常状态的颜色，不可为null
                ButtonType.CIRCLE,     // 子按钮的类型
                BoomType.PARABOLA_2,  // 爆炸类型
                PlaceType.CIRCLE_5_1,  // 排列类型
                null,               // 展开时子按钮移动的缓动函数类型
                null,               // 展开时子按钮放大的缓动函数类型
                null,               // 展开时子按钮旋转的缓动函数类型
                null,               // 隐藏时子按钮移动的缓动函数类型
                null,               // 隐藏时子按钮缩小的缓动函数类型
                null,               // 隐藏时子按钮旋转的缓动函数类型
                null                // 旋转角度
        );
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        anim.stop();
        anim = null;
    }
}
