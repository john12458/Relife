package com.mis.relife.pages.sport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.mis.relife.R;
import com.mis.relife.useful.recyler_item_space;

public class Sport_Plus_Activity extends AppCompatActivity {

    private RecyclerView recyclerView_sport_type;
    private GridView gv_sport_type;
    private RecyclerView recyclerView_sport_record;

    private sport_plus_type_gridview gv_sport_type_adapter;
    private recyclerview_sport_plus_adapter recyclerview_sport_type_child_adapter;
    public static sport_recycler_record_adapter sport_recycler_record_adapter;

    private  String[] sport_type = {"跑步","拍類","棒類","球類","武術","水上","健體","工作","騎車","其他"};
//    private List<String> sport_type_child = new ArrayList<String>();

//    private List<String> sport_record_name = new ArrayList<String>();
//    private List<String> sport_record_info = new ArrayList<String>();

    static TextView tv_sport_child_name;
    private Button bt_finish;

    static int choose_type = 0;
    int position;
    private String name = "",start = "",time = "",speed = "",distance = "",cal = "";
    Bundle bundle = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport__plus_);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerView_sport_type = findViewById(R.id.recycler_sport_type);
        gv_sport_type = findViewById(R.id.gv_data2);
        recyclerView_sport_record = findViewById(R.id.recycler_record);
        tv_sport_child_name = findViewById(R.id.tv_sport_child_type);
        bt_finish = findViewById(R.id.bt_finish);

        //初始化下面提供使用者輸入資訊的地方
        sport_recycler_record_adapter.initView_record_other();
        sport_recycler_record_adapter = new sport_recycler_record_adapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView_sport_record.setLayoutManager(linearLayoutManager);
        recyclerView_sport_record.addItemDecoration(new recyler_item_space(0,25));
        recyclerView_sport_record.setAdapter(sport_recycler_record_adapter);
        sport_recycler_record_adapter.setOnItemClickListener(record_click);


        //初始化供使用者點擊的運動種類子類別
