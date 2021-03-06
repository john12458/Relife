package com.mis.relife.pages.home.userInfo.components;

import android.content.Context;
import android.widget.GridView;

import com.mis.relife.pages.home.userInfo.UserInfoActivity;
import com.mis.relife.pages.home.userInfo.UserInfoModel;


public class GridBtn{
    private final UserInfoModel userInfoModel;
    private BtnsGridAdapter adapter;
    private GridView userInfoBtns;
    private UserInfoActivity activity;


    public GridBtn(UserInfoActivity activity, GridView userInfoBtns, UserInfoModel userInfoModel){
        this.activity =  activity;
        this.userInfoBtns = userInfoBtns;
        this.userInfoModel = userInfoModel;
        initlize();
    }
    private void initlize(){
        Context context = activity.getApplicationContext();
        adapter = new BtnsGridAdapter(context,userInfoModel.btns);
        userInfoBtns.setAdapter(adapter);
        userInfoBtns.setOnItemClickListener(userInfoModel);
    }
}
