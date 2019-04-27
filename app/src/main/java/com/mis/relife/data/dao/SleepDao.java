package com.mis.relife.data.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Sleep;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SleepDao implements MyDao<Map<String,Sleep>,Map<String,Sleep>> {
    private DatabaseReference sleepRef;
    public SleepDao(String userId,FirebaseDatabase db){
        this.sleepRef = db.getReference("user/"+userId+"/sleeps");
    }

    @Override
    public Task<Void> insert(Object value) {return  sleepRef.push().setValue((Sleep)value);}


    @Override
    public Task<Void> update(String key, Object value) {
        return null;
    }
    @Override
    public Task<Void> delete(String key) {
        return sleepRef.child(key).setValue(null);
    }
    @Override
    public Task<Void> deleteAll() {
        return sleepRef.setValue(null);
    }
    @Override
    public void load(String key, Object value, final MyCallBack<Map<String,Sleep>> myCallback) {
        sleepRef.orderByChild(key).equalTo(String.valueOf(value)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Sleep> sleepList = new HashMap<String,Sleep>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot",data.getValue().toString());
                        Sleep sleep = data.getValue(Sleep.class);
                        Log.d("sleep_inner",sleep.toString());
                        sleepList.put(data.getKey(),sleep);
                    }

                }else  Log.e("err","err");
                myCallback.onCallback(sleepList,sleepRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }

    @Override
    public void loadAll(final MyCallBack<Map<String,Sleep>> myCallback) {
        sleepRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Sleep> sleepList = new HashMap<String,Sleep>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot",data.getValue().toString());
                        Sleep sleep = data.getValue(Sleep.class);
                        Log.d("sleep_inner",sleep.toString());
                        sleepList.put(data.getKey(),sleep);
                    }

                }else  Log.e("err","err");
                myCallback.onCallback(sleepList,sleepRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }



}
