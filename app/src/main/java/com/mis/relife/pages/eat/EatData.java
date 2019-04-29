package com.mis.relife.pages.eat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Diet;
import com.mis.relife.data.model.Food;
import com.mis.relife.data.model.Sport;
import com.mis.relife.pages.BaseViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EatData extends BaseViewModel {

    public List<Integer> category = new ArrayList<>();
    public List<String> eatDate = new ArrayList<>();
    public List<List<Food>> foods = new ArrayList<>();
    public List<Food> recipe_foods = new ArrayList<>();
    public String record_key = "";
    private Map<String,Diet> eatMap;


    public String get_key(final int category, final Food food){
        for(String key : eatMap.keySet()) {
            Diet eat_data = eatMap.get(key);
            if(category == eat_data.category &&  food.food.equals(eat_data.foods.get(0).food) &&
                    food.number == eat_data.foods.get(0).number &&
                    food.cal == eat_data.foods.get(0).cal)
            {
                record_key = key;
                break;
            }
        }
        return record_key;
    }

    @Override
    public void myInit() {
        AppDbHelper.getAllDietFromFireBase(new MyCallBack<Map<String, Diet>>() {
            @Override
            public void onCallback(Map<String, Diet> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                eatMap = new HashMap<>();
                eatMap.putAll(value);
                category.clear();
                eatDate.clear();
                foods.clear();
                recipe_foods.clear();
                for(String key : eatMap.keySet()){
                    Diet diet_data = eatMap.get(key);
                    category.add(diet_data.category);
                    eatDate.add(diet_data.eatDate);
                    foods.add(diet_data.foods);
                    if(diet_data.category == 2){
                        for(int i = 0;i < diet_data.foods.size();i++) {
                            recipe_foods.add(diet_data.foods.get(i));
                        }
                    }
                }
            }
        });
    }
}
