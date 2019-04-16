package com.mis.relife.pages.eat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mis.relife.R;

public class eat_new_recipe extends AppCompatActivity {
    TextView test;
    EditText ed_food, ed_cal;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_new_recipe);

        test = (TextView)findViewById(R.id.txv_test);
        ed_food = (EditText)findViewById(R.id.ed_food);
        ed_cal = (EditText)findViewById(R.id.ed_cal);
        db = openOrCreateDatabase("relife",0,null);

    }
    public void cancel(View v){
        db.close();
        eat_new_recipe.this.finish();
    }
    public void save(View v){
        ContentValues cv = new ContentValues(2);
        cv.put("name", ed_food.getText().toString());
        cv.put("cal", ed_cal.getText().toString());
        Cursor c = db.rawQuery("SELECT * FROM recipe", null);
        int num = c.getCount();
        db.insert("recipe", null, cv);
        Cursor c2 = db.rawQuery("SELECT * FROM recipe", null);
        if(num == c2.getCount()) {
            Snackbar.make(findViewById(R.id.eat_new_reciipe), "食譜已經存在囉 ! ", Snackbar.LENGTH_SHORT).show();
        }
        else {
            db.close();
            eat_new_recipe.this.finish();
        }
    }
    public void test(View v){
        Intent intent_new_recipe = new Intent();
        intent_new_recipe.setClass(this, eat_new_second.class);
        startActivityForResult(intent_new_recipe, 0);
    }
    public void test2(View v){
        new AlertDialog.Builder(this).setTitle("牛角麵包378大卡(1個)，份數 : ").setIcon(R.drawable.pen).setView(new EditText(this)).setPositiveButton("確認", null).setNegativeButton("取消", null).show();
    }
}
