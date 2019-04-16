package com.mis.relife.pages.home.userInfo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mis.relife.R;
import com.mis.relife.databinding.ActivityUserInfoBinding;
import com.mis.relife.pages.home.userInfo.components.GridBtn;

/*
 * Structure
 * View -----------------------
 * UserInfoActivity
 *   -   GridBtn
 *       -   BtnsGridAdapter
 * ViewModel -----------------
 *   -   UserInfoModel
 * */
public class UserInfoActivity extends AppCompatActivity {

    private UserInfoModel vm;
    private GridBtn gridBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserInfoBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        vm = new UserInfoModel(this);
        binding.setUserInfoModel(vm);
        gridBtn = new GridBtn(this,binding.UserInfoBtns,vm);
    }

}
