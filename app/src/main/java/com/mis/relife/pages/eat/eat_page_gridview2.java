package com.mis.relife.pages.eat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;

public class eat_page_gridview2 extends BaseAdapter {

    private LayoutInflater myinflater;
    private String[] menu;
    private Context context;
    private int[] menu_img;
    SQLiteDatabase db;

    public eat_page_gridview2(MainActivity mainActivity, String[] menu, int[] menu_img, Context context) {
        myinflater = LayoutInflater.from(mainActivity);
        this.menu = menu;
        this.menu_img = menu_img;
        this.context = context;

        db = context.openOrCreateDatabase("relife",0,null);
    }

    @Override
    public int getCount() {
        return menu.length;
    }

    @Override
    public Object getItem(int position) {
        return menu[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    float cal;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        cal = 0;
        convertView = myinflater.inflate(R.layout.eat_page_gridview_button,null);
        TextView tv_eat = convertView.findViewById(R.id.tv_eat);
        ImageView bt_eat = convertView.findViewById(R.id.bt_eat);
        TextView tv_cal = convertView.findViewById(R.id.tv_cal);
        tv_eat.setText(menu[position]);
        bt_eat.setImageResource(menu_img[position]);
        if(position != 5) {
            Cursor c = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + menu[position] + "'", null);
            if (c.moveToFirst()) {
                do {
                    Cursor cal_in_food = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + c.getInt(3), null);
                    if (cal_in_food.moveToFirst()) {
                        Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + menu[position] + "' AND foodID = " + c.getInt(3), null);
                        if(food_num.moveToFirst()) {
                            cal += cal_in_food.getFloat(2) * food_num.getFloat(4);
                        }
                    }
                    // search
                    Cursor cal_in_food2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + c.getInt(3), null);
                    if (cal_in_food2.moveToFirst()) {
                        Cursor food_num = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "' AND category = '" + menu[position] + "' AND foodID = " + c.getInt(3), null);
                        if(food_num.moveToFirst()) {
                            cal += cal_in_food2.getFloat(2) * food_num.getFloat(4);
                        }
                    }
                } while (c.moveToNext());
            }
            tv_cal.setText((int) cal + "大卡");
        }
        else {
            Cursor c = db.rawQuery("SELECT * FROM water WHERE date = '" + eat_page_activity.selectdate + "'", null);
            if(c.moveToFirst()) {
                tv_cal.setText(c.getInt(1) + "cc");
            }
            else {
                tv_cal.setText("0 cc");
            }
        }

        return convertView;
    }

}