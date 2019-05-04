package com.mis.relife.pages.eat;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EatSearchData {
    private final FragmentActivity activity;
    private AutoCompleteTextView auto_search;
    private Map<String, Food> foodCal;
    private List<String> dictionary;
    private ArrayAdapter<String> adapter;


    public EatSearchData(AutoCompleteTextView auto_search, FragmentActivity activity) {
        this.auto_search = auto_search;
        this.activity = activity;
        myInit();
    }

    private void dataInToAdapter(){
        dictionary = new ArrayList<String>();
        for (String key:foodCal.keySet()) {
            Food value = foodCal.get(key);
            Log.d("FoodCal",value.toString());
            dictionary.add(value.food);
        }
        adapter = new ArrayAdapter<>(activity.getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line,
                dictionary);
        auto_search.setThreshold(1);
        auto_search.setAdapter(adapter);
    }

    private void myInit(){
        if(foodCal==null){
            AppDbHelper.getAllFoodCalFromFireBase(new MyCallBack<Map<String, Food>>() {
                @Override
                public void onCallback(Map<String, Food> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                    foodCal = new HashMap();
                    foodCal.putAll(value);
                    dataInToAdapter();
                    dataRef.removeEventListener(vlistenr);
                }
            });
        }
    }
}
