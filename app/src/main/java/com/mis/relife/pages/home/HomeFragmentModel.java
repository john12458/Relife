package com.mis.relife.pages.home;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.mis.relife.databinding.FragmentHomeBinding;
import com.mis.relife.pages.BaseViewModel;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Info;
import com.mis.relife.pages.home.userInfo.UserInfoActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class HomeFragmentModel extends BaseViewModel{

    private final Activity activity;
    public final ObservableField<Integer> life = new ObservableField<>();
    public final ObservableField<String> account = new ObservableField<>();
    private final FragmentHomeBinding binding;
    private HomeFragment homeFragment;

    public HomeFragmentModel(Activity activity, FragmentHomeBinding binding,HomeFragment homeFragment) {
        super();
        this.activity = activity;
        this.binding = binding;
        this.homeFragment = homeFragment;
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
                    homeFragment.judgePetTime();
                }
            }
        });
    }

}
