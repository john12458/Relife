package com.mis.relife.pages.sport.New_Delete;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.pages.sport.Adapter.recylerview_sportpage_adapter;

@SuppressLint("ValidFragment")
public class SportDialogFragment extends DialogFragment {

    private Button bt_delete;
    private String delete_key;
    private int position;
    private com.mis.relife.pages.sport.Adapter.recylerview_sportpage_adapter recylerview_sportpage_adapter;

    public SportDialogFragment(String key,recylerview_sportpage_adapter recylerview_sportpage_adapter,int position){
        this.delete_key = key;
        this.recylerview_sportpage_adapter = recylerview_sportpage_adapter;
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sport_dialog_fragment, null);
        bt_delete = view.findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDbHelper.deleteSportToFireBase(delete_key);
                recylerview_sportpage_adapter.sport_name.remove(position);
                recylerview_sportpage_adapter.sport_StartTime.remove(position);
                recylerview_sportpage_adapter.sport_time.remove(position);
                recylerview_sportpage_adapter.sport_cal.remove(position);
                recylerview_sportpage_adapter.notifyDataSetChanged();
                dismiss();
            }
        });
        Dialog dialog = new Dialog(getActivity());
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
