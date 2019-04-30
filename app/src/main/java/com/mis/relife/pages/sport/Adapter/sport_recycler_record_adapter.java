package com.mis.relife.pages.sport.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.TimePickerView;
import com.mis.relife.R;
import com.mis.relife.pages.sport.count_cal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class sport_recycler_record_adapter extends RecyclerView.Adapter<sport_recycler_record_adapter.Viewholder> {

    Context context;
    public List<String> sport_record_name = new ArrayList<>();
    public List<String> sport_record_info = new ArrayList<>();
    private com.mis.relife.pages.sport.count_cal count_cal = null;

    private OnItemClickListener mOnItemClickListener;

    public  sport_recycler_record_adapter(Context context){
        this.context = context;
        initView_record_other();
        count_cal = new count_cal();
    }

    @NonNull
    @Override
    public sport_recycler_record_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sport_plus_record_recyclerview,viewGroup,false);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        Viewholder viewHolder = new Viewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder viewholder, final int i) {
        ViewGroup.LayoutParams layoutParams = viewholder.itemView.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        viewholder.tv_name.setText(sport_record_name.get(i));
        if(i == 1) {
            viewholder.tv_record.setText(sport_record_info.get(i) + "分");
        }
        else {
            viewholder.tv_record.setText(sport_record_info.get(i));
        }

        if (mOnItemClickListener != null) {
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(viewholder.itemView , i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return sport_record_name.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    //初始化其他紀錄
    public void initView_record_other() {
        sport_record_name.clear();
        sport_record_info.clear();
        sport_record_name.add("開始時間: ");
        sport_record_name.add("運動長度: ");
        sport_record_name.add("卡路里: ");
        sport_record_info.add("");
        sport_record_info.add("");
        sport_record_info.add("");
//        Sport_Plus_Activity.choose_type = 2;
    }



    //新增timepicker方法
    public void initStartTimePicker(final int position) {

        TimePickerView mTimePickerView = new TimePickerView(context, TimePickerView.Type.HOURS_MINS);
        // 设置是否循环
        mTimePickerView.setCyclic(true);
        // 设置滚轮文字大小
        mTimePickerView.setTextSize(TimePickerView.TextSize.SMALL);
        // 设置时间可选范围(结合 setTime 方法使用,必须在)
        //Calendar calendar = Calendar.getInstance();
        // mTimePickerView.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));
        // 设置选中时间
        // mTimePickerView.setTime(new Date());
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                sport_record_info.set(position,format.format(date));
                notifyDataSetChanged();
            }
        });
        mTimePickerView.show();
    }

    //初始化運動時間選擇器
    public void initTimePicker(final int position) {

        TimePickerView mTimePickerView = new TimePickerView(context, TimePickerView.Type.HOURS_MINS);
        // 设置是否循环
        mTimePickerView.setCyclic(true);
        // 设置滚轮文字大小
        mTimePickerView.setTextSize(TimePickerView.TextSize.SMALL);
        // 设置时间可选范围(结合 setTime 方法使用,必须在)
//         mTimePickerView.setRange(30, 60);
        // 设置选中时间
//         mTimePickerView.setTime(new Date());
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                String time = format.format(date);
                int hour = Integer.valueOf(time.substring(0,2));
                int min = Integer.valueOf(time.substring(3,5));
                int total_time = hour * 60 + min;
//                count_cal.if_else_sport(Sport_Plus_Activity.tv_sport_child_name.getText().toString(),total_time,40);
                sport_record_info.set(position,String.valueOf(total_time));
                notifyDataSetChanged();
            }
        });
        mTimePickerView.show();
    }


    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView tv_name,tv_record;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_record);
            tv_record = itemView.findViewById(R.id.tv_num);
        }
    }
}
