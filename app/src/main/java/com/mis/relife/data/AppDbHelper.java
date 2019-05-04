package com.mis.relife.data;

import com.mis.relife.data.dao.DietDao;
import com.mis.relife.data.dao.FoodCalDao;
import com.mis.relife.data.dao.InfoDao;
import com.mis.relife.data.dao.SleepDao;
import com.mis.relife.data.dao.SportDao;
import com.mis.relife.data.model.Diet;
import com.mis.relife.data.model.Food;
import com.mis.relife.data.model.Info;
import com.mis.relife.data.model.Sleep;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.mis.relife.data.model.Sport;

import java.util.Map;

public class AppDbHelper {
    private static String userId = "";
    public static FirebaseDatabase mFirebase;
    private static DietDao dietDao;
    private static InfoDao infoDao;
    private static SleepDao sleepDao;
    private static SportDao sportDao;
    private static FoodCalDao foodCalDao;

    public AppDbHelper(String userId){
        this.userId = userId;
        this.mFirebase = FirebaseDatabase.getInstance();
        this.dietDao = new DietDao(userId,mFirebase);
        this.infoDao = new InfoDao(userId,mFirebase);
        this.sleepDao = new SleepDao(userId,mFirebase);
        this.sportDao = new SportDao(userId,mFirebase);
        this.foodCalDao = new FoodCalDao(mFirebase);
    }
    // FoofCal
    public static void getAllFoodCalFromFireBase(MyCallBack<Map<String, Food>> myCallback) {foodCalDao.loadAll(myCallback);}
    // Diet
    public static Task<Void> insertDietToFireBase(Diet value) {return dietDao.insert(value); }
    public static Task<Void> updateDietToFireBase(String key, Object value) {return dietDao.update(key,value); }
    public static Task<Void> deleteDietToFireBase(String key) {return dietDao.delete(key); }
    public static Task<Void> deleteAllDietToFireBase() {return dietDao.deleteAll(); }
    public static void getAllDietFromFireBase(MyCallBack<Map<String,Diet>> myCallback) {dietDao.loadAll(myCallback);}
    public static void getDietFromFireBaseWhere(String key,Object value,MyCallBack< Map<String,Diet>> myCallback) {dietDao.load(key,value,myCallback);}
    // Sleep
    public static Task<Void> insertSleepToFireBase(Sleep value) {return sleepDao.insert(value); }
    public static Task<Void> updateSleepToFireBase(String key, Object value) {return sleepDao.update(key,value); }
    public static Task<Void> deleteSleepToFireBase(String key) {return sleepDao.delete(key); }
    public static Task<Void> deleteAllSleepToFireBase() {return sleepDao.deleteAll(); }
    public static void getAllSleepFromFireBase(MyCallBack<Map<String,Sleep>> myCallback) {sleepDao.loadAll(myCallback);}
    public static void getSleepFromFireBaseWhere(String key,Object value,MyCallBack<Map<String,Sleep>> myCallback) {sleepDao.load(key,value,myCallback);}
    // Info
    public static Task<Void> insertInfoToFireBase(Info value) {return infoDao.insert(value); }
    public static Task<Void> deleteInfoToFireBase(String key) {return infoDao.delete(key); }
    public static Task<Void> deleteAllInfoToFireBase() {return infoDao.deleteAll(); }
    public static Task<Void> updateInfoToFireBase(String key, Object value) {return infoDao.update(key,value); }
    public static void getAllInfoFromFireBase(MyCallBack<Info> myCallback) {infoDao.loadAll(myCallback);}
    // Sport
    public static Task<Void> insertSportToFireBase(Sport value) {return sportDao.insert(value); }
    public static Task<Void> updateSportToFireBase(String key, Object value) {return sportDao.update(key,value); }
    public static Task<Void> deleteSportToFireBase(String key) {return sportDao.delete(key); }
    public static Task<Void> deleteAllSportToFireBase() {return sportDao.deleteAll(); }
    public static void getAllSportFromFireBase(MyCallBack<Map<String, Sport>> myCallback) {sportDao.loadAll(myCallback);}
    public static void getSportFromFireBaseWhere(String key,Object value,MyCallBack<Map<String,Sport>> myCallback) {sportDao.load(key,value,myCallback);}

}
