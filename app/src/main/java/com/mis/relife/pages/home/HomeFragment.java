package com.mis.relife.pages.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Diet;
import com.mis.relife.data.model.Sleep;
import com.mis.relife.data.model.Sport;
import com.mis.relife.databinding.FragmentHomeBinding;
import com.mis.relife.pages.MainActivity;
import com.mis.relife.pages.eat.eat_page_activity;
import com.squareup.picasso.Picasso;
import com.white.progressview.CircleProgressView;

import java.util.Calendar;
import java.util.Map;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    private HomeFragmentModel vm;
    public AnimationDrawable anim;
    private TextView tvSportBadge,tvEatBadge,tvSleepBadge,tvDrinkBadge;
    public ImageView food,sport,sleep,drink,user_pet;
    private ImageButton ibt_food,ibt_sport,ibt_sleep,ibt_drink;
    private FrameLayout test_frame;

    private int first = 0,count = 0,shy = 0;
    private String type = "";
    private int repeat = 0;
    float foodLastRawX, foodLastRawY;
    float sportLastRawX,sportLastRawY;
    float sleepLastRawX,sleepLastRawY;
    float drinkLastRawX,drinkLastRawY;
    private FragmentHomeBinding  binding;

    private CircleProgressView eatProgress,sportProgress,sleepProgress,drinkProgress;
    private int sportNum = 0,eatNum = 0,sleepNum = 0,drinkNum = 0;
    private int sporthun = 0,eathun = 0,sleephun = 0,drinkhun;
    private int mYear,mMonth,mDay,mHour;
    private String dateFormate;
    private Boolean sportHave = false,sleepHave = false,breakHave = false,lunHave = false,dinnHave = false;
    private int sportShow = 0,sleepShow = 0,breakShow = 0,lunShow = 0,dinnShow = 0;

    SQLiteDatabase db;
    private int cc = 0;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        View view = binding.getRoot();
        test_frame = view.findViewById(R.id.frame_test);
        user_pet = view.findViewById(R.id.imageView3);
        ibt_food = view.findViewById(R.id.ibt_food);
        ibt_sport = view.findViewById(R.id.ibt_sport);
        ibt_sleep = view.findViewById(R.id.ibt_sleep);
        ibt_drink = view.findViewById(R.id.ibt_drink);
        tvEatBadge = view.findViewById(R.id.tvEatBadge);
        tvSportBadge = view.findViewById(R.id.tvSportBadge);
        tvSleepBadge = view.findViewById(R.id.tvSleepBadge);
        tvDrinkBadge = view.findViewById(R.id.tvDrinkBadge);
        eatProgress = view.findViewById(R.id.eatCircleProgress);
        sportProgress = view.findViewById(R.id.sportCircleProgress);
        sleepProgress = view.findViewById(R.id.sleepCircleProgress);
        drinkProgress = view.findViewById(R.id.drinkCircleProgress);
        picasso_ibt(ibt_food,R.drawable.meal);
        picasso_ibt(ibt_sport,R.drawable.running);
        picasso_ibt(ibt_sleep,R.drawable.sleep);
        picasso_ibt(ibt_drink,R.drawable.water);
        sportHave = false;
        breakHave = false;
        lunHave = false;
        dinnHave = false;
        sleepHave = false;
        myInit();
        vm = new HomeFragmentModel(getActivity(),binding,this);
        binding.setHomeFragmentModel(vm);
        //初始化pet
        anim_pica();

        //寵物動畫區
        //增加食物 運動 睡眠 喝水 的view
        ibt_food.setOnClickListener(ibtEatClick);
        ibt_sport.setOnClickListener(ibtSportClick);
        ibt_sleep.setOnClickListener(ibtSleepClick);
        ibt_drink.setOnClickListener(ibtDrinkClick);

        return view;
    }

    //方法區________________________________開始

    //初始化按鈕的progress 還有寵物的反應看資料庫有新增些什麼紀錄
    //-------------------------初始化按鈕的progress 和寵物反應-------------------------------------
    private void myInit(){
        //首先拿到時間
//        nowdate();
//        dateFormate = setDateFormat(mYear,mMonth,mDay);
//        System.out.println("現在時間!!!!!!!!!!!" + dateFormate + "!!!!!!!!!!!!!! HOUR" + mHour);
//        //拿到喝多少水
//        db = getContext().openOrCreateDatabase("relife", 0, null);
//        Cursor c = db.rawQuery("SELECT * FROM water WHERE date = '" + dateFormate + "'", null);
//        if(c.moveToFirst()) {
//            cc = c.getInt(1);
//        }
//        else {
//
//        }
        //SQLlite結束
        //再來做比對 會做到運動 飲食 睡眠的比對
        AppDbHelper.getAllSportFromFireBase(new MyCallBack<Map<String, Sport>>() {
            @Override
            public void onCallback(Map<String, Sport> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                sportNum = 0;
                for(String key : value.keySet()){
                    Sport sport_data = value.get(key);
                    //看一下今天有沒有運動了
                    if(sport_data.recordDate.equals(dateFormate)){
                        sportHave = true;
                    }
                    sportNum++;
                }
                sportProgress.setProgress(countProgress(sportNum, tvSportBadge) * 20);
            }
        });
        AppDbHelper.getAllSleepFromFireBase(new MyCallBack<Map<String, Sleep>>() {
            @Override
            public void onCallback(Map<String, Sleep> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                sleepNum = 0;
                //看出前一天有沒有紀錄
                //拿到前一天的日期
                Calendar before = Calendar.getInstance();
                before.add(Calendar.DAY_OF_MONTH,-1);
                String beforeDate = setDateFormat(before.get(Calendar.YEAR),before.get(Calendar.MONTH),before.get(Calendar.DAY_OF_MONTH));
                //看一下有紀錄昨天的睡眠嗎
                for(String key : value.keySet()){
                    Sleep sleep_data = value.get(key);
                    //比對日期
                    if(sleep_data.recordDate.equals(beforeDate)){
                        sleepHave = true;
                    }
                    sleepNum++;
                }
                sleepProgress.setProgress(countProgress(sleepNum, tvSleepBadge) * 20);
            }
        });
        AppDbHelper.getAllDietFromFireBase(new MyCallBack<Map<String, Diet>>() {
            @Override
            public void onCallback(Map<String, Diet> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                eatNum = 0;
                //看一下有紀錄各餐嗎
                for(String key : value.keySet()){
                    Diet eat_data = value.get(key);
                    if(eat_data.eatDate.equals(dateFormate) && eat_data.category == 1){
                        breakHave = true;
                    }
                    if (eat_data.eatDate.equals(dateFormate) && eat_data.category == 2){
                        lunHave = true;
                    }
                    if (eat_data.eatDate.equals(dateFormate) && eat_data.category == 3){
                        dinnHave = true;
                    }
                    eatNum++;
                }
                eatProgress.setProgress(countProgress(eatNum, tvEatBadge) * 20);
            }
        });
    }

    //判斷在特定時間點如果沒有作紀錄的話 會出現某些特定反應
    public void judgePetTime(){
        System.out.println("!!!!!!!!!!" + sportHave);
        //為了與Info更新同步放在這裡
        drinkNum = cc / 100;
        drinkProgress.setProgress(countProgress(drinkNum,tvDrinkBadge) * 20);
        //--------------------為了與Info更新同步放在這裡---------------
        if(mHour < 11 && breakShow <= 1){
            judgeBreakfast();
            breakShow++;
            anim_change(8);
        }
        else if(mHour >= 12 && mHour < 14 && lunShow <= 1){
            judgeLunch();
            lunShow++;
            anim_change(8);
        }
        else if(mHour >= 18 && mHour < 20 && dinnShow <= 1){
            judgeDinner();
            dinnShow++;
            anim_change(8);
        }
        else if(mHour >= 20 && mHour < 22 && sportShow <= 1){
            judgeSport();
            sportShow++;
            anim_change(8);
        }
        else if(mHour >= 23 && sleepShow <= 1){
            judgeSleepWithAll();
            sleepShow++;
            anim_change(8);
        }
    }

    //以下為判斷區
    // ------------------------判斷區 開始--------------------------------
    private void judgeBreakfast(){
        if(breakHave == false && sleepHave == false){
            anim_hungryTired();
        }
        else if(breakHave == false && sleepHave == true){
            anim_hungry();
        }
    }

    private void judgeLunch(){
        if(lunHave == false){
            anim_hungry();
        }
    }

    private void judgeDinner(){
        if(dinnHave == false){
            anim_hungry();
        }
    }

    private void judgeSport(){
        if(sportHave == false){
            anim_lazy();
        }
    }

    private void judgeSleepWithAll(){
        if(dinnHave == false && lunHave == false && breakHave == false && sportHave == false){
            anim_terrible();
            return;
        }

        if(dinnHave == false){
            anim_hungryTired();
        }
        else {
            anim_tired();
        }
    }
    //--------------------------------判斷區 結束--------------------------------------

    //計算圓形進度條還有角標的量
    private int countProgress(int Num,TextView textView){
        int hun;
        if(Num >= 5){
            hun = Num / 5;
            Num = Num % 5;
            System.out.println("!!!!!!!!!!!!!!!!!hun" + hun);
            if(textView.getVisibility() == View.GONE) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(String.valueOf(hun));
            }
        }
        else {

        }
        return Num;
    }

    //得到現在日期 來判斷今天有記錄了哪些東西
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH );
        mDay = now.get(Calendar.DAY_OF_MONTH);
        mHour = now.get(Calendar.HOUR_OF_DAY);
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

    //-------------------------progress 和寵物反應 END-------------------------------------

    //動態加一個運動的view
    //-------------------------addview 開始-------------------------------------
    private void addsport(){
        //初始劃一個imageview
        sport = new ImageView(getActivity());
        //初始化view的大小
        FrameLayout.LayoutParams frame = new FrameLayout.LayoutParams(100, 100);
        //設定view的位置
        frame.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        frame.setMargins(0,0,50,190);
        //利用picasso來放入圖片
        picasso_iv(sport,R.drawable.run);
        //將上面設定好的大小位置放入
        sport.setLayoutParams(frame);
        //add進去主要view裡面
        test_frame.addView(sport);
    }

    //動態加一個食物的view
    //用法同上
    private void addfood(){
        food = new ImageView(getActivity());
        FrameLayout.LayoutParams frame = new FrameLayout.LayoutParams(100, 100);
        frame.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        frame.setMargins(0,0,200,190);
        picasso_iv(food,R.drawable.lunch);
        food.setLayoutParams(frame);
        test_frame.addView(food);
    }
    //動態加一個喝水的view
    //用法同上
    private void adddrink(){
        drink = new ImageView(getActivity());
        FrameLayout.LayoutParams frame = new FrameLayout.LayoutParams(100, 100);
        frame.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        frame.setMargins(0,0,500,190);
        picasso_iv(drink,R.drawable.water);
        drink.setLayoutParams(frame);
        test_frame.addView(drink);
    }
    //動態加一個睡眠的view
    //用法同上
    private void addsleep(){
        sleep = new ImageView(getActivity());
        FrameLayout.LayoutParams frame = new FrameLayout.LayoutParams(100, 100);
        frame.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
        frame.setMargins(0,0,350,190);
        picasso_iv(sleep,R.drawable.sleep);
        sleep.setLayoutParams(frame);
        test_frame.addView(sleep);
    }

    //-------------------------addview END-------------------------------------

    //picasso 套件 給imageview的
    //-------------------------picasso 開始-------------------------------------
    private void picasso_iv(ImageView imageView,int res){
        Picasso
                .with(getContext())
                .load(res)
                .into(imageView);
    }
    //picasso 套件 給imagebutton的
    private void picasso_ibt(ImageButton imageButton,int res){
        Picasso
                .with(getContext())
                .load(res)
                .into(imageButton);
    }
    //-------------------------picasso END-------------------------------------

    //以下是按鈕區 有運動睡眠食物喝水 工作都是判斷角標是否為正數 如果是則創造view 然後可以拖拉
    //-------------------------按鈕區開始-------------------------------------
    private Button.OnClickListener ibtSportClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Integer.valueOf(tvSportBadge.getText().toString()) > 0) {
                if (sport == null) {
                    addsport();
                    sport.setOnTouchListener(sport_ontouch);
                    System.out.println("null!!!!!!!!!!!!!!!!!!!");
                } else if (sport.getVisibility() == View.GONE) {
                    sport.setVisibility(View.VISIBLE);
                    System.out.println("visible!!!!!!!!!!!!!!!!!!!");
                } else {

                }
            }
        }
    };

    private Button.OnClickListener ibtSleepClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Integer.valueOf(tvSleepBadge.getText().toString()) > 0) {
                if (sleep == null) {
                    addsleep();
                    sleep.setOnTouchListener(sleep_ontouch);
                    System.out.println("null!!!!!!!!!!!!!!!!!!!");
                } else if (sleep.getVisibility() == View.GONE) {
                    addsleep();
                    sleep.setOnTouchListener(sleep_ontouch);
                    System.out.println("visible!!!!!!!!!!!!!!!!!!!");
                } else {
                    System.out.println("NO!!!!!!!!!!!!!!!!!!!" + sleep.getVisibility());
                }
            }
        }
    };

    private Button.OnClickListener ibtEatClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Integer.valueOf(tvEatBadge.getText().toString()) > 0) {
                if (food == null) {
                    addfood();
                    food.setOnTouchListener(food_ontouch);
                    System.out.println("null!!!!!!!!!!!!!!!!!!!");
                } else if (food.getVisibility() == View.GONE) {
                    food.setVisibility(View.VISIBLE);
                    System.out.println("visible!!!!!!!!!!!!!!!!!!!");
                } else {

                }
            }
        }
    };

    private Button.OnClickListener ibtDrinkClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(Integer.valueOf(tvDrinkBadge.getText().toString()) > 0) {
                if (drink == null) {
                    adddrink();
                    drink.setOnTouchListener(drink_ontouch);
                    System.out.println("null!!!!!!!!!!!!!!!!!!!");
                } else if (drink.getVisibility() == View.GONE) {
                    adddrink();
                    drink.setOnTouchListener(drink_ontouch);
                    System.out.println("visible!!!!!!!!!!!!!!!!!!!");
                } else {

                }
            }
        }
    };

    //---------------------------------按鈕區結束-----------------------------------------------


    //add進來的sport物件 的 touch 事件
    //-------------------------touch 開始-------------------------------------
    private View.OnTouchListener sport_ontouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //紀錄一開始的位置
                    sportLastRawX = event.getRawX();
                    sportLastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //變更位置的計算
                    int dx = (int) (event.getRawX() - sportLastRawX);//相对坐标
                    int dy = (int) (event.getRawY() - sportLastRawY);//相对坐标
                    //動態變更位置
                    sport.layout(sport.getLeft() + dx, sport.getTop() + dy, sport.getRight() + dx, sport.getBottom() + dy);
                    //紀錄位置
                    sportLastRawX = event.getRawX();
                    sportLastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    int duration = 0;
                    //如果物件離寵物很近 就執行
                    if(user_pet.getTop() + 10 < sport.getTop() && user_pet.getLeft() + 10 < sport.getLeft() &&
                            user_pet.getBottom() - 10 > sport.getBottom() && user_pet.getRight() - 10 > sport.getRight()){
                        anim_walk();
                        //將物件弄不見
                        sport.setVisibility(View.GONE);
                        //角標的減少
                        sporthun = Integer.valueOf(tvSportBadge.getText().toString());
                        sporthun--;
                        if(sporthun == 0){
                            tvSportBadge.setText("0");
                            tvSportBadge.setVisibility(View.GONE);
                        }
                        else {
                            tvSportBadge.setText(String.valueOf(sporthun));
                        }

                        //當動畫跑完後 開始跑原本的
                        anim_change(4);
                    }
                    else {
                        //如果沒有放到正確位置 動畫繼續原本的
                        anim_pica();
                    }
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener sleep_ontouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //紀錄一開始的位置
                    sleepLastRawX = event.getRawX();
                    sleepLastRawY = event.getRawY();
                    anim_tired();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //變更位置的計算
                    int dx = (int) (event.getRawX() - sleepLastRawX);//相对坐标
                    int dy = (int) (event.getRawY() - sleepLastRawY);//相对坐标
                    //動態變更位置
                    sleep.layout(sleep.getLeft() + dx, sleep.getTop() + dy, sleep.getRight() + dx, sleep.getBottom() + dy);
                    //紀錄位置
                    sleepLastRawX = event.getRawX();
                    sleepLastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    int duration = 0;
                    //如果物件離寵物很近 就執行
                    if(user_pet.getTop() + 10 < sleep.getTop() && user_pet.getLeft() + 10 < sleep.getLeft() &&
                            user_pet.getBottom() - 10 > sleep.getBottom() && user_pet.getRight() - 10 > sleep.getRight()){
                        anim_shine();
                        //將物件弄不見
                        sleep.setVisibility(View.GONE);
                        //角標的減少
                        sleephun = Integer.valueOf(tvSleepBadge.getText().toString());
                        sleephun--;
                        if(sleephun == 0){
                            tvSleepBadge.setText("0");
                            tvSleepBadge.setVisibility(View.GONE);
                        }
                        else {
                            tvSleepBadge.setText(String.valueOf(sleephun));
                        }

                        //當動畫跑完後 開始跑原本的
                        anim_change(4);
                    }
                    else {
                        //如果沒有放到正確位置 動畫繼續原本的
                        anim_pica();
                    }
                    break;
            }
            return true;
        }
    };

    //食物物件的touch事件 用法同上
    private View.OnTouchListener food_ontouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    foodLastRawX = event.getRawX();
                    foodLastRawY = event.getRawY();
                    anim_shine();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int dx = (int) (event.getRawX() - foodLastRawX);//相对坐标
                    int dy = (int) (event.getRawY() - foodLastRawY);//相对坐标
                    food.layout(food.getLeft() + dx, food.getTop() + dy, food.getRight() + dx, food.getBottom() + dy);//设置按钮位置
                    foodLastRawX = event.getRawX();
                    foodLastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    int duration = 0;
                    if(user_pet.getTop() + 10 < food.getTop() && user_pet.getLeft() + 10 < food.getLeft() &&
                            user_pet.getBottom() - 10 > food.getBottom() && user_pet.getRight() - 10 > food.getRight()){
                        anim_eat();
                        food.setVisibility(View.GONE);
                        eathun = Integer.valueOf(tvEatBadge.getText().toString());
                        eathun--;
                        if(eathun == 0){
                            tvEatBadge.setText("0");
                            tvEatBadge.setVisibility(View.GONE);
                        }
                        else {
                            tvEatBadge.setText(String.valueOf(eathun));
                        }
                        anim_change(4);
                    }
                    else {
                        anim_pica();
                    }
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener drink_ontouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    //紀錄一開始的位置
                    drinkLastRawX = event.getRawX();
                    drinkLastRawY = event.getRawY();
                    anim_drink();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //變更位置的計算
                    int dx = (int) (event.getRawX() - drinkLastRawX);//相对坐标
                    int dy = (int) (event.getRawY() - drinkLastRawY);//相对坐标
                    //動態變更位置
                    drink.layout(drink.getLeft() + dx, drink.getTop() + dy, drink.getRight() + dx, drink.getBottom() + dy);
                    //紀錄位置
                    drinkLastRawX = event.getRawX();
                    drinkLastRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    int duration = 0;
                    //如果物件離寵物很近 就執行
                    if(user_pet.getTop() + 10 < drink.getTop() && user_pet.getLeft() + 10 < drink.getLeft() &&
                            user_pet.getBottom() - 10 > drink.getBottom() && user_pet.getRight() - 10 > drink.getRight()){
                        anim_eat();
                        //將物件弄不見
                        drink.setVisibility(View.GONE);
                        //角標的減少
                        drinkhun = Integer.valueOf(tvDrinkBadge.getText().toString());
                        drinkhun--;
                        if(drinkhun == 0){
                            tvDrinkBadge.setText("0");
                            tvDrinkBadge.setVisibility(View.GONE);
                        }
                        else {
                            tvDrinkBadge.setText(String.valueOf(drinkhun));
                        }

                        //當動畫跑完後 開始跑原本的
                        anim_change(4);
                    }
                    else {
                        //如果沒有放到正確位置 動畫繼續原本的
                        anim_pica();
                    }
                    break;
            }
            return true;
        }
    };

    //寵物的touch 事件 主要是害羞用
    private View.OnTouchListener pet_ontouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int mx;
            int my;
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    shy = 0;
                    // hand
                    mx = (int) event.getRawX() ;     //getRawX()：是獲取相對顯示螢幕左上角的座標
                    my = (int) event.getRawY() ;
                    binding.handImage.setX(mx-binding.handImage.getWidth()/3*2);
                    binding.handImage.setY(my-binding.handImage.getHeight()/5*4);
                    binding.handImage.setVisibility(View.VISIBLE);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(shy == 0){
                        anim_shy();
                        shy++;
                    }
                    // hand
                    mx = (int) event.getRawX() ;
                    my = (int) event.getRawY() ;
                    binding.handImage.setX(mx-binding.handImage.getWidth()/3*2);
                    binding.handImage.setY(my-binding.handImage.getHeight()/5*4);
                    break;
                case MotionEvent.ACTION_UP:
                    anim_pica();
                    binding.handImage.setVisibility(View.GONE);
            }
            return true;
        }
    };

    //-------------------------touch 結束-------------------------------------

    //用來動畫排隊執行
    //-------------------------動畫區 開始-------------------------------------
    private void anim_change(int repeat){
        int duration = 0;
        for(int i=0;i<anim.getNumberOfFrames();i++){
            duration += anim.getDuration(i);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                anim_pica();
            }
        }, duration * repeat);
    }

    //執行 吃的動畫
    private void anim_eat(){
        type = "eat";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_eat);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 害羞的動畫
    private void anim_shy(){
        type = "shy";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_shy);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 走路的動畫
    private void anim_walk(){
        type = "walk";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_walk);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 開心的動畫
    private void anim_shine(){
        type = "shine";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_shine);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    private void anim_drink(){
        type = "drink";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_drink);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 累的動畫
    private void anim_tired(){
        type = "tired";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_tired);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 餓的動畫
    private void anim_hungry(){
        type = "hungry";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_hungry);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 糟糕的動畫
    private void anim_terrible(){
        type = "terrible";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_terrible);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 累的動畫
    private void anim_lazy(){
        type = "lazy";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_lazy);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 餓加累的動畫
    private void anim_hungryTired(){
        type = "hungryTired";
        anim.stop();
        anim = null;
        user_pet.setImageResource(R.drawable.anim_hun_plus_tired);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //執行 原本的動畫
    private void anim_pica(){
        type = "normal";
        if(anim != null){
            anim.stop();
            anim = null;
        }
        user_pet.setImageResource(R.drawable.ani_normal);
        user_pet.setOnTouchListener(pet_ontouch);
        anim = (AnimationDrawable) user_pet.getDrawable();
        anim.start();
    }
    //-------------------------動畫區 結束-------------------------------------

    //方法區________________________________結束
}
