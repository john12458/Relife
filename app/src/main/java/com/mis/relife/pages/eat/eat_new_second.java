package com.mis.relife.pages.eat;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mis.relife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class eat_new_second extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner meal;
    String[] meals;
    ListView lv_record;
    public static TextView cal;
    int index;
    String date;
    public static String category;
    SQLiteDatabase db;
    private List<eat_listview_recipe> mData; // 定義數據

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_new_second);
        Bundle bundle = getIntent().getExtras();
        index = bundle.getInt("index");
        meal = (Spinner)findViewById(R.id.spinner);
        meals = getResources().getStringArray(R.array.meal);
        meal.setSelection(index);
        lv_record = (ListView) findViewById(R.id.listview_record);
        date = bundle.getString("date");
        meal.setOnItemSelectedListener(this);
        db = this.openOrCreateDatabase("relife",0,null);
        cal = (TextView) findViewById(R.id.tv_total_cal_num);
        //category = 點擊進來的那餐
        initData();
    }

    public void insert(View v) {
        int index = meal.getSelectedItemPosition();
        Intent it = new Intent(this, eat_new_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("eat_name", meals[index]);
        it.putExtras(bundle);
        startActivityForResult(it, 0);
    }
    float total_cal;
    public void initData() {
        total_cal = 0;
        db = this.openOrCreateDatabase("relife", 0, null);
        mData = new ArrayList<eat_listview_recipe>();
        Cursor c = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + category + "'", null);
        if(c.moveToFirst()){
            do {
                eat_listview_recipe record  = new eat_listview_recipe(c.getInt(0), c.getString(2), c.getInt(3), c.getDouble(4));
                mData.add(record);
                Cursor cal_in_food = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + c.getInt(3), null);
                if (cal_in_food.moveToFirst()) {
                    Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + category + "' AND foodID = " + c.getInt(3), null);
                    if(food_num.moveToFirst()) {
                        total_cal += cal_in_food.getFloat(2) * food_num.getFloat(4);
                    }
                }
                //search
                Cursor cal_in_food2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + c.getInt(3), null);
                if (cal_in_food2.moveToFirst()) {
                    Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + category + "' AND foodID = " + c.getInt(3), null);
                    if(food_num.moveToFirst()) {
                        total_cal += cal_in_food2.getFloat(2) * food_num.getFloat(4);
                    }
                }
                //--------
            } while (c.moveToNext());
        }
        cal.setText(String.valueOf((int) total_cal) + " 大卡");

        //  將布局添加到ListView中
        LayoutInflater layoutinflater =getLayoutInflater();
        // 創建自定義Adapter的對象
        recipe_adapter adapter = new recipe_adapter(layoutinflater,mData,this);
        lv_record.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = meals[position];
        eat_new_viewpager_recipe.eat = meals[position];
        initData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void back(View v) {
        finish();
    }
}
