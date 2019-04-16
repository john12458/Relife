package com.mis.relife.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Sleep {
    public String description;
    public String mood;
    public String sleepTime;
    public String wakeTime;

    public Sleep(String description, String mood, String sleepTime, String wakeTime) {
        this.description = description;
        this.mood = mood;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
    }

    public Sleep(){}
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("description", description);
        result.put("mood", mood);
        result.put("sleepTime", sleepTime);
        result.put("wakeTime",wakeTime);
        return result;
    }
    @Override
    public String toString() {
        String txt="\n\t--- sleeps ---\n";
        txt+="\tdescription:\t"+description+"\n";
        txt+="\tmood:\t"+mood+"\n";
        txt+="\tsleepTime:\t"+sleepTime+"\n";
        txt+="\twakeTime:\t"+wakeTime+"\n";
        txt+="\t------------\n";
        return txt;
    }
}
