package com.mis.relife.pages.eat;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mis.relife.R;

public class eat_listview_recipe extends AppCompatActivity {

    SQLiteDatabase db;
    private int food_id, love_id, record_id;
    private double cal, number;
    private String name, category;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_listview_recipe);



    }

    public eat_listview_recipe(int id,String name, double cal){ // 食譜
        this.food_id = id;
        this.name = name;
        this.cal = cal;
    }

    public eat_listview_recipe(int loveid,int foodid){ // 我的最愛
        this.love_id = loveid;
        this.food_id = foodid;
    }

    public eat_listview_recipe(int recordid, String category, int foodid, double number){ // 記錄
        this.record_id = recordid;
        this.category = category;
        this.food_id = foodid;
        this.number = number;
    }

    public eat_listview_recipe(int food_id){ // 最近新增
        this.food_id = food_id;
    }

    public int getFood_id() {
        return food_id;
    }
    public void setRecipeId(int id) {
        this.food_id = id;
    }

    public double getCal() {
        return cal;
    }
    public void setCal(double cal) {
        this.cal = cal;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public void insert(View v){

    }


}
