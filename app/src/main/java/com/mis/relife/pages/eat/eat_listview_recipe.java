package com.mis.relife.pages.eat;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mis.relife.R;

public class eat_listview_recipe extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_listview_recipe);


    }
    public eat_listview_recipe(String name, int cal){
        this.name = name;
        this.cal = cal;
    }
    private int cal;
    private String name;

    public int getCal() {
        return cal;
    }
    public void setCal(int cal) {
        this.cal = cal;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void insert(View v){

    }
}
