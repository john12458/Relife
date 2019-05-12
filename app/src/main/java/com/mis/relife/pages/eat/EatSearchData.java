package com.mis.relife.pages.eat;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EatSearchData {
    private final FragmentActivity activity;
    private final LayoutInflater layoutInflater;
    private final Context context;
    private ListView lv_new;
    private EditText ed_search;
    private Map<String, Food> foodCal;
    private ProgressDialog pd;

    public EatSearchData(EditText ed_search, ListView lv_new, FragmentActivity activity, LayoutInflater layoutInflater, Context context) {
        this.activity = activity;
        this.ed_search = ed_search;
        this.layoutInflater = layoutInflater;
        this.lv_new = lv_new;
        this.context = context;
        myInit();
    }
    private void myInit(){
        if(foodCal==null){
            AppDbHelper.getAllFoodCalFromFireBase(new MyCallBack<Map<String, Food>>() {
                @Override
                public void onCallback(Map<String, Food> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                    foodCal = new HashMap();
                    foodCal.putAll(value);
                    onDataChange();
                    dataRef.removeEventListener(vlistenr);
                }
            });
        }
    }
    private void onDataChange(){
        if(!checkSearchTableIsNewest()){
            pd = ProgressDialog.show(activity,
                    "資料上傳", "請等待...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("SQLite Thread","insert Now");
                    for (String key:foodCal.keySet()) {
                        Food value = foodCal.get(key);
                        insertIntoSQLite(Integer.parseInt(key),value.food,value.cal);
                    }
                    pdHandler.sendEmptyMessage(0);
                }
            }).start();
        }
        ed_search.addTextChangedListener(searchWatcher);
    }
    private Handler pdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pd.dismiss();
        }
    };
    private boolean checkSearchTableIsNewest(){
        SQLiteDatabase db = activity.openOrCreateDatabase("relife",0,null);
        Cursor cursor = db.rawQuery("SELECT * FROM search", null);
        Log.d("c_getCnt  foddCal_siaze",cursor.getCount() +"___"+ foodCal.size());
        boolean result = cursor.getCount() == foodCal.size();
        db.close();
        return result;
    }
    private void insertIntoSQLite(int foodID,String name,double cal){
        SQLiteDatabase db = activity.openOrCreateDatabase("relife",0,null);
        ContentValues cv = new ContentValues(3);
        cv.put("foodID",foodID);
        cv.put("name", name);
        cv.put("cal", cal);
        db.insert("search", null, cv);
        db.close();
    }
    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            lv_new.setAdapter(new recipe_adapter(layoutInflater, getDictionary(s),context,(eat_new_activity) activity));
        }
        @Override
        public void afterTextChanged(Editable s) { }
    };
    private List<eat_listview_recipe> getDictionary(CharSequence s){
        List<eat_listview_recipe> dictionary = new ArrayList<eat_listview_recipe>();
        for (String key:foodCal.keySet()) {
            Food value = foodCal.get(key);
            if(value.food.matches(".*"+s+".*"))
                dictionary.add(new eat_listview_recipe(Integer.parseInt(key),value.food,value.cal));
        }
        return dictionary;
    }

    public void notifyChange(){
        lv_new.setAdapter(new recipe_adapter(layoutInflater, getDictionary(ed_search.getText().toString()), context,(eat_new_activity) activity));
    }

}
