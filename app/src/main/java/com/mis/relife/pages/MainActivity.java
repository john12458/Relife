package com.mis.relife.pages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;

import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.pages.Service.FloatWindowService;
import com.mis.relife.pages.eat.EatTabViewpagerFragment;
import com.mis.relife.pages.eat.eat_page_activity;
import com.mis.relife.pages.home.HomeFragment;
import com.mis.relife.pages.login.LoginDialogFragment;
import com.mis.relife.pages.sleep.viewPager.sleep_tab_viewpager;
import com.mis.relife.pages.sport.ViewPager.sport_tab_viewpager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends AppCompatActivity implements sleep_tab_viewpager.OnFragmentInteractionListener , sport_tab_viewpager.OnFragmentInteractionListener {

    private FragmentManager fManager;
    private sport_tab_viewpager sport_tab_viewpager;
    private sleep_tab_viewpager sleep_page;
    private EatTabViewpagerFragment eat_page;
    private HomeFragment homeFragment;
    private BottomNavigationView navigation;
    private ImageView iv_icon;
    private ConstraintLayout main_constrain;
    private int[] tab_layout={R.layout.test_badge,R.layout.test_badge2,R.layout.test_badge3,R.layout.test_badge4};
    private int[] iv_id = {R.id.iv_home,R.id.iv_eat,R.id.iv_sport,R.id.iv_sleep};
    private int[] iv_res = {R.drawable.house,R.drawable.lunch,R.drawable.running,R.drawable.bed};

    int first = 0;
    SensorManager mSensorManager;
    Sensor stepCounter;
    float mSteps = 0;

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        main_constrain = findViewById(R.id.main_cons);
        System.out.println(first + "!!!!!!!!!aaaaaaa!!");

        checkFirstLogin(); //如果不是初次登入則會進入登入畫面
        //開啟service
        service();
        service_button();

    }
    private void checkFirstLogin(){
        String id = getSharedPreferences("user", MODE_PRIVATE)
                .getString("id", "");
        if(id.equals("")) {
            new LoginDialogFragment().show(getSupportFragmentManager(), "Login");
        } else{
            System.out.println("login id:"+id);
            new AppDbHelper(id);
            myInitlize();
        }
    }
    //方法區

    //開啟service
    private void service(){
        if(Build.VERSION.SDK_INT>=23)
        {
            if(Settings.canDrawOverlays(this))
            {
//有悬浮窗权限开启服务绑定 绑定权限
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);

            }else{
//没有悬浮窗权限m,去开启悬浮窗权限
                try{
                    Intent  intent=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivityForResult(intent, 0);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        } else{
            Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
            startService(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(MainActivity.this,"授權失敗",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this,"授權成功",Toast.LENGTH_LONG).show();
                //启动FxService
                Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
                startService(intent);
            }

        }
    }

    public void myInitlize(){
        fManager = getSupportFragmentManager();
        //  Fragments - Home, Eat, Sport, Sleep
        homeFragment = new HomeFragment();
        eat_page = new EatTabViewpagerFragment();
        sport_tab_viewpager = new sport_tab_viewpager(this,getSupportFragmentManager());
        sleep_page = new sleep_tab_viewpager(this);

        //  firstPage HomeFragment
        fManager.beginTransaction().replace(R.id.ly_content,homeFragment).commit();

        //  BottomNavigation
        navigation = findViewById(R.id.navigation);
        for(int i = 0;i < 4;i++){
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
            View tab = menuView.getChildAt(i);
            BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
            View badge= LayoutInflater.from(this).inflate(tab_layout[i],menuView,false);
            //將圖片用成picasso
            iv_icon = badge.findViewById(iv_id[i]);
            Picasso
                    .with(this)
                    .load(iv_res[i])
                    .into(iv_icon);

            itemView.addView(badge);
        }
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fManager.beginTransaction().replace(R.id.ly_content,homeFragment).commit();
                    changebackgorund_page(R.drawable.background_home);

                    return true;
                case R.id.navigation_eat:
                    fManager.beginTransaction().replace(R.id.ly_content,eat_page).commit();
                    changebackgorund_page(R.drawable.background_eat);

                    home_changepage_do();
                    return true;
                case R.id.navigation_sport:
                    fManager.beginTransaction().replace(R.id.ly_content,sport_tab_viewpager).commit();
                    changebackgorund_page(R.drawable.background_sport);

                    home_changepage_do();
                    return true;
                case R.id.navigation_sleep:
                    fManager.beginTransaction().replace(R.id.ly_content, sleep_page).commit();
                    changebackgorund_page(R.drawable.background_sleep);

                    home_changepage_do();
                    return true;
            }
            return false;
        }
    };

    private void changebackgorund_page(final int res){
        new Thread(new Runnable() {
            public void run() {
                main_constrain.post(new Runnable() {
                    public void run() {
                        main_constrain.setBackgroundResource(res);
                    }
                });
            }
        }).start();
    }

    private void home_changepage_do(){
        if(homeFragment.anim != null) {
            homeFragment.anim.stop();
        }
        if(homeFragment.food != null){
            homeFragment.food = null;
        }
        if(homeFragment.sport != null){
            homeFragment.sport = null;
        }
        homeFragment.anim = null;
    }

    private void service_button(){

        if( getIntent().getExtras() != null){
            fManager = getSupportFragmentManager();
            Bundle bundle = getIntent().getExtras();
            int bool = bundle.getInt("choose");
            if(bool == 1){
                eat_page = new EatTabViewpagerFragment();
                fManager.beginTransaction().replace(R.id.ly_content,eat_page).commit();
                main_constrain.setBackgroundResource(R.drawable.background_eat);
            }
            else if(bool == 2){
                sport_tab_viewpager = new sport_tab_viewpager(this,getSupportFragmentManager());
                fManager.beginTransaction().replace(R.id.ly_content,sport_tab_viewpager).commit();
                main_constrain.setBackgroundResource(R.drawable.background_sport);
            }
            else if(bool == 3){
                sleep_page = new sleep_tab_viewpager(this);
                fManager.beginTransaction().replace(R.id.ly_content, sleep_page).commit();
                main_constrain.setBackgroundResource(R.drawable.background_sleep);
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
