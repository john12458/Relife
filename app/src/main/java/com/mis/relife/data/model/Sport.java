package com.mis.relife.data.model;

public class Sport {
    public int betweenTime;
    public int cal;
    public String startTime;
    public String type;

    public Sport(int betweenTime, int cal, String startTime, String type) {
        this.betweenTime = betweenTime;
        this.cal = cal;
        this.startTime = startTime;
        this.type = type;
    }

    public Sport(){}

    @Override
    public String toString() {
        String txt="\n\t--- sleeps ---\n";
        txt+="\tbetweenTime:\t"+betweenTime+"\n";
        txt+="\tcal:\t"+cal+"\n";
        txt+="\tstartTime:\t"+startTime+"\n";
        txt+="\ttype:\t"+type+"\n";
        txt+="\t------------\n";
        return txt;
    }
}
