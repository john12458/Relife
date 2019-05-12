package com.mis.relife.pages.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Info;
import com.mis.relife.data.model.MyUser;
import com.mis.relife.databinding.FragmentLoginDialogBinding;
import com.mis.relife.databinding.FragmentRegisterDialogBinding;
import com.mis.relife.pages.MainActivity;

import static android.content.Context.MODE_PRIVATE;

public class RegisterDialogFragment extends DialogFragment {
    private FragmentRegisterDialogBinding binding;
    private InputMethodManager imm;
    private String account ;
    private String password ;
    private String checkPassword;

    public RegisterDialogFragment() { }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_dialog, null, false);
        binding.setRegister(this);
        View view = binding.getRoot();
        Dialog myDialog = new Dialog(getActivity());

        //讓dialog全屏
        Window myWindow = myDialog.getWindow();
        myWindow.requestFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(view);
        myWindow.setGravity(Gravity.BOTTOM);
        myWindow.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        myWindow.setBackgroundDrawableResource(R.color.transparent); //設了就不會有外圍的黑陰影 (不好的用法

        return myDialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        // 設置點外圍或是點返回鍵不會將跳出
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
    @Override
    public void onResume() {
        super.onResume();
        // 一開啟即出現鍵盤
        imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        //後來才瞭解，原來畫面還沒準備好，就去呼叫強制顯示鍵盤，所以Android根本不理我。
        //所以必須先讓他等個0.1秒，這樣就可以成功呼叫啦。
        binding.account.postDelayed(new Runnable(){
            @Override
            public void run(){
                imm.showSoftInput(binding.account,InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }
    /**   event and Data handler ----------------------------------------------------------------------------------------------------------------------------------------- */
    public void goToLoginClick(){ this.dismiss(); }
    public void onRegisterClick(){
        account = binding.account.getText().toString();
        password = binding.password.getText().toString();
        checkPassword = binding.checkPassword.getText().toString();

        if(!password.equals(checkPassword)) {
            alert("確認密碼與密碼不符合","確認");
            return ;
        }
        // 確認是否有重複帳號，沒有就執行註冊
        DatabaseReference userRef = AppDbHelper.mFirebase.getReference("user");
        final Query userQuery = userRef.orderByChild("info/account").equalTo(account);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userQuery.removeEventListener(this);
                if (dataSnapshot.exists())alert("已有此帳號，請重新填寫","確認");
                else register();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void register(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("user");
        String key = userRef.push().getKey();
        userRef.child(key).setValue(new MyUser(
                new Info(key,account,password,20,170,60,1,20,"男")
        ));
        alert("成功註冊 ! ! 請重新登入頁面","確認");
        goToLoginClick();
    }

    private void alert(String message,String btnTxt){
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setNegativeButton(btnTxt, null)
                .create()
                .show();
    }

}


