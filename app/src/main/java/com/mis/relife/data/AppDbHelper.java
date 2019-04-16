package com.mis.relife.data;

import com.mis.relife.data.dao.DietDao;
import com.mis.relife.data.dao.InfoDao;
import com.mis.relife.data.dao.SleepDao;
import com.mis.relife.data.dao.SportDao;
import com.mis.relife.data.model.Diet;
import com.mis.relife.data.model.Info;
import com.mis.relife.data.model.Sleep;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AppDbHelper {
    private static int userId = 0;
//    private static FirebaseDatabase mFirebase;
    private static DietDao dietDao;
    private static InfoDao infoDao;
    private static SleepDao sleepDao;
    private static SportDao sportDao;

    public AppDbHelper(int userId){
        this.userId = userId;
//        this.mFirebase = FirebaseDatabase.getInstance();
        this.dietDao = DietDao.getInstance(userId);
        this.infoDao = InfoDao.getInstance(userId);
        this.sleepDao = SleepDao.getInstance(userId);
        this.sportDao = SportDao.getInstance(userId);
    }

    // Diet
    public static Task<Void> insertDietToFireBase(Diet value) {return dietDao.insert(value); }
    public static Task<Void> updateDietToFireBase(String key, Object value) {return dietDao.update(key,value); }
    public static void getAllDietFromFireBase(MyCallBack<Map<String,Diet>> myCallback) {dietDao.loadAll(myCallback);}
    public static void getDietFromFireBaseWhere(String key,Object value,MyCallBack< Map<String,Diet>> myCallback) {dietDao.load(key,value,myCallback);}
    // Sleep
    public static Task<Void> insertSleepToFireBase(Sleep value) {return sleepDao.insert(value); }
    public static Task<Void> updateSleepToFireBase(String key, Object value) {return sleepDao.update(key,value); }
    public static void getAllSleepFromFireBase(MyCallBack<Map<String,Sleep>> myCallback) {sleepDao.loadAll(myCallback);}
    public static void getSleepFromFireBaseWhere(String key,Object value,MyCallBack<Map<String,Sleep>> myCallback) {sleepDao.load(key,value,myCallback);}
    // Info
    public static Task<Void> insertInfoToFireBase(Info value) {return infoDao.insert(value); }
    public static Task<Void> updateInfoToFireBase(String key, Object value) {return infoDao.update(key,value); }
    public static void getAllInfoFromFireBase(MyCallBack<Info> myCallback) {infoDao.loadAll(myCallback);}
}
