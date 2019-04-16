package com.mis.relife.pages.eat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.mis.relife.R;

public class eat_new_second extends AppCompatActivity{

    Spinner meal;
    String[] meals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_new_second);

        meal = (Spinner)findViewById(R.id.spinner);
        meals = getResources().getStringArray(R.array.meal);
    }

    public void insert(View v) {
        int index = meal.getSelectedItemPosition();
        Intent it = new Intent(this, eat_new_activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("eat_name", meals[index]);
        it.putExtras(bundle);
        startActivityForResult(it, 0);
    }
}
