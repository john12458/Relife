package com.mis.relife.pages.eat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.model.Diet;
import com.mis.relife.data.model.Food;

public class eat_new_recipe extends AppCompatActivity {
    TextView test;
    EditText ed_food, ed_cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_new_recipe);

        test = findViewById(R.id.txv_test);
        ed_food = findViewById(R.id.ed_food);
        ed_cal = findViewById(R.id.ed_cal);

    }
    public void cancel(View v){
        finish();
    }
    public void save(View v){
        Diet diet_insert = new Diet();
        Food food_insert = new Food();
        food_insert.food = ed_food.getText().toString();
        food_insert.cal = Integer.valueOf(ed_cal.getText().toString());
        food_insert.number = 1;
        diet_insert.category = 2;
        diet_insert.eatDate = "2019/4/27";
        diet_insert.foods.add(food_insert);

        AppDbHelper.insertDietToFireBase(diet_insert);
        finish();
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
