package com.mis.relife.pages.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mis.relife.R;
import com.mis.relife.databinding.FragmentHomeBinding;
import com.squareup.picasso.Picasso;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    private HomeFragmentModel vm;
    public AnimationDrawable anim;
    public ImageView food,sport,user_pet;
    private ImageButton ibt_food,ibt_sport;
    private FrameLayout test_frame;

    private int first = 0,count = 0,shy = 0;
    private String type = "";
    private int repeat = 0;
    float foodLastRawX, foodLastRawY;
    float sportLastRawX,sportLastRawY;
    private FragmentHomeBinding  binding;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        vm = new HomeFragmentModel(getActivity(),binding);
        binding.setHomeFragmentModel(vm);
        View view = binding.getRoot();
        test_frame = view.findViewById(R.id.frame_test);
        user_pet = view.findViewById(R.id.imageView3);
        ibt_food = view.findViewById(R.id.ibt_food);
        ibt_sport = view.findViewById(R.id.ibt_sport);
        picasso_ibt(ibt_food,R.drawable.meal);
        picasso_ibt(ibt_sport,R.drawable.running);
        //初始化pet
        anim_pica();



        //寵物動畫區
        //增加兩個view
        ibt_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food == null){
                    addfood();
                    food.setOnTouchListener(food_ontouch);
                    System.out.println("null!!!!!!!!!!!!!!!!!!!");
                }
                else if(food.getVisibility() == View.GONE){
                    food.setVisibility(View.VISIBLE);
                    System.out.println("visible!!!!!!!!!!!!!!!!!!!");
                }
                else {

                }
            }
        });
        ibt_sport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sport == null){
                    addsport();
                    sport.setOnTouchListener(sport_ontouch);
                    System.out.println("null!!!!!!!!!!!!!!!!!!!");
                }
                else if(sport.getVisibility() == View.GONE){
                    sport.setVisibility(View.VISIBLE);
                    System.out.println("visible!!!!!!!!!!!!!!!!!!!");
                }
                else {

                }
            }
        });


        return view;
    }

    //動態加一個運動的view
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
    //picasso 套件 給imageview的
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

    //add進來的sport物件 的 touch 事件
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
                        //當動畫跑完後 開始跑原本的
                        anim_change(duration);
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
                        anim_change(duration);
                    }
                    else {
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

    //用來動畫排隊執行
    private void anim_change(int duration){
        for(int i=0;i<anim.getNumberOfFrames();i++){
            duration += anim.getDuration(i);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                anim_pica();
            }
        }, duration * 4);
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
}
