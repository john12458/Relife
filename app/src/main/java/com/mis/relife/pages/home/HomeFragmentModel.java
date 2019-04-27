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

public class HomeFragmentModel extends BaseViewModel implements View.OnTouchListener {

    private final Activity activity;
    public final ObservableField<Integer> life = new ObservableField<>();
    public final ObservableField<String> account = new ObservableField<>();
    private final FragmentHomeBinding binding;

    public HomeFragmentModel(Activity activity, FragmentHomeBinding binding) {
        super();
        this.activity = activity;
        this.binding = binding;
    }
    public void onEnterUserInfo(){
        Intent intent = new Intent(activity, UserInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) { //touch the pet
        // Log.e("View", v.toString());
        int mx;
        int my;
        switch (event.getAction()) {          //判斷觸控的動作
            case MotionEvent.ACTION_DOWN:// 按下圖片時
                mx = (int) event.getRawX() ;     //getRawX()：是獲取相對顯示螢幕左上角的座標
                my = (int) event.getRawY() ;
                binding.handImage.setX(mx-binding.handImage.getWidth()/3*2);
                binding.handImage.setY(my-binding.handImage.getHeight()/5*4);
                binding.handImage.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:// 移動圖片時
                mx = (int) event.getRawX() ;
                my = (int) event.getRawY() ;
                binding.handImage.setX(mx-binding.handImage.getWidth()/3*2);
                binding.handImage.setY(my-binding.handImage.getHeight()/5*4);
                break;
            case MotionEvent.ACTION_UP:
                binding.handImage.setVisibility(View.GONE);
                break;
        }
        return true;
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
