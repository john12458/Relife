package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class eat_new_viewpager_recipe extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    Context context;
    private TextView tv_eat;
    String eat;
    ImageButton bt_new_recipe;
    ListView lv_recipe;
    // ArrayList<String> array_recipe = new ArrayList<String>();
    SQLiteDatabase db;

    private List<eat_listview_recipe> mData; // 定義數據

    public eat_new_viewpager_recipe(Context context, String eat){
        this.context = context;
        this.eat = eat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eat_new_viewpager_recipe,container,false);
        tv_eat = view.findViewById(R.id.tv_eat);
        tv_eat.setText(eat);
        bt_new_recipe = view.findViewById(R.id.bt_new_recipe);
        bt_new_recipe.setOnClickListener(this);
        lv_recipe = view.findViewById(R.id.listview_recipe);
        // 初始化數據
        initData();
        lv_recipe.setOnItemClickListener(this);
        // ArrayAdapter<String> ad_recipe = new ArrayAdapter<>(context, android.R.layout.simple_list_item_checked, array_recipe);
        return view;
    }

    // 初始化数据
    private void initData() {
        db = context.openOrCreateDatabase("relife", 0, null);
        mData = new ArrayList<eat_listview_recipe>();
        Cursor c = db.rawQuery("SELECT * FROM recipe", null);
        if(c.moveToFirst()){
            do {
                eat_listview_recipe record  = new eat_listview_recipe(c.getString(0), c.getInt(1));
                mData.add(record);
            } while (c.moveToNext());
        }
        //  將布局添加到ListView中
        LayoutInflater layoutinflater =getLayoutInflater();
        // 創建自定義Adapter的對象
        recipe_adapter adapter = new recipe_adapter(layoutinflater,mData);
        lv_recipe.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_new_recipe){
            Intent it = new Intent(context, eat_new_recipe.class);
            startActivityForResult(it, 0);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
