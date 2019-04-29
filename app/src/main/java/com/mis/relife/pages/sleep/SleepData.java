package com.mis.relife.pages.sleep;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Sleep;
import com.mis.relife.data.model.Sport;
import com.mis.relife.pages.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SleepData extends BaseViewModel {
    public List<String> sleep_time = new ArrayList<>();
    public List<String> wake_time = new ArrayList<>();
    public List<String> record_date = new ArrayList<>();
    public List<String> description = new ArrayList<>();
    public List<String> mood = new ArrayList<>();
    private Map<String, Sleep> sleepMap;
    public String record_key = "";


    public String get_key(final String sleep_time, final String wake_time, final String record_date, final String description){
        for(String key : sleepMap.keySet()) {
            Sleep sleep_data = sleepMap.get(key);
            if(sleep_time.equals(sleep_data.sleepTime) && wake_time.equals(sleep_data.wakeTime)
                    && record_date.equals(String.valueOf(sleep_data.recordDate)) &&
                    description.equals(String.valueOf(sleep_data.description)))
            {
                record_key = key;
                break;
            }
        }
        return record_key;
    }

    @Override
    public void myInit() {
        AppDbHelper.getAllSleepFromFireBase(new MyCallBack<Map<String, Sleep>>() {
            @Override
            public void onCallback(Map<String, Sleep> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                sleepMap = new HashMap<>();
                sleepMap.putAll(value);
                sleep_time.clear();
                wake_time.clear();
                record_date.clear();
                description.clear();
                mood.clear();
                for(String key : sleepMap.keySet()) {
                    Sleep sleep_data = sleepMap.get(key);
                    sleep_time.add(sleep_data.sleepTime);
                    wake_time.add(String.valueOf(sleep_data.wakeTime));
                    record_date.add(String.valueOf(sleep_data.recordDate));
                    description.add(sleep_data.description);
                    mood.add(sleep_data.mood);
                    System.out.println("!!!!!!!!!!!!!!!!!!");
                }
            }
        });
    }
}

