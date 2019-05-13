package com.mis.relife.pages.eat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;

public class eat_page_gridview extends BaseAdapter {

    private LayoutInflater myinflater;
    private String[] top;
    private String[] data;
    private Context context;
    SQLiteDatabase db;
    private eat_page_activity eatPage;

    public eat_page_gridview(MainActivity mainActivity, String[] data, String[] top, Context context,eat_page_activity eatPage) {
        myinflater = LayoutInflater.from(mainActivity);
        this.data = data;
        this.top = top;
        this.context = context;
        this.eatPage = eatPage;
        db = context.openOrCreateDatabase("relife",0,null);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    float cal;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myinflater.inflate(R.layout.eat_page_gridview,null);
        TextView tv_top = convertView.findViewById(R.id.tv_top);
        TextView tv_data = convertView.findViewById(R.id.tv_data);
        switch(position) {
            case 0:
                tv_data.setText("1500 cal");
                float normal_cal;
                if(eat_page_activity.gender.equals("男")){
                    //normal_cal = 66 + (9.6 * eat_page_activity.weight)
                }
                else if(eat_page_activity.gender.equals("女")){

                }
                break;
            case 1:
                cal = 0;
                Cursor c = db.rawQuery("SELECT * FROM record WHERE date = '" + eat_page_activity.selectdate + "'", null);
                if (c.moveToFirst()) {
                    do {
                        Cursor cal_in_food = db.rawQuery("SELECT * FROM recipe WHERE foodID = " + c.getInt(3), null);
                        if (cal_in_food.moveToFirst()) {
                                cal += cal_in_food.getFloat(2) * c.getFloat(4);
                        }
                        // search
                        Cursor cal_in_food2 = db.rawQuery("SELECT * FROM search WHERE foodID = " + c.getInt(3), null);
                        if (cal_in_food2.moveToFirst()) {
                                cal += cal_in_food2.getFloat(2) * c.getFloat(4);
                        }
                    } while (c.moveToNext());
                    tv_data.setText(String.valueOf((int) cal) + " cal");
                }
                else {tv_data.setText("0 cal");}
            break;
            case 2:
                tv_data.setText(String.valueOf(eatPage.lossTotalCal) + " cal");
        }
        tv_top.setText(top[position]);
        return convertView;
    }
}
