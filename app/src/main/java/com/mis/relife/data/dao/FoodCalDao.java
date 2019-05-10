package com.mis.relife.data.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Food;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodCalDao implements  MyDao<Map<String, Food>, Map<String,Food>>{
    private DatabaseReference foodCalRef;
    private MyCallBack<List<Food>> myCallback;

    public FoodCalDao(FirebaseDatabase db){
        this.foodCalRef = db.getReference("foodCal");
    }

    @Override
    public Task<Void> insert(Object value) {
        return null;
    }

    @Override
    public Task<Void> update(String key, Object value) {
        return null;
    }

    @Override
    public Task<Void> delete(String key) {
        return null;
    }

    @Override
    public Task<Void> deleteAll() {
        return null;
    }

    @Override
    public void load(String key, Object value, MyCallBack<Map<String, Food>> myCallback) {

    }

    @Override
    public void loadAll(final MyCallBack<Map<String, Food>> myCallback) {
        Log.d("foodCalURL",foodCalRef.toString());
        foodCalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Food> foodCalList = new HashMap<String, Food>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot",data.getValue().toString());
                        Food food = data.getValue(Food.class);
                        Log.d("foodCal_inner",food.toString());
                        foodCalList.put(data.getKey(),food);
                    }
                }else  Log.e("err","err");
                myCallback.onCallback(foodCalList,foodCalRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }
}
