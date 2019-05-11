package com.mis.relife.data.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Info {

    public String id;
    public String account;
    public String password;
    public int life;

    public int height;
    public int weight;
    public int goalWeight;
    public int old;
    public String gender;

    public Info(){}

    public Info(String id, String account, String password, int life, int height, int weight, int goalWeight, int old, String gender) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.life = life;
        this.height = height;
        this.weight = weight;
        this.goalWeight = goalWeight;
        this.old = old;
        this.gender = gender;
    }

    public Info(String id, String account, String password, int life) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.life = life;
    }

    @Override
    public String toString() {
        String txt="\n----- Info ------\n";
        txt += "id:\t"+ String.valueOf(id)+"\n"
                +"account:\t"+account+"\n"
                +"password:\t"+password+"\n"
                +"life:\t"+String.valueOf(life)+"\n"
                +"height:\t"+String.valueOf(height)+"\n"
                +"weight:\t"+String.valueOf(weight)+"\n"
                +"goalWeight:\t"+String.valueOf(goalWeight)+"\n"
                +"old:\t"+String.valueOf(old)+"\n"
                +"gender:\t"+gender+"\n";
        txt+="----------------\n";
        return txt;
    }
}
