package com.mis.relife.pages.sleep.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.New_Delete.sleep_plus;
import com.squareup.picasso.Picasso;

public class GridviewMoodChooseAdapter extends BaseAdapter {

    private LayoutInflater myinflater;
    public int[] moodImg = {R.drawable.first4,R.drawable.eat1,R.drawable.shine3,R.drawable.shy6,R.drawable.tired3,R.drawable.walk6};
    private Context context;

    public GridviewMoodChooseAdapter(Context context){
        this.context = context;
        myinflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return moodImg.length;
    }

    @Override
    public Object getItem(int position) {
        return moodImg[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myinflater.inflate(R.layout.sleep_mood_choose_gridview,null);
        ImageView ivMood = convertView.findViewById(R.id.iv_mood);
        Picasso
                .with(context)
                .load(moodImg[position])
                .into(ivMood);
        return convertView;
    }
}
