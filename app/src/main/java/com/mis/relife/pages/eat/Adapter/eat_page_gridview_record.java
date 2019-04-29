package com.mis.relife.pages.eat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;

public class eat_page_gridview_record extends BaseAdapter {

    private LayoutInflater myinflater;
    private String[] top = {"今日目標","飲食攝取","運動消耗"};
    private String[] data = {"0","0","0"};

    public eat_page_gridview_record(Context context) {
        myinflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myinflater.inflate(R.layout.eat_page_gridview,null);
        TextView tv_top = convertView.findViewById(R.id.tv_top);
        TextView tv_data = convertView.findViewById(R.id.tv_data);
        tv_top.setText(top[position]);
        tv_data.setText(data[position]);
        return convertView;
    }

}
