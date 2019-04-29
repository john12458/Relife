package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.data.model.Food;
import com.mis.relife.pages.eat.Adapter.recipe_adapter;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class eat_new_viewpager_recipe extends Fragment {

    private Context context;
    private TextView tv_eat;
    private String eat;
    private ImageButton bt_new_recipe;
    private ListView lv_recipe;

    private List<Food> recipeData = new ArrayList<>(); // 定義數據
    private recipe_adapter recipeAdapter;
    private EatData eatData;

    public eat_new_viewpager_recipe(Context context, String eat,EatData eatData){
        this.context = context;
        this.eat = eat;
        this.eatData = eatData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.eat_new_viewpager_recipe,container,false);
        tv_eat = view.findViewById(R.id.tv_eat);
        tv_eat.setText(eat);
        bt_new_recipe = view.findViewById(R.id.bt_new_recipe);
        bt_new_recipe.setOnClickListener(recipe_plus);
        lv_recipe = view.findViewById(R.id.listview_recipe);
        // 初始化數據
        initData();
        //設置刪除和編輯
        lv_recipe.setOnItemClickListener(recipe_edit);
        lv_recipe.setOnItemLongClickListener(recipe_delete);
        return view;
    }

//     初始化数据
    private void initData() {
//        recipeData.clear();
//        recipeData = eatData.recipe_foods;
//        System.out.println(eatData.recipe_foods.get(0).food + "!!!!!\n" +eatData.recipe_foods.get(0).cal + "!!!!!\n" +
//                eatData.recipe_foods.get(0).number);
        recipeAdapter = new recipe_adapter(context,eatData.recipe_foods);
        lv_recipe.setAdapter(recipeAdapter);

    }

    private Button.OnClickListener recipe_plus = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent it = new Intent(context, eat_new_recipe.class);
            startActivityForResult(it, 0);
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private AdapterView.OnItemLongClickListener recipe_delete = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    };

    private AdapterView.OnItemClickListener recipe_edit = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            String key = eatData.get_key(2,recipeAdapter.mData.get(position));
//            Intent it = new Intent(context, eat_new_recipe.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("food",recipeAdapter.mData.);
//            bundle.putString("key",key);
//            startActivity(it);
        }

    };

}
