package com.mis.relife.pages.sport;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Sport;
import com.mis.relife.pages.BaseViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SportData extends BaseViewModel {

    public List<String> sport_time = new ArrayList<>();
    public List<String> sport_cal = new ArrayList<>();
    public List<String> sport_name = new ArrayList<>();
    public List<String> sport_StartTime = new ArrayList<>();
    public List<String> sport_recordDate = new ArrayList<>();
    private  Map<String,Sport> sportMap;
    public String record_key = "";

    public SportData(){

    }

//    public void initial(){
//
//    }

    public String get_key(final String name, final String startTime, final String time, final String cal){
       for(String key : sportMap.keySet()) {
                    Sport sport_data = sportMap.get(key);
                    if(name.equals(sport_data.type) && startTime.equals(sport_data.startTime)
                            && time.equals(String.valueOf(sport_data.betweenTime)) &&
                            cal.equals(String.valueOf(sport_data.cal)))
                    {
                        record_key = key;
                        break;
                    }
                }
        return record_key;
    }

    @Override
    public void myInit() {
        AppDbHelper.getAllSportFromFireBase(new MyCallBack<Map<String, Sport>>() {
            @Override
            public void onCallback(Map<String, Sport> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                sportMap = new HashMap<>();
                sportMap.putAll(value);
                sport_name.clear();
                sport_time.clear();
                sport_cal.clear();
                sport_StartTime.clear();
                sport_recordDate.clear();
                for(String key : sportMap.keySet()) {
                    Sport sport_data = sportMap.get(key);
                    sport_name.add(sport_data.type);
                    sport_time.add(String.valueOf(sport_data.betweenTime));
                    sport_cal.add(String.valueOf(sport_data.cal));
                    sport_StartTime.add(sport_data.startTime);
                    sport_recordDate.add(sport_data.recordDate);
                    System.out.println("!!!!!!!!!!!!!!!!!!");
                }
            }
        });
    }

//    public void insetdata_sport_diary(String name, String time, String cal, String starttime){
//        sport_name.add(name);
//        sport_time.add(time);
//        sport_cal.add(cal);
//        sport_StartTime.add(starttime);
//        System.out.println("insert!!!!!!!!!!!!!!!!!!" + sport_name.get(2));
//        sport_tab_viewpager.sport_adapter.notifyDataSetChanged();
//    }
//
//    public void delete_sport_diary(int position) {
//        sport_name.remove(position);
//        sport_time.remove(position);
//        sport_cal.remove(position);
//        sport_StartTime.remove(position);
//        sport_tab_viewpager.sport_adapter.notifyDataSetChanged();
//    }
//
//    public void edit_sport_diary(String name, String time, String cal, String starttime, int position){
//        sport_name.set(position,name);
//        sport_time.set(position,time);
//        sport_cal.set(position,cal);
//        sport_StartTime.set(position,starttime);
//        System.out.println(position + ": " +name);
//        sport_tab_viewpager.sport_adapter.notifyDataSetChanged();
//    }
}
