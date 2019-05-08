package com.mis.relife.pages.sleep.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.utils.Utils;
import com.mis.relife.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class recylerview_sleep_adapter extends RecyclerView.Adapter<recylerview_sleep_adapter.viewholder> {

    private LayoutInflater inflater;
    private Context context;
    public  List<String> description = new ArrayList<>();
    public  List<String> mood = new ArrayList<>();
    public  List<String> sleep_time = new ArrayList<>();
    public  List<String> wake_time = new ArrayList<>();
    public  List<String> recordDate = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    private Calendar cal;
    SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy/MM/dd");

    public recylerview_sleep_adapter(Context context, List<String> recordDate, List<String> sleep_time, List<String> wake_time, List<String> description,List<String> mood){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.recordDate = recordDate;
        this.sleep_time = sleep_time;
        this.wake_time = wake_time;
        this.description = description;
        this.mood = mood;
    }

    @Override
    public recylerview_sleep_adapter.viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view  =LayoutInflater.from(context).inflate(R.layout.sleep_page,viewGroup,false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,  RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        viewholder holder = new viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final recylerview_sleep_adapter.viewholder viewHolder, final int i) {
        String sleepTime = "";
        String wakeTime = "";
        sleepTime = getTime(sleepTime,sleep_time.get(i));
        wakeTime = getTime(wakeTime,wake_time.get(i));
        viewHolder.tv_day.setText(getDay(recordDate.get(i)));
        viewHolder.tv_go_bed_time.setText(sleepTime);
        viewHolder.tv_get_up_time.setText(wakeTime);
        viewHolder.tv_record.setText(description.get(i));
        viewHolder.sleep_card.setRadius(10);
        viewHolder.sleep_card.setCardBackgroundColor(Color.WHITE);
        viewHolder.sleep_card.setCardElevation(10);
        viewHolder.sleep_card.setContentPadding(0,0,0,0);

        if(mood.get(i).charAt(0) == '/'){
            Bitmap bitmap= BitmapFactory.decodeFile(mood.get(i));
            viewHolder.iv_rabit.setImageBitmap(bitmap);
        }
        else {
            int resId = context.getResources()
                    .getIdentifier(mood.get(i)
                            , "drawable"
                            , "com.mis.relife");
            viewHolder.iv_rabit.setImageResource(resId);
        }

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
        return recordDate.size();
    }

    //拿到轉換過後的day
    private String getDay(String date){
        int day = 0;
        try {
            Date recorDate = sdf_date.parse(date);
            cal = Calendar.getInstance();
            cal.setTime(recorDate);
            day = cal.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  String.valueOf(day);
    }

    //拿到切割過後的時間
    private String getTime(String Time,String date){
        Time = date.substring(11,16);
        return  Time;
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

    class viewholder extends RecyclerView.ViewHolder{

        public TextView tv_day,tv_go_bed_time,tv_get_up_time,tv_record;
        public CardView sleep_card;
        public ImageView iv_rabit;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_go_bed_time = itemView.findViewById(R.id.tv_go_bed_time);
            tv_get_up_time = itemView.findViewById(R.id.tv_get_up_time);
            tv_record = itemView.findViewById(R.id.tv_record);
            sleep_card = itemView.findViewById(R.id.sleep_card);
            iv_rabit = itemView.findViewById(R.id.iv_img);
        }
    }
}
