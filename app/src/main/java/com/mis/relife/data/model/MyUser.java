package com.mis.relife.data.model;

import java.util.List;

public class MyUser {
    public List<Diet> diets;
    public Info info;
    public List<Sleep> sleeps;
    public List<Sport> sports;

    public MyUser(){}
    public MyUser(Info info){this.info = info;}

    @Override
    public String toString() {
        String txt="\n----- User ------\n";
        txt+=info.toString();
        for (int i = 0; i<diets.size(); i++)
            if(diets.get(i)!=null)
                txt +="\t"+ ((Diet)diets.get(i)).toString()+"\n";
            else
                txt+="\tnull to get diet"+"\n";
        txt+="\t----- Sport ------\n";
        for (int i = 0; i< sports.size(); i++)
            if(sports.get(i)!=null)
                txt +="\t"+ ((Sport) sports.get(i)).toString()+"\n";
            else
                txt+="\tnull to get sports"+"\n";
        txt+="\t----- Sleep ------\n";
        for (int i = 0; i< sleeps.size(); i++)
            if(sleeps.get(i)!=null)
                txt +="\t"+ ((Sleep) sleeps.get(i)).toString()+"\n";
            else
                txt+="\tnull to get sleeps"+"\n";
        txt+="------------\n";
        return txt;
    }
}
