package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class eat_new_viewpager_favorite extends Fragment {

    Context context;
    private TextView tv_eat;
    String eat;
    SQLiteDatabase db;
    ListView lv_love;
    private List<eat_listview_recipe> mData;

    public eat_new_viewpager_favorite(Context context,String eat){
        this.context = context;
        this.eat = eat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eat_new_viewpager_favorite,container,false);
        tv_eat = view.findViewById(R.id.tv_eat);
        lv_love = view.findViewById(R.id.listview_love);
        tv_eat.setText(eat);
        db = context.openOrCreateDatabase("relife",0,null);
        Cursor c = db.rawQuery("SELECT * FROM love", null);
        initData();

        return view;
    }


    public void initData() {
        db = context.openOrCreateDatabase("relife", 0, null);
        mData = new ArrayList<eat_listview_recipe>();
        Cursor c = db.rawQuery("SELECT * FROM love", null);
        if(c.moveToFirst()){
            do {
                eat_listview_recipe record  = new eat_listview_recipe(c.getInt(0),c.getInt(1));
                mData.add(record);
            } while (c.moveToNext());
        }
        //  將布局添加到ListView中
        LayoutInflater layoutinflater =getLayoutInflater();
        // 創建自定義Adapter的對象
        recipe_adapter adapter = new recipe_adapter(layoutinflater,mData,context, (eat_new_activity) getActivity());
        lv_love.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
