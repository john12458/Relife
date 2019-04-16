package com.mis.relife.pages.eat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;

public class eat_page_gridview extends BaseAdapter {

    private LayoutInflater myinflater;
    private String[] top;
    private String[] data;

    public eat_page_gridview(MainActivity mainActivity, String[] data, String[] top) {
        myinflater = LayoutInflater.from(mainActivity);
        this.data = data;
        this.top = top;
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
