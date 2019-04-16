package com.mis.relife.pages.home.userInfo.components;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.mis.relife.data.MyCallBack;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Info;
import com.mis.relife.databinding.FragmentReviceAccountDialogBinding;
import com.mis.relife.pages.home.userInfo.UserInfoModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


@SuppressLint("ValidFragment")
public class ReviceAccountDialogFragment extends DialogFragment {
    private final UserInfoModel userInfoModel;
    private FragmentReviceAccountDialogBinding binding;
    private AlertDialog.Builder builder;

    public ReviceAccountDialogFragment(UserInfoModel userInfoModel) {
        this.userInfoModel=  userInfoModel;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_revice_account_dialog,null,false);
        binding.setUserInfoModel(userInfoModel);
        View view = binding.getRoot();

        builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton("submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AppDbHelper.getAllInfoFromFireBase(new MyCallBack<Info>() {
                            @Override
                            public void onCallback(Info value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                                if(binding.oldPassword.getText().toString().equals(value.password)){
                                    String account = binding.editname.getText().toString();
                                    String newPassword = binding.newPassword.getText().toString();
                                    AppDbHelper.insertInfoToFireBase(new Info(value.id,account,newPassword,value.life));

                                    userInfoModel.account.set(account);
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("通知")
                                            .setMessage("更改完成!!")
                                            .setPositiveButton("確認", null).create().show();
                                }else{
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("警告")
                                            .setMessage("密碼錯誤!!")
                                            .setPositiveButton("確認", null).create().show();
                                }
                                dataRef.removeEventListener(vlistenr);
                            }

                });}})
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

}
