package com.mis.relife.pages.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;

import com.mis.relife.pages.BaseViewModel;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Info;
import com.mis.relife.pages.home.userInfo.UserInfoActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HomeFragmentModel extends BaseViewModel {

    private final Activity activity;
    public final ObservableField<Integer> life = new ObservableField<>();
    public final ObservableField<String> account = new ObservableField<>();

    public HomeFragmentModel(Activity activity) {
        super();
        this.activity = activity;
    }
    public void onEnterUserInfo(){
        Intent intent = new Intent(activity, UserInfoActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public void myInit() {
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
}
