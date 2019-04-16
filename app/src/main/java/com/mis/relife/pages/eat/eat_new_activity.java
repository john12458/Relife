package com.mis.relife.pages.eat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

public class eat_new_activity extends AppCompatActivity {
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> tabs = new ArrayList<>();
    private TabLayout tb_content;
    private ViewPager vp_content;
    private eat_new_viewpager_adapter vp_adapter = null;
    Button bt_finish;
    private String eat_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eat_new_main);
        Bundle bundle = getIntent().getExtras();
        eat_name = bundle.getString("eat_name");
        initData_tablayout();
        vp_adapter = new eat_new_viewpager_adapter(getSupportFragmentManager(),fragments,tabs);
        tb_content = findViewById(R.id.tb_content);
        vp_content = findViewById(R.id.vp_content);
        //设置TabLayout的模式
        tb_content.setTabMode(TabLayout.MODE_FIXED);
        vp_content.setAdapter(vp_adapter);
        //关联ViewPager和TabLayout
        tb_content.setupWithViewPager(vp_content);
        bt_finish = findViewById(R.id.bt_left);
        bt_finish.setOnClickListener(finish);
    }

    private void initData_tablayout() {
        tabs.add("搜尋");
        tabs.add("我的食譜");
        tabs.add("我的最愛");
        tabs.add("最近新增");
        fragments.add(new eat_new_viewpager_new(this,eat_name));
        fragments.add(new eat_new_viewpager_recipe(this,eat_name));
        fragments.add(new eat_new_viewpager_favorite(this,eat_name));
        fragments.add(new eat_new_viewpager_recent(this,eat_name));
    }

    private Button.OnClickListener finish = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
