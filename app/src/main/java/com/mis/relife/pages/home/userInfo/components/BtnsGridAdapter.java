package com.mis.relife.pages.home.userInfo.components;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mis.relife.R;

class BtnsGridAdapter extends BaseAdapter {
    private final Context context;
    private String[] btns;
    private LayoutInflater inflater;

    public BtnsGridAdapter(Context context,String[] btns) {
        this.btns = btns;
        this.context = context;
    }

    @Override
    public int getCount() {
        return btns.length;
    }

    @Override
    public Object getItem(int position) {
        return btns[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.user_btns_grid,null);
        TextView userBtnTextView = convertView.findViewById(R.id.UserBtnTextView);
        ImageView imageView = convertView.findViewById(R.id.UserIcon);
        imageView.setImageResource(R.drawable.anim_shy);
        userBtnTextView.setText(btns[position]);
        if(position==2)userBtnTextView.setTextColor(Color.RED);
        return convertView;
    }
}
