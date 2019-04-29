package com.mis.relife.pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.pages.eat.eat_page_activity;
import com.mis.relife.pages.home.HomeFragment;
import com.mis.relife.pages.login.LoginDialogFragment;
import com.mis.relife.pages.sleep.SleepFragment;
import com.mis.relife.pages.sport.sport_page_activity;

public class MainActivity extends AppCompatActivity{

    private FragmentManager fManager;
    private sport_page_activity sportFragment;
    private SleepFragment sleep_page;   
    private eat_page_activity eat_fg4;
    private HomeFragment homeFragment;
    private BottomNavigationView navigation;
    private int[] tab_layout={R.layout.test_badge,R.layout.test_badge2,R.layout.test_badge3,R.layout.test_badge4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new LoginDialogFragment().show(getSupportFragmentManager(),"Login");

        myInitlize();
    }
    public void myInitlize(){

        fManager = getSupportFragmentManager();
        //  Fragments - Home, Eat, Sport, Sleep
        homeFragment = new HomeFragment();
        eat_fg4 = new eat_page_activity(this);
        sportFragment = new sport_page_activity(this,fManager);
        sleep_page = new SleepFragment();
        //  firstPage HomeFragment
        fManager.beginTransaction().replace(R.id.ly_content,homeFragment).commit();
        //  BottomNavigation
        navigation = findViewById(R.id.navigation);
        for(int i = 0;i < 4;i++){
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
            View tab = menuView.getChildAt(i);
            BottomNavigationItemView itemView = (BottomNavigationItemView) tab;
            View badge= LayoutInflater.from(this).inflate(tab_layout[i],menuView,false);
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
                    return true;
                case R.id.navigation_eat:
                    fManager.beginTransaction().replace(R.id.ly_content,eat_fg4).commit();
                    return true;
                case R.id.navigation_sport:
                    fManager.beginTransaction().replace(R.id.ly_content,sportFragment).commit();
                    return true;
                case R.id.navigation_sleep:
                    fManager.beginTransaction().replace(R.id.ly_content, sleep_page).commit();
                    return true;
            }
            return false;
        }
    };


}
