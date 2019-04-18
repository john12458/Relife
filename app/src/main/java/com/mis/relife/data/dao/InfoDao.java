package com.mis.relife.data.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Info;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InfoDao implements MyDao<Info,Info>{
    private DatabaseReference infoRef;

    public InfoDao(int userId,FirebaseDatabase db){
        this.infoRef = db.getReference("user/"+userId+"/info");
    }
    @Override
    public Task<Void> insert(Object value) {return infoRef.setValue((Info)value);}

    @Override
    public Task<Void> update(String key,Object value) { return infoRef.child(key).setValue(value); }
    @Override
    public Task<Void> delete(String key) {
        return infoRef.child(key).setValue(null);
    }
    @Override
    public Task<Void> deleteAll() {
        return infoRef.setValue(null);
    }
    @Override
    public void load(String key, Object value, final MyCallBack<Info> myCallback) {
        infoRef.orderByChild(key).equalTo(String.valueOf(value)).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Info info = null;
                if (dataSnapshot.exists()) {
                    Log.d("dataSnapshot",dataSnapshot.getValue().toString());
                    info = dataSnapshot.getValue(Info.class);
                    Log.d("info_inner",info.toString());
                }else  Log.e("err","err");
                myCallback.onCallback(info,infoRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }

    @Override
    public void loadAll(final MyCallBack<Info> myCallback) {
        Log.d("InfoURL",infoRef.toString());
        infoRef.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Info info = null;
                if (dataSnapshot.exists()) {
                    Log.d("dataSnapshot",dataSnapshot.getValue().toString());
                    info = dataSnapshot.getValue(Info.class);
                    Log.d("info_inner",info.toString());
                }else  Log.e("err","err");
                myCallback.onCallback(info,infoRef,this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
        });
    }

}