package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class eat_new_viewpager_favorite extends Fragment implements View.OnClickListener {

    Context context;
    private TextView tv_eat;
    String eat;
    SQLiteDatabase db;
    ListView lv_love;
    Button bt_cancel;
    EditText ed_search;
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
        bt_cancel = view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(this);
        db = context.openOrCreateDatabase("relife",0,null);
        ed_search = view.findViewById(R.id.ed_search);
        ed_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<eat_listview_recipe> search_data = new ArrayList<eat_listview_recipe>();
                String word = ed_search.getText().toString();
                Cursor search_name = db.rawQuery("SELECT * FROM recipe WHERE name LIKE '%" + word + "%'", null);
                if(search_name.moveToFirst()) {
                    do {
                        Cursor search = db.rawQuery("SELECT * FROM love WHERE foodID = " + search_name.getInt(1), null);
                        if (search.moveToFirst()) {
                            eat_listview_recipe record = new eat_listview_recipe(search.getInt(0), search.getInt(1));
                            search_data.add(record);
                        }
                    }while (search_name.moveToNext());
                }
                //search
                    Cursor search_name2 = db.rawQuery("SELECT * FROM search WHERE name LIKE '%" + word + "%'", null);
                    if(search_name2.moveToFirst()) {
                        do {
                            Log.d("Search Table ",search_name2.getString(0)+search_name2.getString(1));
                            Cursor search2 = db.rawQuery("SELECT * FROM love WHERE foodID = " + search_name2.getInt(1), null);
                            if (search2.moveToFirst()) {
                                eat_listview_recipe search = new eat_listview_recipe(search2.getInt(0), search2.getInt(1));
                                search_data.add(search);
                            }
                        }while (search_name2.moveToNext());
                    }
                //--
                LayoutInflater layoutinflater = getLayoutInflater();
                recipe_adapter adapter = new recipe_adapter(layoutinflater, search_data, context,(eat_new_activity) getActivity());
                lv_love.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.bt_cancel){
            ed_search.setText("");
        }
    }
}
