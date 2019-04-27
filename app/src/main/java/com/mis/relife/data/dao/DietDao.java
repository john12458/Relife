package com.mis.relife.data.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Diet;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DietDao implements MyDao<Map<String,Diet>, Map<String,Diet>> {
    private DatabaseReference dietRef;
    private MyCallBack<List<Diet>> myCallback;

    public DietDao(String userId,FirebaseDatabase db){
        this.dietRef = db.getReference("user/"+userId+"/diets");
    }


    @Override
    public Task<Void> insert(Object value) { return  dietRef.push().setValue((Diet)value);}

    @Override
    public Task<Void> update(String key, Object value) {
        return dietRef.child(key).setValue((Diet)value);
    }

    @Override
    public Task<Void> delete(String key) {
        return dietRef.child(key).setValue(null);
    }

    @Override
    public Task<Void> deleteAll() {
        return dietRef.setValue(null);
    }

    @Override
    public void load(String key,Object value,final MyCallBack<Map<String,Diet>> myCallback) {
        Log.d("InfoURL",dietRef.toString());
        dietRef.orderByChild(key).equalTo(String.valueOf(value)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Diet> dietList = new HashMap<String, Diet>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot",data.getValue().toString());
                        Diet diet = data.getValue(Diet.class);
                        Log.d("diet_inner",diet.toString());
                        dietList.put(data.getKey(),diet);
                    }
                }else  Log.e("err","err");
                myCallback.onCallback(dietList,dietRef,this);
        }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }

    @Override
    public void loadAll(final MyCallBack< Map<String,Diet>> myCallback) {
        Log.d("InfoURL",dietRef.toString());
        dietRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Diet>dietList = new HashMap<String,Diet>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("key",data.getKey().toString());
                        Diet diet = data.getValue(Diet.class);
                        Log.d("diet_inner",diet.toString());
                        dietList.put(data.getKey(),diet);
                    }
                }else  Log.e("err","err");
                myCallback.onCallback(dietList,dietRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }



}
