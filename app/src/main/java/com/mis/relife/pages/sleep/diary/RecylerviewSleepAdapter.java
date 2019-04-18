package com.mis.relife.pages.sleep.diary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.SleepFragment;
import com.mis.relife.pages.sleep.day.DayFragment;

import java.util.ArrayList;
import java.util.List;

public class RecylerviewSleepAdapter extends RecyclerView.Adapter<RecylerviewSleepAdapter.viewholder> {

    private LayoutInflater inflater;
    private static Context context;
    public static List<String> day = new ArrayList<>();
    public static List<String> content = new ArrayList<>();
    public static List<String> go_bed_time = new ArrayList<>();
    public static List<String> get_up_time = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    private static DayFragment sleep_day = null;

    public RecylerviewSleepAdapter(Context context, List<String> day, List<String> go_bed_time, List<String> get_up_time, List<String> content){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.day = day;
        this.go_bed_time = go_bed_time;
        this.get_up_time = get_up_time;
        this.content = content;
    }

    @Override
    public RecylerviewSleepAdapter.viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view  =LayoutInflater.from(context).inflate(R.layout.sleep_page,viewGroup,false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,250);
        view.setLayoutParams(params);
        viewholder holder = new viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecylerviewSleepAdapter.viewholder viewHolder, final int i) {
        viewHolder.tv_day.setText(String.valueOf(day.get(i)));
        viewHolder.tv_go_bed_time.setText(go_bed_time.get(i));
        viewHolder.tv_get_up_time.setText(get_up_time.get(i));
        viewHolder.tv_record.setText(content.get(i));
        viewHolder.sleep_card.setRadius(10);
        viewHolder.sleep_card.setCardBackgroundColor(Color.WHITE);
        viewHolder.sleep_card.setCardElevation(10);
        viewHolder.sleep_card.setContentPadding(0,0,0,0);

        // item click
        if (mOnItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView , i);
                }
            });
        }
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemClickListener.onItemLongClick(viewHolder.itemView, i);
                return true;
            }
        });

        
    }

    @Override
    public int getItemCount() {
        return day.size();
    }

    //點擊功能
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    //點擊接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    //初始化日記
    public static void initial_sleep_diary(String test) {
        day.clear();
        go_bed_time.clear();
        get_up_time.clear();
        content.clear();
        day.add(test);
        day.add("8");
        go_bed_time.add("23:00");
        go_bed_time.add("23:00");
        get_up_time.add("08:00");
        get_up_time.add("07:00");
        content.add("今天很猛");
        content.add("今天很帥");

        DiaryFragment.recylerview_sleep_adapter = new RecylerviewSleepAdapter(DiaryFragment.context,day,go_bed_time,get_up_time,content);
    }

    //手動增加日記
    public static void insert_sleep_diary(String bed_data,String get_data,String back_day,String dream_content) {
        get_up_time.add(get_data);
        go_bed_time.add(bed_data);
        day.add(back_day);
        content.add(dream_content);
        DiaryFragment.recylerview_sleep_adapter.notifyDataSetChanged();
        SleepFragment.sleep_adapter.notifyDataSetChanged();
    }

    //刪除點選日記
    public static void delete_sleep_diary(int position) {
        get_up_time.remove(position);
        go_bed_time.remove(position);
        day.remove(position);
        content.remove(position);
        DiaryFragment.recylerview_sleep_adapter.notifyDataSetChanged();
        SleepFragment.sleep_adapter.notifyDataSetChanged();
    }

    //修改點選日記
    public static void edit_sleep_diary(String bed_data,String get_data,String back_day,String dream_content,int position) {
        get_up_time.set(position,get_data);
        go_bed_time.set(position,bed_data);
        day.set(position,back_day);
        content.set(position,dream_content);
        DiaryFragment.recylerview_sleep_adapter.notifyDataSetChanged();
        SleepFragment.sleep_adapter.notifyDataSetChanged();
    }

    class viewholder extends RecyclerView.ViewHolder{

        public TextView tv_day,tv_go_bed_time,tv_get_up_time,tv_record;
        public CardView sleep_card;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_go_bed_time = itemView.findViewById(R.id.tv_go_bed_time);
            tv_get_up_time = itemView.findViewById(R.id.tv_get_up_time);
            tv_record = itemView.findViewById(R.id.tv_record);
            sleep_card = itemView.findViewById(R.id.sleep_card);
        }
    }
}
