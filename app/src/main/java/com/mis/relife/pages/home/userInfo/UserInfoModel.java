package com.mis.relife.pages.home.userInfo;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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
        Toast.makeText(context,"返回",Toast.LENGTH_SHORT).show();
        activity.onBackPressed();
    }
    //  GridBtn
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(position) {
            case 0: // 帳戶設定
                Toast.makeText(context,"帳戶設定",Toast.LENGTH_SHORT).show();
                new ReviceAccountDialogFragment(this)
                        .show(activity.getSupportFragmentManager(),"accountSetting");
                break;
            case 1: // 綁定帳號
                Toast.makeText(context,"修改",Toast.LENGTH_SHORT).show();
                AppDbHelper.updateInfoToFireBase("life",50)
                        .addOnSuccessListener(this)
                        .addOnFailureListener(this);

                List<Food> foods = new ArrayList<Food>();
                foods.add(new Food("bfefefewfa",1,100));
                foods.add(new Food("nofefefel",2,300));
                foods.add(new Food("neeel",2,300));
                foods.add(new Food("neee",2,300));

                AppDbHelper.updateDietToFireBase(dietMap.keySet().iterator().next(),new Diet(2,"2020-04-03 20:00",foods));
            break;
            case 2: // 重生
                Toast.makeText(context,"重生!!",Toast.LENGTH_SHORT).show();
                AppDbHelper.updateInfoToFireBase("life",10)
                        .addOnSuccessListener(this)
                        .addOnFailureListener(this);

                break;
        }
    }

    @Override
    public void myInit(){
        AppDbHelper.getAllInfoFromFireBase(new MyCallBack<Info>() {
            @Override
            public void onCallback(Info value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                life.set(value.life);
                account.set(value.account);
            }

        });

        AppDbHelper.getAllDietFromFireBase(new MyCallBack<Map<String, Diet>>() {
            @Override
            public void onCallback(Map<String, Diet> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                dietMap = new HashMap<String, Diet>();
                dietMap.putAll(value);
                for (String name: dietMap.keySet()){
                    String key = name.toString();
                    String mvalue = dietMap.get(name).toString();
                    System.out.println(key + " " + mvalue);
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

