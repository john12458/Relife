package com.mis.relife.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public interface MyCallBack<T>{
    void onCallback(T value, DatabaseReference dataRef, ValueEventListener vlistenr);
}
