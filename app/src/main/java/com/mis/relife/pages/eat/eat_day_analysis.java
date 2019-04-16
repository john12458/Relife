package com.mis.relife.pages.eat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

public class eat_day_analysis extends Activity {

    private List<String> data = new ArrayList<>();
    private List<Integer> percent = new ArrayList<>();
    private List<Integer> cal = new ArrayList<>();
    private eat_week_gridview week_adapter;
    private GridView gv_menu;
    private ImageButton bt_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_day_analysis);
        gv_menu = findViewById(R.id.gv_data2);
        bt_finish = findViewById(R.id.bt_left);
        bt_finish.setOnClickListener(finish);
        inidata();
        week_adapter = new eat_week_gridview(this,data,cal,percent);
        gv_menu.setAdapter(week_adapter);
    }

    private Button.OnClickListener finish = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void inidata(){
        data.add("早餐");
        data.add("午餐");
        data.add("晚餐");
        data.add("宵夜");
        data.add("點心");
        percent.add(20);
        percent.add(20);
        percent.add(20);
        percent.add(20);
        percent.add(20);
        cal.add(200);
        cal.add(200);
        cal.add(200);
        cal.add(200);
        cal.add(200);
    }
}