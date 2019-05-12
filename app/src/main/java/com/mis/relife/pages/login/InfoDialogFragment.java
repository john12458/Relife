package com.mis.relife.pages.login;

import android.annotation.SuppressLint;
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
import com.mis.relife.databinding.FragmentInfoDialogBinding;
import com.mis.relife.databinding.FragmentLoginDialogBinding;
import com.mis.relife.databinding.FragmentRegisterDialogBinding;
import com.mis.relife.pages.MainActivity;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class InfoDialogFragment extends DialogFragment {
    private final RegisterDialogFragment fontPage;
    private FragmentInfoDialogBinding binding;
    private InputMethodManager imm;
    private String account ;
    private String password ;

    @SuppressLint("ValidFragment")
    public InfoDialogFragment(RegisterDialogFragment fontPage,String account,String password) {
        this.fontPage = fontPage;
        this.account = account;
        this.password = password;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_info_dialog, null, false);
        binding.setInfo(this);
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
    /**   event and Data handler ----------------------------------------------------------------------------------------------------------------------------------------- */
    public void goToFontPage(){ this.dismiss(); }
    public void onRegisterClick(){
        String gender = "其他";
            if(binding.gender.getCheckedRadioButtonId() == R.id.male)
                gender = "男";
            else if(binding.gender.getCheckedRadioButtonId() == R.id.female)
                gender = "女";
        float height = Float.parseFloat(binding.height.getText().toString());
        float weight = Float.parseFloat(binding.weight.getText().toString());
        float goalWeight = Float.parseFloat(binding.goalWeight.getText().toString());
        float goalWeekWeight = Float.parseFloat(binding.goalWeekWeight.getText().toString());
        int old = Integer.parseInt(binding.old.getText().toString());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference("user");
        String key = userRef.push().getKey();
        userRef.child(key).setValue(new MyUser(
                new Info(key,account,password,0,height,weight,goalWeight,goalWeekWeight,old,gender)
        ));
        alert("成功註冊 ! ! 請重新登入頁面","確認");
        fontPage.dismiss();
        this.dismiss();
    }

    private void alert(String message,String btnTxt){
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setNegativeButton(btnTxt, null)
                .create()
                .show();
    }

}


