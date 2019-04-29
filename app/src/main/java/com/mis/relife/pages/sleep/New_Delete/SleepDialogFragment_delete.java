package com.mis.relife.pages.sleep.New_Delete;

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
import com.mis.relife.pages.sleep.Adapter.recylerview_sleep_adapter;

@SuppressLint("ValidFragment")
public class SleepDialogFragment_delete extends DialogFragment {

    private String delete_key;
    private int position;
    private Button bt_delete;
    private com.mis.relife.pages.sleep.Adapter.recylerview_sleep_adapter recylerview_sleep_adapter;

    public SleepDialogFragment_delete(int position,String delete_key,recylerview_sleep_adapter recylerview_sleep_adapter){
        this.delete_key = delete_key;
        this.position = position;
        this.recylerview_sleep_adapter = recylerview_sleep_adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sleep_dialog_fragment, null);
        bt_delete = view.findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDbHelper.deleteSleepToFireBase(delete_key);
                recylerview_sleep_adapter.sleep_time.remove(position);
                recylerview_sleep_adapter.wake_time.remove(position);
                recylerview_sleep_adapter.recordDate.remove(position);
                recylerview_sleep_adapter.mood.remove(position);
                recylerview_sleep_adapter.notifyDataSetChanged();
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