//        inidata_sport_child_type_run();
        recyclerview_sport_type_child_adapter = new recyclerview_sport_plus_adapter(Sport_Plus_Activity.this);
        LinearLayoutManager ms= new LinearLayoutManager(this);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_sport_type.setLayoutManager(ms);
        recyclerView_sport_type.setAdapter(recyclerview_sport_type_child_adapter);

        //初始化供使用者點擊的運動種類父類別
        gv_sport_type_adapter = new sport_plus_type_gridview(Sport_Plus_Activity.this);
        gv_sport_type.setAdapter(gv_sport_type_adapter);
        gv_sport_type.setOnItemClickListener(gv_sport_type_selector);

        if(getIntent().getExtras() != null){
            have_deliever_way();
            bt_finish.setOnClickListener(edit_back);
        }
        else{
            recyclerview_sport_type_child_adapter.sport_type_child.clear();
            recyclerview_sport_type_child_adapter.notifyDataSetChanged();
            bt_finish.setOnClickListener(finish_back);
        }
    }

    //方法區

    //編輯的 傳值方式
    private Button.OnClickListener edit_back = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {

            //以下為重複動作 名稱 開始 和 時間長度
            name = tv_sport_child_name.getText().toString();
            start = sport_recycler_record_adapter.sport_record_info.get(0);
            time = sport_recycler_record_adapter.sport_record_info.get(1);

            if(tv_sport_child_name.getText().toString().equals("運動項目")){
                Toast.makeText(Sport_Plus_Activity.this,"請選擇運動項目",Toast.LENGTH_LONG).show();
            }
            else if(time.equals("")){
                Toast.makeText(Sport_Plus_Activity.this,"請選擇運動長度",Toast.LENGTH_LONG).show();
            }
            else {
                    cal = sport_recycler_record_adapter.sport_record_info.get(2);

                recylerview_sportpage_adapter.edit_sport_diary(name, time, cal, start, position);
                finish();
            }
        }
    };

    //單純新增運動日記的傳值
    private Button.OnClickListener finish_back = new Button.OnClickListener(){

        //先用好重複的部分
        String insert_name = "";
        String insert_start = "";
        String insert_time = "";
        String insert_cal = "";
        //重複的值有 名稱 開始時間 時間長度
        private void finish_initial(){
            insert_name = tv_sport_child_name.getText().toString();
            insert_start = sport_recycler_record_adapter.sport_record_info.get(0);
            insert_time = sport_recycler_record_adapter.sport_record_info.get(1);
        }
        @Override
        public void onClick(View v) {

            finish_initial();

            if(tv_sport_child_name.getText().toString().equals("運動項目")){
                Toast.makeText(Sport_Plus_Activity.this,"請選擇運動項目",Toast.LENGTH_LONG).show();
            }
            else if(insert_time.equals("")){
                Toast.makeText(Sport_Plus_Activity.this,"請選擇運動長度",Toast.LENGTH_LONG).show();
            }
            else {
                insert_cal = sport_recycler_record_adapter.sport_record_info.get(2);
                recylerview_sportpage_adapter.insetdata_sport_diary(insert_name, insert_time, insert_cal, insert_start);

                finish();
            }
        }
    };

    //如果使用者要編輯日記 就會判斷有值傳過來 要做的事情 分三部分 跑步類 腳踏車類 或是其他類
    private void have_deliever_way(){
        recyclerview_sport_type_child_adapter.sport_type_child.clear();
        recyclerview_sport_type_child_adapter.notifyDataSetChanged();
        bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        sport_recycler_record_adapter.sport_record_info.clear();
        tv_sport_child_name.setText(bundle.getString("sport_name"));
        name = bundle.getString("sport_name");
        start = bundle.getString("sport_start");
        time = bundle.getString("sport_time");
        cal = bundle.getString("sport_cal");

        sport_recycler_record_adapter.initView_record_other();
        sport_recycler_record_adapter.sport_record_info.set(0,start);
        sport_recycler_record_adapter.sport_record_info.set(1,time);
        sport_recycler_record_adapter.sport_record_info.set(2,cal);
        sport_recycler_record_adapter.notifyDataSetChanged();

    }

    //連結運動類別父子類別
    private AdapterView.OnItemClickListener gv_sport_type_selector = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            gv_sport_type_adapter.setSeclection(position);
            gv_sport_type_adapter.notifyDataSetChanged();

            if(position == 0){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_run();
                connect_easy_other();
            }
            else if(position == 1){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_beat();
                connect_easy_other();
            }
            else if(position == 2){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_stick();
                connect_easy_other();
            }
            else if(position == 3){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_ball();
                connect_easy_other();
            }
            else if(position == 4){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_Martial();
                connect_easy_other();
            }
            else if(position == 5){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_swim();
                connect_easy_other();
            }
            else if(position == 6){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_gymnastics();
                connect_easy_other();
            }
            else if(position == 7){
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_work();
                connect_easy_other();
            }
            else if(position == 8){
                recyclerview_sport_plus_adapter.inidata_sport_child_type_bike();
                tv_sport_child_name.setText("運動項目");
                connect_easy_other();
            }
            else {
                tv_sport_child_name.setText("運動項目");
                recyclerview_sport_plus_adapter.inidata_sport_child_type_other();
                connect_easy_other();
            }
        }
    };

    //讓上面的連結方便一點 父類別按了之後會連帶帶動下面的子類別和record資訊
    private void connect_easy_other(){
        sport_recycler_record_adapter.initView_record_other();
        sport_recycler_record_adapter.notifyDataSetChanged();
        recyclerview_sport_type_child_adapter.notifyDataSetChanged();
    }

    private sport_recycler_record_adapter.OnItemClickListener record_click = new sport_recycler_record_adapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

                if(position == 0) {
                    sport_recycler_record_adapter.initStartTimePicker(position);
                }
                else if(position == 1){
                    sport_recycler_record_adapter.initTimePicker(position);
                }

        }
    };
}
