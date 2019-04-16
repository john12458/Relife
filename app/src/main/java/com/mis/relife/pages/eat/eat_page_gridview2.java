package com.mis.relife.pages.eat;

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
    private int[] menu_img;
    public eat_page_gridview2(MainActivity mainActivity, String[] menu, int[] menu_img) {
        myinflater = LayoutInflater.from(mainActivity);
        this.menu = menu;
        this.menu_img = menu_img;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myinflater.inflate(R.layout.eat_page_gridview_button,null);
        TextView tv_eat = convertView.findViewById(R.id.tv_eat);
        ImageView bt_eat = convertView.findViewById(R.id.bt_eat);
        tv_eat.setText(menu[position]);
        bt_eat.setImageResource(menu_img[position]);
        return convertView;
    }
}