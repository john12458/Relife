package com.mis.relife.pages.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Info;
import com.mis.relife.databinding.FragmentLoginDialogBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginDialogFragment extends DialogFragment {
    private AlertDialog.Builder builder;
    private FragmentLoginDialogBinding binding;
    public LoginDialogFragment() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_dialog, null, false);
        binding.setLogin(this);
        View view = binding.getRoot();
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
    @Override
    public void onStart() {
        super.onStart();
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK) return true;
                else return false;
            }
        });
    }

    public void onLoginClick() {
        final String account = binding.account.getText().toString();
        final String password = binding.password.getText().toString();
//        final String account = "john1234";  //for test
//        final String password = "1234";     //for test
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("user");
        Query userQuery = userRef.orderByChild("info/account").equalTo(account);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Info info = data.child("info").getValue(Info.class);
                        if(info.password.equals(password)){
                            new AppDbHelper(info.id);
                            new AlertDialog.Builder(getContext())
                                    .setMessage("Sucess")
                                    .setNegativeButton("Enter", null)
                                    .create()
                                    .show();
                            getDialog().dismiss();
                            ((MainActivity)getActivity()).myInitlize();
                            return;
                        }
                    }
                }
                new AlertDialog.Builder(getContext())
                        .setMessage("Login Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

}


