package com.mis.relife.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Diet {
    public int category;
    public String eatDate;
    public List<Food> foods;

    public Diet(int category, String eatDate, List<Food> foods) {
        this.category = category;
        this.eatDate = eatDate;
        this.foods = foods;
    }

    public Diet(){}

    @Override
    public String toString() {
        String txt="\n----- Diet ------\n";
        txt += "category:\t"+ String.valueOf(category)+"\n"
                +"eatDate:\t"+eatDate+"\n";
        for (int i = 0; i<foods.size(); i++)
            if(foods.get(i)!=null)
                txt += ((Food)foods.get(i)).toString()+"\n";
            else
                txt+="null to get food"+"\n";
        txt+="----------------\n";
        return txt;
    }
}
