package com.mis.relife.pages.sport.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mis.relife.R;

import java.util.List;

public class recylerview_sportpage_adapter extends RecyclerView.Adapter<recylerview_sportpage_adapter.viewholder> {

    private Context context;
    private LayoutInflater inflater;
    public List<String> sport_time;
    public List<String> sport_cal;
    public List<String> sport_name;
    public List<String> sport_StartTime;

    private OnItemClickListener mOnItemClickListener;

    public recylerview_sportpage_adapter(Context context,List<String> sport_name,List<String> sport_StartTime,List<String> sport_time,List<String> sport_cal){
        this.context = context;
        this.sport_name = sport_name;
        this.sport_StartTime = sport_StartTime;
        this.sport_time = sport_time;
        this.sport_cal = sport_cal;
        this.inflater = LayoutInflater.from(context);
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
