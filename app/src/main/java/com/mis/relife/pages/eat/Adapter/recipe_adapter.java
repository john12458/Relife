package com.mis.relife.pages.eat.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.data.model.Food;

import java.util.List;

public class recipe_adapter extends BaseAdapter {
    public List<Food> mData; // 定義數據
    private LayoutInflater mInflater; // 定義Inflater, 加载自定義的布局

    // 定義構造器, 在Activity創建對象Adapter的時候將數據data和Inflater傳入自定義的Adapter中進行處理

    public recipe_adapter(Context context,List<Food> data){
        mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public int getCount() {
            return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
            return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        // 獲得ListView中的view
        View viewRecipe = mInflater.inflate(R.layout.eat_listview_recipe,null);
        // 獲得自定義布局中每一個控件的對象
        TextView name = viewRecipe.findViewById(R.id.information);
        // 將數據一一添加到自定義的布局中
        name.setText(mData.get(position).food + "(" + mData.get(position).cal  + "大卡)");
        return viewRecipe ;
    }
}