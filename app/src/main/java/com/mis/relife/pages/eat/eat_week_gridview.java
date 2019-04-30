package com.mis.relife.pages.eat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.List;

public class eat_week_gridview extends BaseAdapter {

    private LayoutInflater myinflater;
    private List<String> data;
    private List<Integer> percent;
    private List<Integer> cal;
    private TextView tv_menu,tv_percnt,tv_cal;

    public eat_week_gridview(Context context, List<String> data, List<Integer> cal, List<Integer> percent){
        myinflater = LayoutInflater.from(context);
        this.data = data;
        this.cal = cal;
        this.percent = percent;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myinflater.inflate(R.layout.eat_week_gridview,null);
        tv_menu = convertView.findViewById(R.id.tv_menu);
        tv_percnt = convertView.findViewById(R.id.tv_percent_number);
        tv_cal = convertView.findViewById(R.id.tv_cal);
        tv_menu.setText(data.get(position));
        tv_percnt.setText(String.valueOf(percent.get(position)));
        tv_cal.setText(String.valueOf(cal.get(position)));
        return convertView;
    }
}
