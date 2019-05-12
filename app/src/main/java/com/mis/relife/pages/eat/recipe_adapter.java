package com.mis.relife.pages.eat;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mis.relife.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class recipe_adapter extends BaseAdapter implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

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

    TextView name, num;
    ImageView love, add;

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
        num = (TextView) viewRecipe.findViewById(R.id.txv_number);
        switch (viewGroup.getId()) {
            case R.id.listview_detect:
                name.setText(recipe.getName() + "(" + recipe.getCal() + "大卡)");
                break;
            case R.id.listview_new:
                name.setText(recipe.getName() + "(" + recipe.getCal() + "大卡)");
                break;
            case R.id.listview_recipe:
                // 將數據一一添加到自定義的布局中
                name.setText(recipe.getName() + "(" + recipe.getCal() + "大卡)");
                break;
            case R.id.listview_love:
                Cursor c = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + recipe.getFood_id(), null); //我的最愛中的foodID
                if(c.moveToFirst()) {
                    name.setText(c.getString(0) + "(" + c.getDouble(2) + "大卡)");
                }
                //search
                Log.e("getFoodid", String.valueOf(recipe.getFood_id()));
                    Cursor c2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + recipe.getFood_id(), null); //我的最愛中的foodID
                    if(c2.moveToFirst()) {
                        Log.e("getFoodName", c2.getString(0));
                        name.setText(c2.getString(0) + "(" + c2.getDouble(2) + "大卡)");
                    }
                //----
                break;
            case R.id.listview_record:
                Cursor today_record = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "' AND foodID = " + recipe.getFood_id(), null);
                if(today_record.moveToFirst()) {
                    num.setText(today_record.getFloat(4) + "份");
                    Cursor foodinformation = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + recipe.getFood_id(), null);
                    if(foodinformation.moveToFirst()) {
                        name.setText(foodinformation.getString(0) + "(" + foodinformation.getDouble(2) + "大卡)");
                    }
                }
                // search
                Cursor today_record2 = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "' AND foodID = " + recipe.getFood_id(), null);
                if(today_record2.moveToFirst()) {
                    Cursor foodinformation2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + recipe.getFood_id(), null);
                    if(foodinformation2.moveToFirst()) {
                        name.setText(foodinformation2.getString(0) + "(" + foodinformation2.getDouble(2) + "大卡)");
                    }
                }
                //--
                break;
            case R.id.listview_recent:
                Cursor recent_foodid = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + recipe.getFood_id(), null);
                if(recent_foodid.moveToFirst()) {
                    name.setText(recent_foodid.getString(0) + "(" + recent_foodid.getDouble(2) + "大卡)");
                }
                // search
                Cursor recent_foodid2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + recipe.getFood_id(), null);
                if(recent_foodid2.moveToFirst()) {
                    name.setText(recent_foodid2.getString(0) + "(" + recent_foodid2.getDouble(2) + "大卡)");
                }
                break;
        }
        Cursor food_in_love = db.rawQuery("SELECT * FROM love WHERE foodID = " + recipe.getFood_id(), null);
        if(food_in_love.moveToFirst()){
            love.setImageResource(R.drawable.heart2);
        }
        Cursor food_in_record = db.rawQuery("SELECT * FROM record WHERE  date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe.getFood_id(), null);
        if(food_in_record.moveToFirst()){
            add.setImageResource(R.drawable.checked2);
        }

        viewRecipe.setOnClickListener(this);
        love.setOnClickListener(this);
        add.setOnClickListener(this);
        name.setOnClickListener(this);
        love.setTag(recipe.getFood_id());
        add.setTag(recipe.getFood_id());
        name.setTag(recipe.getFood_id());
        ListView lv = (ListView)viewGroup;
        lv.setOnItemLongClickListener(this);
        lv.setOnItemClickListener(this);

        return viewRecipe ;
    }


    @Override
    public void onClick(View v) {
        final int recipe_id;
        switch(v.getId()) {
            case R.id.btn_love:
                ImageView love = (ImageView) v;
                recipe_id = (int) v.getTag();
                Cursor c = db.rawQuery("SELECT * FROM love WHERE foodID = " + recipe_id, null);
                ContentValues cv = new ContentValues(1);
                cv.put("foodID", recipe_id);///////////
                if(c.getCount() == 0) {
                    db.insert("love", null, cv);
                    love.setImageResource(R.drawable.heart2);
                    Toast.makeText(context, "已新增至我的最愛 ! ", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.delete("love", "foodID = " + recipe_id, null);
                    love.setImageResource(R.drawable.heart);
                    Toast.makeText(context, "已從我的最愛移除 ! ", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add:
                ImageView add = (ImageView)v;
                recipe_id = (int) v.getTag();
                Log.e("recipe_id", String.valueOf(recipe_id));
                Cursor food_in_today_meal = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, null);
                Cursor food_in_recent = db.rawQuery("SELECT * FROM recent WHERE foodID = " + recipe_id, null);
                ContentValues cv_record = new ContentValues(4);
                cv_record.put("date", eat_page_activity.selectdate);
                cv_record.put("category", eat_new_viewpager_recipe.eat);
                cv_record.put("foodID", recipe_id);
                cv_record.put("number", 1);
                if(food_in_today_meal.getCount() == 0) {
                    db.insert("record", null, cv_record);
                    add.setImageResource(R.drawable.checked2);
                    Toast.makeText(context, "已新增記錄 ! ", Toast.LENGTH_SHORT).show();
                    if(food_in_recent.moveToFirst()){
                        db.delete("recent", "foodID = " + recipe_id, null);
                    }
                    ContentValues cv_recent = new ContentValues(1);
                    cv_recent.put("foodID", recipe_id);
                    db.insert("recent", null, cv_recent);
                }
                else {
                    db.delete("record", "date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, null);
                    add.setImageResource(R.drawable.checked);
                    Toast.makeText(context, "已移除記錄 ! ", Toast.LENGTH_SHORT).show();
                }
                float total_cal = 0;
                mData = new ArrayList<eat_listview_recipe>();
                Cursor c2 = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "'", null);
                if(c2.moveToFirst()) {
                    do {
                        eat_listview_recipe record = new eat_listview_recipe(c2.getInt(0), c2.getString(2), c2.getInt(3), c2.getDouble(4));
                        mData.add(record);
                        Cursor cal_in_food = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + c2.getInt(3), null);
                        if (cal_in_food.moveToFirst()) {
                            total_cal += cal_in_food.getFloat(2);// * cal_in_food.getFloat(4);
                        }
                        Cursor cal_in_food2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + c2.getInt(3), null);
                        if (cal_in_food2.moveToFirst()) {
                            total_cal += cal_in_food2.getFloat(2);//
                        }
                    } while (c2.moveToNext());
                    eat_new_second.cal.setText( (int) total_cal + " 大卡");
                }
                else {
                    eat_new_second.cal.setText("0 大卡");
                }

                // detect 那個頁面執行會崩掉
                notifyDataSetChanged();

                break;
            case R.id.information:
                TextView information = (TextView)v;
                recipe_id = (int) v.getTag();
                Cursor food_in_recipe = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + recipe_id, null);
                Cursor food_in_search = db.rawQuery("SELECT * FROM search WHERE foodID = " + recipe_id, null);
                final Cursor food_meal = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, null);
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(food_in_recipe.moveToFirst()) {
                    new AlertDialog.Builder(context).setTitle(food_in_recipe.getString(0) + "("+ food_in_recipe.getFloat(2) + "大卡)").setMessage("份數 : ").setIcon(R.drawable.pen).setView(input)
                            .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!input.getText().toString().equals("")) {
                                        float inputnum = Float.parseFloat(input.getText().toString());
                                        if(food_meal.moveToFirst()){
                                            if(inputnum != 0) {
                                                db.execSQL("UPDATE  record SET number = " + inputnum + " WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, new Object[]{});
                                                Toast.makeText(context, "已修改記錄 ! ", Toast.LENGTH_SHORT).show();
                                                notifyDataSetChanged();
                                                float total_cal = 0;
                                                Cursor change_cal = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "'", null);
                                                if(change_cal.moveToFirst()) {
                                                    do {
                                                        Cursor cal_in_food = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + change_cal.getInt(3), null);
                                                        if (cal_in_food.moveToFirst()) {
                                                            Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "' AND foodID = " + change_cal.getInt(3), null);
                                                            if(food_num.moveToFirst()) {
                                                                total_cal += cal_in_food.getFloat(2) * food_num.getFloat(4);
                                                            }
                                                        }
                                                        Cursor cal_in_food2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + change_cal.getInt(3), null);
                                                        if (cal_in_food2.moveToFirst()) {
                                                            Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "' AND foodID = " + change_cal.getInt(3), null);
                                                            if(food_num.moveToFirst()) {
                                                                total_cal += cal_in_food2.getFloat(2) * food_num.getFloat(4);
                                                            }
                                                        }
                                                    } while (change_cal.moveToNext());
                                                    eat_new_second.cal.setText( (int) total_cal + " 大卡");
                                                }
                                                else {
                                                    eat_new_second.cal.setText("0 大卡");
                                                }
                                            }
                                    }
                                        else {
                                            if(inputnum != 0) {
                                                ContentValues cv_record = new ContentValues(4);
                                                cv_record.put("date", eat_page_activity.selectdate);
                                                cv_record.put("category", eat_new_viewpager_recipe.eat);
                                                cv_record.put("foodID", recipe_id);
                                                cv_record.put("number", inputnum);
                                                db.insert("record", null, cv_record);
                                                Toast.makeText(context, "已新增記錄 ! ", Toast.LENGTH_SHORT).show();
                                                Cursor food_in_recent = db.rawQuery("SELECT * FROM recent WHERE foodID = " + recipe_id, null);
                                                if (food_in_recent.moveToFirst()) {
                                                    db.delete("recent", "foodID = " + recipe_id, null);
                                                }
                                                ContentValues cv_recent = new ContentValues(1);
                                                cv_recent.put("foodID", recipe_id);
                                                db.insert("recent", null, cv_recent);
                                            }
                                        }
                                    }

                                }
                            })
                            .setNegativeButton("取消", null).show();
                }
                if(food_in_search.moveToFirst()){
                     new AlertDialog.Builder(context)
                             .setTitle(food_in_search.getString(0) + "("+ food_in_search.getFloat(2) + "大卡)")
                             .setMessage("份數 : ")
                             .setIcon(R.drawable.pen)
                             .setView(input).setPositiveButton("確認", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             if(!input.getText().toString().equals("")) {
                                 float inputnum = Float.parseFloat(input.getText().toString());
                                 if(food_meal.moveToFirst()){
                                     if(inputnum != 0) {
                                         db.execSQL("UPDATE  record SET number = " + inputnum + " WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_viewpager_recipe.eat + "' AND foodID = " + recipe_id, new Object[]{});
                                         Toast.makeText(context, "已修改記錄 ! ", Toast.LENGTH_SHORT).show();
                                         notifyDataSetChanged();
                                         float total_cal = 0;
                                         Cursor change_cal = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "'", null);
                                         if(change_cal.moveToFirst()) {
                                             do {
                                                 Cursor cal_in_food = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + change_cal.getInt(3), null);
                                                 if (cal_in_food.moveToFirst()) {
                                                     Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "' AND foodID = " + change_cal.getInt(3), null);
                                                     if(food_num.moveToFirst()) {
                                                         total_cal += cal_in_food.getFloat(2) * food_num.getFloat(4);
                                                     }
                                                 }
                                                 Cursor cal_in_food2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + change_cal.getInt(3), null);
                                                 if (cal_in_food2.moveToFirst()) {
                                                     Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + eat_new_second.category + "' AND foodID = " + change_cal.getInt(3), null);
                                                     if(food_num.moveToFirst()) {
                                                         total_cal += cal_in_food2.getFloat(2) * food_num.getFloat(4);
                                                     }
                                                 }
                                             } while (change_cal.moveToNext());
                                             eat_new_second.cal.setText( (int) total_cal + " 大卡");
                                         }
                                         else {
                                             eat_new_second.cal.setText("0 大卡");
                                         }
                                     }
                                 }
                                 else {
                                     if(inputnum != 0) {
                                         ContentValues cv_record = new ContentValues(4);
                                         cv_record.put("date", eat_page_activity.selectdate);
                                         cv_record.put("category", eat_new_viewpager_recipe.eat);
                                         cv_record.put("foodID", recipe_id);
                                         cv_record.put("number", inputnum);
                                         db.insert("record", null, cv_record);
                                         Toast.makeText(context, "已新增記錄 ! ", Toast.LENGTH_SHORT).show();
                                         Cursor food_in_recent = db.rawQuery("SELECT * FROM recent WHERE foodID = " + recipe_id, null);
                                         if (food_in_recent.moveToFirst()) {
                                             db.delete("recent", "foodID = " + recipe_id, null);
                                         }
                                         ContentValues cv_recent = new ContentValues(1);
                                         cv_recent.put("foodID", recipe_id);
                                         db.insert("recent", null, cv_recent);
                                     }
                                 }
                             }
                         }
                     }).setNegativeButton("取消", null).show();

                }
                notifyDataSetChanged();
                break;
        }
        if(activity!=null) activity.initAll();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        new AlertDialog.Builder(context).setTitle("牛角麵包378大卡(1個)，份數 : ").setIcon(R.drawable.pen).setView(new EditText(context)).setPositiveButton("確認", null).setNegativeButton("取消", null).show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        new AlertDialog.Builder(context).setTitle("牛角麵包378大卡(1個)，份數 : ").setMessage("").setIcon(R.drawable.pen).setView(new EditText(context)).setPositiveButton("確認", null).setNegativeButton("取消", null).show();
    }
}
