package com.mis.relife.data.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Sport;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SportDao implements MyDao<Map<String,Sport>, Map<String,Sport>> {
    private DatabaseReference sportRef;
    public SportDao(String userId,FirebaseDatabase db){
        this.sportRef = db.getReference("user/"+userId+"/sports");
    }


    @Override
    public Task<Void> insert(Object value) {
        return sportRef.push().setValue((Sport)value);
    }

    @Override
    public Task<Void> update(String key, Object value) {
        return sportRef.child(key).setValue(value);
    }
    @Override
    public Task<Void> delete(String key) {
        return sportRef.child(key).setValue(null);
    }
    @Override
    public Task<Void> deleteAll() {return sportRef.setValue(null);    }
    @Override
    public void load(String key, Object value, final MyCallBack<Map<String,Sport>> myCallback) {
        Log.d("InfoURL",sportRef.toString());
        sportRef.orderByChild(key).equalTo(String.valueOf(value)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Sport> sportList = new HashMap<String,Sport>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot",data.getValue().toString());
                        Sport sport = data.getValue(Sport.class);
                        Log.d("sport_inner",sport.toString());
                        sportList.put(data.getKey(),sport);
                    }
                }else  Log.e("err","err");
                myCallback.onCallback(sportList,sportRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }


    @Override
    public void loadAll(final MyCallBack< Map<String,Sport>> myCallback) {
        sportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Sport> sportList = new HashMap<String,Sport>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Log.d("dataSnapshot",data.getValue().toString());
                        Sport sport = data.getValue(Sport.class);
                        Log.d("sport_inner",sport.toString());
                        sportList.put(data.getKey(),sport);
                    }

                }else  Log.e("err","err");
                myCallback.onCallback(sportList,sportRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }
}
