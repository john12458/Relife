package com.mis.relife.pages.sport;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.ArrayList;
import java.util.List;

public class recylerview_sportpage_adapter extends RecyclerView.Adapter<recylerview_sportpage_adapter.viewholder> {

    private Context context;
    private LayoutInflater inflater;
    public static List<String> sport_time = new ArrayList<>();
    public static List<String> sport_cal = new ArrayList<>();
    public static List<String> sport_name = new ArrayList<>();
    public static List<String> sport_StartTime = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public recylerview_sportpage_adapter(Context context, List<String> sport_name, List<String> sport_time, List<String> sport_cal){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.sport_time = sport_time;
        this.sport_cal = sport_cal;
        this.sport_name = sport_name;
    }

    @Override
    public recylerview_sportpage_adapter.viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(context).inflate(R.layout.sport_recylerview,viewGroup,false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,150);
        view.setLayoutParams(params);
        viewholder holder = new viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final recylerview_sportpage_adapter.viewholder viewholder, final int i) {
        viewholder.tv_sport_name.setText(sport_name.get(i));
        viewholder.tv_sport_distance.setText(String.valueOf(sport_time.get(i)));
        viewholder.tv_sport_cal.setText(String.valueOf(sport_cal.get(i)));

        if (mOnItemClickListener != null) {
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewholder.itemView , i);
                }
            });
        }
        viewholder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mOnItemClickListener.onItemLongClick(viewholder.itemView, i);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return sport_name.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public static void inidata_sport_diary(String test){
        sport_name.clear();
        sport_time.clear();
        sport_cal.clear();
        sport_StartTime.clear();
        sport_name.add("慢跑");
        sport_name.add("快跑");
        sport_time.add("32分");
        sport_time.add("30分");
        sport_cal.add(test);
        sport_cal.add("100");
        sport_StartTime.add("13:30");
        sport_StartTime.add("13:30");
        sport_page_activity.sportpage_adapter = new recylerview_sportpage_adapter(sport_page_activity.context,sport_name,sport_time,sport_cal);
    }

    public static void insetdata_sport_diary(String name, String time,String cal,String starttime){
        sport_name.add(name);
        sport_time.add(time);
        sport_cal.add(cal);
        sport_StartTime.add(starttime);
        sport_page_activity.sportpage_adapter.notifyDataSetChanged();
        sport_tab_viewpager.sport_adapter.notifyDataSetChanged();
        sport_page_activity.totalcal();
    }

    public static void delete_sport_diary(int position) {
        sport_name.remove(position);
        sport_time.remove(position);
        sport_cal.remove(position);
        sport_StartTime.remove(position);
        sport_page_activity.sportpage_adapter.notifyDataSetChanged();
        sport_tab_viewpager.sport_adapter.notifyDataSetChanged();
        sport_page_activity.totalcal();
    }

    public static void edit_sport_diary(String name, String time,String cal,String starttime,int position){
        sport_name.set(position,name);
        sport_time.set(position,time);
        sport_cal.set(position,cal);
        sport_StartTime.set(position,starttime);
        System.out.println(position + ": " +name);
        sport_page_activity.sportpage_adapter.notifyDataSetChanged();
        sport_tab_viewpager.sport_adapter.notifyDataSetChanged();
        sport_page_activity.totalcal();
    }

    class viewholder extends RecyclerView.ViewHolder{

        public TextView tv_sport_name,tv_sport_distance,tv_sport_cal;
        public CardView sport_card;

        public viewholder(View itemView) {
            super(itemView);
            tv_sport_name = itemView.findViewById(R.id.tv_sport_name);
            tv_sport_distance = itemView.findViewById(R.id.tv_sport_distance);
            tv_sport_cal = itemView.findViewById(R.id.tv_sport_cal);
            sport_card = itemView.findViewById(R.id.sport_card);
        }
    }
}
