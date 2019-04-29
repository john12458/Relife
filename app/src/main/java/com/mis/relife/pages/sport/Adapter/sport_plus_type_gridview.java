package com.mis.relife.pages.sport.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.pages.sport.New_Delete.Sport_Plus_Activity;

public class sport_plus_type_gridview extends BaseAdapter {

    private LayoutInflater myinflater;
    private  String[] sport_type = {"跑步","拍類","棒類","球類","武術","水上","健體","工作","騎車","其他"};
    private String[] sport_type_text_color = {"#ff7f50","#00ff7f","#87ceeb","#ba55d3","#ffa500","#0000cd","#3cb371","#ffd700","#a9a9a9","#a9a9a9"};
    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public sport_plus_type_gridview(Sport_Plus_Activity mainActivity){
        myinflater = LayoutInflater.from(mainActivity);
    }

    @Override
    public int getCount() {
        return sport_type.length;
    }

    @Override
    public Object getItem(int position) {
        return sport_type[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myinflater.inflate(R.layout.sport_plus_type_gridview,null);
        if (clickTemp == position) {
            convertView.setBackgroundResource(R.drawable.sleep_plus_button_gray);
        } else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        final TextView tv_sport_type = convertView.findViewById(R.id.tv_sport_type);
        tv_sport_type.setText(sport_type[position]);
        tv_sport_type.setTextColor(Color.parseColor(sport_type_text_color[position]));
        return convertView;
    }
}
