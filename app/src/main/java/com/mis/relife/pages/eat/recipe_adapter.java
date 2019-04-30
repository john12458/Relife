package com.mis.relife.pages.eat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mis.relife.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class recipe_adapter extends BaseAdapter implements View.OnClickListener {

    private eat_new_activity activity;
    private Context context;
    private List<eat_listview_recipe> mData; // 定義數據
    private LayoutInflater mInflater; // 定義Inflater, 加载自定義的布局
    SQLiteDatabase db;

    // 定義構造器, 在Activity創建對象Adapter的時候將數據data和Inflater傳入自定義的Adapter中進行處理

    public recipe_adapter(LayoutInflater inflater,List<eat_listview_recipe> data,Context context,eat_new_activity activity){
        mInflater = inflater;
        mData = data;
        this.context = context;
        this.activity = activity;
        db = context.openOrCreateDatabase("relife",0,null);

    }

    public recipe_adapter(LayoutInflater inflater,List<eat_listview_recipe> data,Context context){
        mInflater = inflater;
        mData = data;
        this.context = context;
        db = context.openOrCreateDatabase("relife",0,null);

    }

    @Override
    public int getCount() {
            return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
            return position;
    }

    TextView name;
    ImageView love, add;
    Date date = new Date();
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy/MM/dd");
    String choose_date = date_format.format(date);

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        // 獲得ListView中的view
        View viewRecipe = mInflater.inflate(R.layout.eat_listview_recipe,null);
        // 獲得食譜對象
        eat_listview_recipe recipe = mData.get(position);
        // 獲得自定義布局中每一個控件的對象
        name = (TextView) viewRecipe.findViewById(R.id.information);
        love = (ImageView) viewRecipe.findViewById(R.id.btn_love);
        add = (ImageView) viewRecipe.findViewById(R.id.btn_add);
        switch (viewGroup.getId()) {
            case R.id.listview_recipe:
                // 將數據一一添加到自定義的布局中
                name.setText(recipe.getName() + "(" + recipe.getCal() + "大卡)");
                break;
            case R.id.listview_love:
                Cursor c = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + recipe.getFood_id(), null); //我的最愛中的foodID
                if(c.moveToFirst()) {
                    name.setText(c.getString(0) + "(" + c.getDouble(2) + "大卡)");
                }
                break;
            case R.id.listview_record:
                Cursor today_record = db.rawQuery("SELECT * FROM record WHERE date = '" + choose_date + "' AND category = '" + eat_new_second.category + "' AND foodID = " + recipe.getFood_id(), null);
                if(today_record.moveToFirst()) {
                    Cursor foodinformation = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + recipe.getFood_id(), null);
                    if(foodinformation.moveToFirst()) {
                        name.setText(foodinformation.getString(0) + "(" + foodinformation.getDouble(2) + "大卡)");
                    }
                }
                break;
        }
        Cursor food_in_love = db.rawQuery("SELECT * FROM love WHERE foodID = " + recipe.getFood_id(), null);
        if(food_in_love.moveToFirst()){
            love.setImageResource(R.drawable.heart2);
        }
        Cursor food_in_record = db.rawQuery("SELECT * FROM record WHERE  date = '" + choose_date + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe.getFood_id(), null);
        if(food_in_record.moveToFirst()){
            add.setImageResource(R.drawable.checked2);
        }

        viewRecipe.setOnClickListener(this);
        love.setOnClickListener(this);
        add.setOnClickListener(this);
        love.setTag(recipe.getFood_id());
        add.setTag(recipe.getFood_id());

        return viewRecipe ;
    }


    @Override
    public void onClick(View v) {
        int recipe_id;
        switch(v.getId()) {
            case R.id.btn_love:
                ImageView love = (ImageView) v;
                recipe_id = (int) v.getTag();
                Cursor c = db.rawQuery("SELECT * FROM love WHERE foodID = " + recipe_id, null);
                ContentValues cv = new ContentValues(1);
                cv.put("foodID", recipe_id);
                if(c.getCount() == 0) {
                    db.insert("love", null, cv);
                    love.setImageResource(R.drawable.heart2);
                }
                else {
                    db.delete("love", "foodID = " + recipe_id, null);
                    love.setImageResource(R.drawable.heart);
                }
                if(activity!=null) activity.initAll();
                break;
            case R.id.btn_add:
                ImageView add = (ImageView)v;
                recipe_id = (int) v.getTag();
                Cursor food_in_today_meal = db.rawQuery("SELECT * FROM record WHERE date = '" + choose_date + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, null);
                ContentValues cv_record = new ContentValues(4);
                cv_record.put("date", choose_date);
                cv_record.put("category", eat_new_viewpager_recipe.eat);
                cv_record.put("foodID", recipe_id);
                cv_record.put("number", 1);
                if(food_in_today_meal.getCount() == 0) {
                    db.insert("record", null, cv_record);
                    add.setImageResource(R.drawable.checked2);
                }
                else {
                    db.delete("record", "date = '" + choose_date + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, null);
                    add.setImageResource(R.drawable.checked);
                }

                break;
        }

    }


}
