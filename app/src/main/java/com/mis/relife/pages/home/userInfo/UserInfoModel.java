package com.mis.relife.pages.home.userInfo;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mis.relife.data.model.Sleep;
import com.mis.relife.data.model.Sport;
import com.mis.relife.pages.BaseViewModel;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Diet;
import com.mis.relife.data.model.Food;
import com.mis.relife.data.model.Info;
import com.mis.relife.pages.home.userInfo.components.ReviceAccountDialogFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoModel extends BaseViewModel implements AdapterView.OnItemClickListener,OnSuccessListener<Void>,OnFailureListener{

    private final Context context;
    private final UserInfoActivity activity;
    private  Map<String,Diet>  dietMap;
    public final ObservableField<Integer> life = new ObservableField<>();
    public final ObservableField<String> account = new ObservableField<>();

    public UserInfoModel(UserInfoActivity activity){
        super();
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    //  BackButton
    public void onBackButtonClick() {
        activity.onBackPressed();
    }
    //  GridBtn
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0: // 帳戶設定
//                new ReviceAccountDialogFragment(this)
//                        .show(activity.getSupportFragmentManager(),"accountSetting");
                List<Food> list = new  ArrayList<Food>();
                list.add(new Food("rice",1,300));
                AppDbHelper.insertDietToFireBase(new Diet(1,"2018-02-03 19:00",list));
                AppDbHelper.insertInfoToFireBase(new Info(1,"accountssssssss","pass",0));
                AppDbHelper.insertSleepToFireBase(new Sleep("Hello world!!","happy","2018-02-03 19:00","2018-02-04 00:00"));
                AppDbHelper.insertSportToFireBase(new Sport(20,300,"2018-02-03 19:00","運動"));

                break;
            case 1: // 重生
                Toast.makeText(context,"重生!!",Toast.LENGTH_SHORT).show();
                AppDbHelper.deleteAllInfoToFireBase();
                AppDbHelper.deleteAllDietToFireBase();
                AppDbHelper.deleteAllSleepToFireBase();
                AppDbHelper.deleteAllSportToFireBase();
            break;
        }
    }

    @Override
    public void myInit(){
        AppDbHelper.getAllInfoFromFireBase(new MyCallBack<Info>() {
            @Override
            public void onCallback(Info value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                if(value!=null){
                    life.set(value.life);
                    account.set(value.account);
                }

            }
        });
    }

    @Override
    public void onSuccess(Void aVoid) {
        Log.d("Suceess/Life","Suceess to change Life");
        Log.d("life", String.valueOf(life.get()));
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        Log.e("Error/Life","Error to change Life");
    }
}

