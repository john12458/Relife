//package com.mis.relife.data.dao;
//
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.mis.relife.MyCallBack;
//import com.mis.relife.data.model.Diet;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MytestDao<O,J,L> implements MyDao<J,L>{
//    private DatabaseReference myRef;
//    private MyCallBack<List<Diet>> myCallback;
//
//    public MytestDao(int userId,String url){
//        this.myRef = FirebaseDatabase.getInstance().getReference(url);
//    }
//
//
//    @Override
//    public Task<Void> insert(Object value) { return  myRef.push().setValue((O)value);}
//
//    @Override
//    public Task<Void> update(String key, Object value) {
//        return myRef.child(key).setValue((O)value);
//    }
//
//    @Override
//    public void load(String key, Object value, final MyCallBack<List<O>> myCallback) {
//        myRef.orderByChild(key).equalTo(String.valueOf(value)).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<O> dietList = new ArrayList<O>();
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        Log.d("dataSnapshot",data.getValue().toString());
////                        O diet = data.getValue(O.class);
//                        Log.d("diet_inner",diet.toString());
//                        dietList.add(diet);
//                    }
//                }else  Log.e("err","err");
//                myCallback.onCallback(dietList);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
//        });
//    }
//
//    @Override
//    public void loadAll(final MyCallBack<L> myCallback) {
//        Log.d("InfoURL", myRef.toString());
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Diet> dietList = new ArrayList<Diet>();
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        Log.d("dataSnapshot",data.getValue().toString());
//                        Diet diet = data.getValue(Diet.class);
//                        Log.d("diet_inner",diet.toString());
//                        dietList.add(diet);
//                    }
//
//                }else  Log.e("err","err");
//                myCallback.onCallback(dietList);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {throw databaseError.toException();}
//        });
//    }
//
//
//}
