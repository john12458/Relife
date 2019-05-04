package com.mis.relife.pages.eat;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.GridView;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;

import java.util.Calendar;

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
    SQLiteDatabase db;

    public eat_page_activity(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.eat_page,container,false);
        gv_data1 = view.findViewById(R.id.gv_data1);
        gv_data2 = view.findViewById(R.id.gv_data2);
        bt_datepicker = view.findViewById(R.id.bt_datepicker);
        bt_datepicker.setOnClickListener(datepicker);
        nowdate();
        date = setDateFormat(mYear,mMonth,mDay);
        bt_datepicker.setText(date);
        eat_gridview_adapter = new eat_page_gridview((MainActivity) context,data,top);
        eat_gridview_adapter2 = new eat_page_gridview2((MainActivity) context,menu,menu_img);
        gv_data1.setAdapter(eat_gridview_adapter);
        gv_data2.setAdapter(eat_gridview_adapter2);
        gv_data2.setOnItemClickListener(gv_eat);
        db = context.openOrCreateDatabase("relife", 0, null);
        String sql_recipe = "CREATE TABLE IF NOT EXISTS recipe " +
                "(name VARCHAR(20) , " +
                "foodID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "cal DOUBLE) ";
        db.execSQL(sql_recipe);
        String sql_record = "CREATE TABLE IF NOT EXISTS record " +
                "(recordID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date VARCHAR(20), " +
                "category VARCHAR(20),"+
                "foodID INTEGER,"+
                "number FLOAT) ";
        db.execSQL(sql_record);
        String sql_love = "CREATE TABLE IF NOT EXISTS love " +
                "(loveID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "foodID INTEGER)";
        db.execSQL(sql_love);
//        gv_data2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(context,"ewewe",Toast.LENGTH_LONG).show();
//                Intent intent_eat_new = new Intent();
//                intent_eat_new.setClass(context,eat_new_activity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("eat_name",menu[position]);
//                intent_eat_new.putExtras(bundle);
//                startActivity(intent_eat_new);
//            }
//        });
        return view;
    }

    private AdapterView.OnItemClickListener gv_eat = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position != 5) {
                Intent intent_eat_new = new Intent();
                intent_eat_new.setClass(context, eat_new_activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("eat_name", menu[position]);
                intent_eat_new.putExtras(bundle);
                startActivity(intent_eat_new);
            }
        }
    };

    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String format = setDateFormat(year,month,day);
                    bt_datepicker.setText(format);
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    private void nowdate(){
        Calendar now = Calendar.getInstance();
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }
}
