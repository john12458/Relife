package com.mis.relife.pages.eat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mis.relife.pages.MainActivity;
import com.mis.relife.R;
import com.squareup.picasso.Picasso;

public class eat_page_gridview_button extends BaseAdapter {

    private LayoutInflater myinflater;
    public String[] menu = {"早餐","午餐","晚餐","宵夜","點心","開水"};
    public int[] menu_img = {R.drawable.breakfast,R.drawable.lunch,R.drawable.dinner,R.drawable.corn
            ,R.drawable.pudding,R.drawable.water};
    private Context context;
    public eat_page_gridview_button(Context context) {
        this.context = context;
        myinflater = LayoutInflater.from(context);
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
        Picasso
                .with(context)
                .load(menu_img[position])
                .into(bt_eat);
        return convertView;
    }
}