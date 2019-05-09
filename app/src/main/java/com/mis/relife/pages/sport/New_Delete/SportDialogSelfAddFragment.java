package com.mis.relife.pages.sport.New_Delete;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mis.relife.R;
import com.mis.relife.pages.sport.count_cal;

@SuppressLint("ValidFragment")
public class SportDialogSelfAddFragment extends DialogFragment {

    Context context;
    private Sport_Plus_Activity sportPlus;
    private EditText edSportType,edSportLoss;
    private Button btAddSelf;
    private count_cal countcal;

    public SportDialogSelfAddFragment(Context context, Sport_Plus_Activity sportPlus,count_cal count_cal){
        this.context = context;
        this.sportPlus = sportPlus;
        this.countcal = count_cal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sport_dialog_addself_fragment, null);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        edSportType = dialog.findViewById(R.id.ed_sport_type);
        edSportLoss = dialog.findViewById(R.id.ed_sport_loss);
        btAddSelf = dialog.findViewById(R.id.bt_ok);
        btAddSelf.setOnClickListener(addSelf);

        return dialog;
    }

    private Button.OnClickListener addSelf = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edSportType.getText().toString().length() == 0 || edSportLoss.getText().toString().length() == 0){
                Toast toast = Toast.makeText(context,"請輸入完整",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else {
                sportPlus.recyclerview_sport_type_child_adapter.sport_type_other.add(edSportType.getText().toString());
                countcal.sportType.add(edSportType.getText().toString());
                countcal.sportLoss.add(Double.valueOf(edSportLoss.getText().toString()));
                Toast toast = Toast.makeText(context,"已新增至其他",Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                dismiss();
            }
        }
    };
}
