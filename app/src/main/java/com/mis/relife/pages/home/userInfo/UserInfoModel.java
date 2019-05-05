package com.mis.relife.pages.home.userInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.pages.MainActivity;
import com.mis.relife.pages.home.userInfo.components.ReviceAccountDialogFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class UserInfoModel extends BaseViewModel implements AdapterView.OnItemClickListener,OnSuccessListener<Void>,OnFailureListener{

    private final Context context;
    private final UserInfoActivity activity;

    public final ObservableField<Integer> life = new ObservableField<>();
    public final ObservableField<String> account = new ObservableField<>();
    public String[] btns={"帳戶設定","登出","重生"};

    public UserInfoModel(UserInfoActivity activity){
        super();
        this.activity = activity;
        this.context = activity.getApplicationContext();
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
    //  BackButton
    public void onBackButtonClick() {
        activity.onBackPressed();
    }
    //  GridBtn
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0: // 帳戶設定
                onAccountChangeClick();
                break;
            case 1: // 登出
                onLogout();
                break;
            case 2: // 重生
                onRelifeClick();
            break;
        }
    }
    private void onLogout(){
        Toast.makeText(context,"登出!!",Toast.LENGTH_SHORT).show();
        SharedPreferences pref = activity.getSharedPreferences("user", MODE_PRIVATE);
        pref.edit()
                .putString("id","")
                .commit();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
    private void onRelifeClick(){
        Toast.makeText(context,"重生!!",Toast.LENGTH_SHORT).show();
//        AppDbHelper.deleteAllInfoToFireBase();
//        AppDbHelper.deleteAllDietToFireBase();
//        AppDbHelper.deleteAllSleepToFireBase();
//        AppDbHelper.deleteAllSportToFireBase();
    }
    private void onAccountChangeClick(){
        new ReviceAccountDialogFragment(this).show(activity.getSupportFragmentManager(),"Revice");
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

