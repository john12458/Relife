package com.mis.relife.pages.sleep.diary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.diary.DiaryEditActivity;
import com.mis.relife.pages.sleep.diary.SleepClockActivity;

@SuppressLint("ValidFragment")
public class SleepDialogFragment_choose_way extends DialogFragment {

    private Button bt_byself,bt_clock;

    public SleepDialogFragment_choose_way(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sleep_dialog_fragment_choose_way, null);
        Dialog dialog = new Dialog(getActivity());
        bt_byself = view.findViewById(R.id.bt_byself);
        bt_clock = view.findViewById(R.id.bt_clock);
        //手動輸入睡眠日記
        bt_byself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sleep_plus = new Intent();
                sleep_plus.setClass(getContext(), DiaryEditActivity.class);
                startActivity(sleep_plus);
                dismiss();
            }
        });
        //設定鬧鐘 計時
        bt_clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clock = new Intent();
                clock.setClass(getContext(), SleepClockActivity.class);
                startActivity(clock);
                dismiss();
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;
    }
}
