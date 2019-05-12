package com.mis.relife.pages.eat;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Info;
import com.mis.relife.generated.callback.OnClickListener;
import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;
import com.mis.relife.pages.sport.SportData;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("ValidFragment")
public class eat_page_activity extends Fragment {

    private View view;
    private Context context;
    GridView gv_data1,gv_data2;
    public Button bt_datepicker,bt_week,bt_day;
    private BaseAdapter eat_gridview_adapter;
    private BaseAdapter eat_gridview_adapter2;
    private int mYear,mMonth,mDay;
    public String date;
    private String[] top = {"今日目標","飲食攝取","運動消耗"};
    private String[] data = {"0","0","0"};
    private String[] menu = {"早餐","午餐","晚餐","宵夜","點心","開水"};
    private int[] menu_img = {R.drawable.breakfast,R.drawable.lunch,R.drawable.dinner,R.drawable.corn
            ,R.drawable.pudding,R.drawable.water};
    public static String selectdate;
    SQLiteDatabase db;
    LayoutInflater inflater;
    private SportData sportData;
    public int lossTotalCal = 0;
    private Info info;

    public eat_page_activity(Context context,SportData sportData) {
        this.context = context;
        this.sportData = sportData;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.eat_page,container,false);
        gv_data1 = view.findViewById(R.id.gv_data1);
        gv_data2 = view.findViewById(R.id.gv_data2);
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        bt_datepicker.setOnClickListener(datepicker);
        nowdate();
        date = setDateFormat(mYear,mMonth,mDay);
        myInit();
        bt_datepicker.setText(date);
        eat_gridview_adapter = new eat_page_gridview((MainActivity) context,data,top,context,this);
        eat_gridview_adapter2 = new eat_page_gridview2((MainActivity) context,menu,menu_img,context);
        gv_data1.setAdapter(eat_gridview_adapter);
        gv_data2.setAdapter(eat_gridview_adapter2);
        gv_data2.setOnItemClickListener(gv_eat);
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy/MM/dd");
        selectdate = ft.format(dNow);

        AppDbHelper.getAllInfoFromFireBase(new MyCallBack<Info>() {
            @Override
            public void onCallback(Info value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                info = value;
            }
        });
        return view;
    }
    TextView watercc;
    int cc;
    private AdapterView.OnItemClickListener gv_eat = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position != 5) {
                Intent intent_eat_new = new Intent();
                intent_eat_new.setClass(context, eat_new_second.class);
                Bundle bundle = new Bundle();
                bundle.putString("eat_name", menu[position]);
                bundle.putInt("index", position);
                bundle.putString("date", selectdate);
                intent_eat_new.putExtras(bundle);
                startActivity(intent_eat_new);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("添加喝水量").setIcon(R.drawable.water)
                        .setView(inflater.inflate(R.layout.eat_water, null));
                final AlertDialog dialog = builder.create();
                dialog.show();
                watercc = dialog.findViewById(R.id.txv_watercc);
                cc = 0;
                db = getActivity().openOrCreateDatabase("relife", 0, null);
                Cursor c = db.rawQuery("SELECT * FROM water WHERE date = '" + eat_page_activity.selectdate + "'", null);
                if(c.moveToFirst()) {
                    cc = c.getInt(1);
                    watercc.setText(c.getInt(1) + "cc");
                }
                else {
                    watercc.setText("0 cc");
                }
                Button add10 = dialog.findViewById(R.id.btn_add10);
                Button add100 = dialog.findViewById(R.id.btn_add100);
                Button sub10 = dialog.findViewById(R.id.btn_sub10);
                Button sub100 = dialog.findViewById(R.id.btn_sub100);
                Button cancel = dialog.findViewById(R.id.dialog_button_cancel);
                Button ok = dialog.findViewById(R.id.dialog_button_ok);
                add10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cc += 10;
                        watercc.setText(cc + "cc");
                    }
                });
                add100.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cc += 100;
                        watercc.setText(cc + "cc");
                    }
                });
                sub10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cc >= 10) {
                            cc -= 10;
                        }
                        watercc.setText(cc + "cc");
                    }
                });
                sub100.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cc >= 100) {
                            cc -= 100;
                        }
                        else {
                            cc = 0;
                        }
                        watercc.setText(cc + "cc");
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cc_in_water = db.rawQuery("SELECT * FROM water WHERE date = '" + selectdate + "'", null);
                        if(cc_in_water.moveToFirst()) {
                            db.execSQL("UPDATE  water SET cc = " + cc + " WHERE date = '" + selectdate + "'");
                        }
                        else {
                            ContentValues cv_water = new ContentValues(2);
                            cv_water.put("date", selectdate);
                            cv_water.put("cc", cc);
                            db.insert("water", null, cv_water);
                        }
                        gv_data2.setAdapter(eat_gridview_adapter2);
                        dialog.dismiss();
                    }
                });
            }
        }
    };
    private void myInit(){

        for(int l = 0;l < sportData.sport_recordDate.size();l++){
            //比對到日期後做加總
            if(date.equals(sportData.sport_recordDate.get(l))){
                lossTotalCal += Integer.valueOf(sportData.sport_cal.get(l));
            }
        }
    }

    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    //String format = setDateFormat(year,month,day);
                    int m = month+1;
                    DecimalFormat df=new DecimalFormat("00");
                    String mon=df.format(m);
                    String d=df.format(day);
                    selectdate = year + "/" + mon + "/" + d;
                    bt_datepicker.setText(selectdate);
                    gv_data2.setAdapter(eat_gridview_adapter2);
                    gv_data1.setAdapter(eat_gridview_adapter);
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        gv_data2.setAdapter(eat_gridview_adapter2);
        gv_data1.setAdapter(eat_gridview_adapter);
    }

    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        DecimalFormat df=new DecimalFormat("00");
        int m = monthOfYear + 1;
        String mon=df.format(m);
        String day=df.format(dayOfMonth);
        return String.valueOf(year) + "/" + mon + "/" + day;
    }
}
