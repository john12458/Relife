package com.mis.relife.pages.sport.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

public class recyclerview_sport_plus_adapter extends RecyclerView.Adapter<recyclerview_sport_plus_adapter.ViewHolder> {

    Context context;
    public List<String> sport_type_child = new ArrayList<String>();
    public List<String> sport_type_other = new ArrayList<>();
//    public String[] sport_type_other = {"腳踏車 10km/時  ","腳踏車 20km/時  ","腳踏車 30km/時"};
    private OnItemClickListener mOnItemClickListener;


    public recyclerview_sport_plus_adapter(Context context){
        this.context = context;
        sport_type_other.clear();
        sport_type_other.add("滑雪");
        sport_type_other.add("攀岩");
        sport_type_other.add("溜冰刀");
        sport_type_other.add("飛盤");
        sport_type_other.add("溜直排輪");
        sport_type_other.add("跳繩");
    }

    @NonNull
    @Override
    public recyclerview_sport_plus_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sport_plus_type_recyclerview,viewGroup,false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tv_sport_type.setText(sport_type_child.get(i));
        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView , i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return sport_type_child.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //以下如題 初始化使用者點擊 運動子類別
    public void inidata_sport_child_type_run(){
        sport_type_child.clear();
        sport_type_child.add("慢走");
        sport_type_child.add("健走");
        sport_type_child.add("慢跑");
        sport_type_child.add("快跑");
    }

    public void inidata_sport_child_type_ball(){
        sport_type_child.clear();
        sport_type_child.add("排球");
        sport_type_child.add("保齡球");
        sport_type_child.add("籃球(半場)  ");
        sport_type_child.add("籃球(全場)  ");
        sport_type_child.add("足球");
    }
    public void inidata_sport_child_type_beat(){
        sport_type_child.clear();
        sport_type_child.add("桌球");
        sport_type_child.add("羽毛球");
        sport_type_child.add("網球");
    }
    public void inidata_sport_child_type_stick(){
        sport_type_child.clear();
        sport_type_child.add("棒壘球");
        sport_type_child.add("高爾夫球");
    }
    public void inidata_sport_child_type_swim(){
        sport_type_child.clear();
        sport_type_child.add("游泳(較快)");
        sport_type_child.add("游泳(慢)");
        sport_type_child.add("划獨木舟");
        sport_type_child.add("划船比賽");
    }
    public void inidata_sport_child_type_gymnastics(){
        sport_type_child.clear();
        sport_type_child.add("瑜珈");
        sport_type_child.add("跳舞(慢)");
        sport_type_child.add("跳舞(快)");
        sport_type_child.add("國標舞");
        sport_type_child.add("有氧舞蹈");
        sport_type_child.add("健康操");
        sport_type_child.add("拳擊");
    }
    public void inidata_sport_child_type_Martial(){
        sport_type_child.clear();
        sport_type_child.add("騎馬");
        sport_type_child.add("太極拳");
    }
    public void inidata_sport_child_type_other(){
        sport_type_child.clear();
        for (int i = 0;i < sport_type_other.size();i++){
            sport_type_child.add(sport_type_other.get(i));
        }
    }
    public void inidata_sport_child_type_work(){
        sport_type_child.clear();
        sport_type_child.add("拖地");
        sport_type_child.add("園藝");
        sport_type_child.add("製造");
        sport_type_child.add("農林漁牧");
        sport_type_child.add("搬重物");
    }
    public void inidata_sport_child_type_bike(){
        sport_type_child.clear();
        for (int i = 0;i < sport_type_other.size();i++){
            sport_type_child.add(sport_type_other.get(i));
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_sport_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sport_type = itemView.findViewById(R.id.tv_sport_type);
        }
    }
}
