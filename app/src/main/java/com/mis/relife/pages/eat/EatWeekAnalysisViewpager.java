package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mis.relife.R;
import com.mis.relife.data.AppDbHelper;
import com.mis.relife.data.MyCallBack;
import com.mis.relife.data.model.Diet;
import com.mis.relife.pages.sport.SportData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link EatWeekAnalysisViewpager.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link EatWeekAnalysisViewpager#newInstance} factory method to
// * create an instance of this fragment.
// */
@SuppressLint("ValidFragment")
public class EatWeekAnalysisViewpager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    //有用的地方
    private List<String> data = new ArrayList<>();
    private List<Integer> percent = new ArrayList<>();
    private List<Integer> cal = new ArrayList<>();
    private eat_week_gridview week_adapter;
    private EatData eatData;
    private SportData sportData;
    //介面區
    private Button btDatepicker,btLossCal,btAllCal;
    private GridView gv_menu;
    private TextView tvDayAverageCal,tvDayAverageLossCal;

    //圖表資料區
    private BarChart barChart;
    private BarData sportBarData;
    private XAxis xAxis;
    private YAxis leftAxis;
    private  YAxis rightAxis;
    private BarData barData;

    private int mYear,mMonth,mDay,monday;
    private int nowmYear,nowmMonth,nowmDay;
    private String date_format;
    float dayAverageCal = 0;
    //每餐總卡路里初始化
    private float breakfastCalTotal = 0;
    private float lunchCalTotal = 0;
    private float dinnerCalTotal = 0;
    private float nightSnackCalTotal = 0;
    private float snackCalTotal = 0;
    private float weekTotalCal = 0;
    private float dayTotalCal = 0;
    private float dayAvgLossCal = 0;
    //每餐%數宣告
    private float breakfastPercent;
    private float lunchPercent;
    private float dinnerPercent;
    private float nightSnackPercent;
    private float snackPercent;

    //圖表外觀資料區
    private  String[] week = {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private  int[] colors = {Color.rgb(178,34,34),Color.rgb(255,69,0)
            ,Color.rgb(240,230,140),Color.rgb(60,179,113),Color.rgb(176,196,222)
            ,Color.rgb(30,144,255),Color.rgb(147,112,219)};

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private String Monday;
    private Date date;
    private Calendar calendar;

    private int first = 0;
    private int week_sport_time_cnt;
    private Context context;


    public EatWeekAnalysisViewpager(EatData eatData, SportData sportData,Context context) {
        // Required empty public constructor
        this.eatData = eatData;
        this.sportData = sportData;
        this.context = context;
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment EatWeekAnalysisViewpager.
//     */
    // TODO: Rename and change types and number of parameters
//    public static EatWeekAnalysisViewpager newInstance(String param1, String param2) {
//        EatWeekAnalysisViewpager fragment = new EatWeekAnalysisViewpager();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.eat_week_analysis_viewpager_fragment,container,false);
        gv_menu = view.findViewById(R.id.gv_data2);
        barChart = view.findViewById(R.id.bar_chart);
        btDatepicker = view.findViewById(R.id.bt_datepicker);
        btAllCal = view.findViewById(R.id.bt_all);
        btLossCal = view.findViewById(R.id.bt_cal);
        tvDayAverageCal = view.findViewById(R.id.tv_day_average_all_num);
        tvDayAverageLossCal = view.findViewById(R.id.tv_day_average_num);
        btDatepicker.setOnClickListener(datepicker);
        btAllCal.setOnClickListener(bt_AllCal);
        btLossCal.setOnClickListener(bt_LossCal);


        if(first == 0){
            nowdate();
            btDatepicker.setText("今天");
            first++;
        }
        //設置圖表
        myInit();
        //設置底下gridview
        inidata();
        week_adapter = new eat_week_gridview(getActivity().getApplicationContext(),data,cal,percent);
        gv_menu.setAdapter(week_adapter);
        return view;
    }

    // 從資料庫取資料，這個會持續監聽新的資料，每當有資料改變，就會執行這個區塊
    private void myInit(){
        AppDbHelper.getAllDietFromFireBase(new MyCallBack<Map<String, Diet>>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onCallback(Map<String, Diet> value, DatabaseReference dataRef, ValueEventListener vlistenr) {
                if(!value.isEmpty()) {
                    Drawable whiteDrawable = context.getResources().getDrawable(R.drawable.button_rectangle);
                    Drawable grayDrawable = context.getResources().getDrawable(R.drawable.button_rectangle_gray);
                    btAllCal.setBackground(grayDrawable);
                    btLossCal.setBackground(whiteDrawable);
                    //繪製圖表
                    get_monday();
                    barData = getBarData();
                    barChart.setData(barData);
                    setBarchar();
                    //leftAxis.setAxisMinimum(0f);
                    getPercent();

                    tvDayAverageCal.setText(String.valueOf((int)dayAverageCal) + "cal");
                    tvDayAverageLossCal.setText(String.valueOf((int)dayAvgLossCal) + "cal");
                    //底下gridview區
                    inidata();
                    if(week_adapter != null) {
                        week_adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    //拿到圖表資料
    private BarData getBarData() {
        //將每一餐總卡路里初始化
        breakfastCalTotal = 0;
        lunchCalTotal = 0;
        dinnerCalTotal = 0;
        nightSnackCalTotal = 0;
        snackCalTotal = 0;
        //設置星期一到星期天
        int maxX = 7;
        //用來看有哪一天有吃東西
        int have,LossHave = 0;
        //有紀錄幾天
        int weekEatTime = 0;
        //每天運動消耗的卡路里
        float totalLossCal;
        //運動和飲食的barentry
        List<BarEntry> list = new ArrayList<>();
        List<BarEntry> sportList = new ArrayList<>();
        //初始化每天和每週的平均 全部 卡路里
        dayAverageCal = 0;
        dayTotalCal = 0;
        weekTotalCal = 0;
        dayAvgLossCal = 0;
        for (int i = 0; i < maxX; i++) {
            //先將X軸設好七天
            BarEntry barEntry = new BarEntry(i, 0);
            BarEntry sportBarEntry = new BarEntry(i, 0);
            //用來計算有哪幾天是有運動的
            have = 0;
            LossHave = 0;
            //每天運動消耗卡路里初始化
            totalLossCal = 0;
            //每天的卡路里總量初始化
            dayTotalCal = 0;
            //再來比對有對應到日記的日期 從資料庫裡的 recordDate 進行比對
            for (int a = 0; a < eatData.eatDate.size(); a++) {
                if (Monday.equals(eatData.eatDate.get(a))) {
                    //比對完日期對 每一餐做加總
                    mealCalTotal(a);
                    if(have == 0) {
                        weekEatTime++;
                        have++;
                    }
                }
            }
            for (int a = 0; a < sportData.sport_recordDate.size(); a++) {
                //對應指定的date
                if (Monday.equals(sportData.sport_recordDate.get(a))) {
                    //比對完日期做消耗卡路里加總
                    int sport_cal = Integer.valueOf(sportData.sport_cal.get(a));
                    totalLossCal += sport_cal;
                }
            }
            //塞入一條bar
            barEntry.setY(dayTotalCal);
            //如果當天沒有紀錄飲食就不做塞入淨值bar
            if (dayTotalCal == 0){
                sportBarEntry.setY(0);
            }
            else {
                sportBarEntry.setY(dayTotalCal - totalLossCal);
                //算出淨值卡路里總量
                dayAvgLossCal += dayTotalCal - totalLossCal;
                //加總有幾條淨值bar
                LossHave ++;
                System.out.println("!!!!!!!!!!!Loss" + totalLossCal);
                System.out.println("!!!!!!!!!!!Day" + dayTotalCal);
            }
            //將日期加一天
            calendar.add(Calendar.DAY_OF_MONTH,1);
            Monday = sdf.format(calendar.getTime());
            //塞入list
            list.add(barEntry);
            sportList.add(sportBarEntry);
        }
        //將一週的卡路里做加總
        weekTotalCal = breakfastCalTotal + lunchCalTotal + dinnerCalTotal + nightSnackCalTotal + snackCalTotal;
        //算出平均每天攝取卡路里
        if(weekEatTime != 0) {
            dayAverageCal = (int) weekTotalCal / weekEatTime;
        }
        //算出平均每天淨值卡路里
        if(LossHave != 0){
            dayAvgLossCal = (int)dayAvgLossCal / LossHave;
        }
        //將資料放進去(總值表)
        BarData barData = new BarData(getDataset(list));
        //將資料放進去(淨值表)
        sportBarData = new BarData(getDataset(sportList));
//        List<IBarDataSet> datasets = new ArrayList<>();
//        datasets.add(barset);
        return  barData;
    }

    private BarDataSet getDataset(List<BarEntry> list){
        BarDataSet barset = new BarDataSet(list, "");
        barset.setColors(colors);
        barset.setValueTextSize(10);
//        barset.setDrawValues(false);
        barset.setBarShadowColor(Color.GRAY);
        return barset;
    }

    //算出每一餐的總卡路里
    private void mealCalTotal(int i){
        if(eatData.category.get(i) == 1){
            breakfastCalTotal = getCalTotal(breakfastCalTotal,i);
        }
        else if(eatData.category.get(i) == 2){
            lunchCalTotal = getCalTotal(lunchCalTotal,i);
        }
        else if(eatData.category.get(i) == 3){
            dinnerCalTotal = getCalTotal(dinnerCalTotal,i);
        }
        else if(eatData.category.get(i) == 4){
            nightSnackCalTotal = getCalTotal(nightSnackCalTotal,i);
        }
        else if(eatData.category.get(i) == 5){
            snackCalTotal = getCalTotal(snackCalTotal,i);
        }
    }

    //得到每一餐的%數
    private void getPercent(){
        breakfastPercent =  breakfastCalTotal / weekTotalCal * 100;
        lunchPercent = lunchCalTotal / weekTotalCal * 100;
        dinnerPercent = dinnerCalTotal / weekTotalCal * 100;
        nightSnackPercent = nightSnackCalTotal / weekTotalCal * 100;
        snackPercent = snackCalTotal / weekTotalCal * 100;
    }

    //卡路里加總
    private float getCalTotal(float calTotal,int datePosition){
        for(int l = 0;l < eatData.foods.get(datePosition).size();l++){
            calTotal += eatData.foods.get(datePosition).get(l).cal * eatData.foods.get(datePosition).get(l).number;
            dayTotalCal += eatData.foods.get(datePosition).get(l).cal * eatData.foods.get(datePosition).get(l).number;
        }
        return  calTotal;
    }


    //拿到monday的day
    private void get_monday(){
        try {
            date = sdf.parse(date_format);
            date = getThisWeekMonday(date);
            Monday = sdf.format(date);
            System.out.println("Monday!!!!!!!!" + Monday);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //計算星期一是幾號
    private Date getThisWeekMonday(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
        return calendar.getTime();
    }

    //設定X軸的value
    private IAxisValueFormatter x_value = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            try{
                return week[(int) value];
            }
            catch (Exception e) {
                Log.d("ERROR TAG: " , "xAxis.setValueFormatter() -- " + e.toString());
                return "";
            }
        }
    };

    //設定圖表外觀
    private void setBarchar(){
        //不顯示圖表網格
        barChart.setDrawGridBackground(false);
        //不顯示description
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        //背景陰影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //顯示邊框
        barChart.setDrawBorders(false);

        barChart.animateY(3000);
        barChart.animateX(3000);

        barChart.setTouchEnabled(false);

        /***XY軸的設置***/
        xAxis = barChart.getXAxis();
        leftAxis = barChart.getAxisLeft();
        rightAxis = barChart.getAxisRight();
        //不顯示XY軸的線條
        xAxis.setDrawAxisLine(false);
        leftAxis.setDrawAxisLine(false);
        rightAxis.setDrawAxisLine(false);
        //X軸設置顯示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        //設定X的值
        xAxis.setValueFormatter(x_value);
        //保证Y轴从0开始，不然会上移一点
        //leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(7);
        leftAxis.setGranularity(2f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setEnabled(false);
        xAxis.setDrawGridLines(false);
        leftAxis.setDrawGridLines(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);
    }

    //拿到現在日期
    private void nowdate(){
        Calendar now = Calendar.getInstance();
        nowmYear = now.get(Calendar.YEAR);
        nowmMonth = now.get(Calendar.MONTH);
        nowmDay = now.get(Calendar.DAY_OF_MONTH);
        mYear = now.get(Calendar.YEAR);
        mMonth = now.get(Calendar.MONTH);
        mDay = now.get(Calendar.DAY_OF_MONTH);
        date_format = setDateFormat(mYear,mMonth,mDay);
    }

    private Button.OnClickListener bt_AllCal = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            Drawable whiteDrawable = getContext().getResources().getDrawable(R.drawable.button_rectangle);
            Drawable grayDrawable = getContext().getResources().getDrawable(R.drawable.button_rectangle_gray);
            btAllCal.setBackground(grayDrawable);
            btLossCal.setBackground(whiteDrawable);
            //繪製圖表
            get_monday();
            barData = getBarData();
            barChart.setData(barData);
            setBarchar();
            //leftAxis.setAxisMinimum(0f);
        }
    };

    private Button.OnClickListener bt_LossCal = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            Drawable whiteDrawable = getContext().getResources().getDrawable(R.drawable.button_rectangle);
            Drawable grayDrawable = getContext().getResources().getDrawable(R.drawable.button_rectangle_gray);
            btAllCal.setBackground(whiteDrawable);
            btLossCal.setBackground(grayDrawable);
            //繪製圖表
            get_monday();
            barData = getBarData();
            barChart.setData(sportBarData);
            setBarchar();
        }
    };

    private Button.OnClickListener datepicker = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {

            new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    date_format = setDateFormat(year,month,day);
                    if(nowmYear == year && nowmMonth == month && nowmDay == day)
                        btDatepicker.setText("今天");
                    else if(nowmYear == year && nowmMonth == month && nowmDay - 1 == day)
                        btDatepicker.setText("昨天");
                    else btDatepicker.setText(date_format);
                    mYear = year;
                    mMonth = month;
                    mDay = day;
                    myInit();
                }

            }, mYear,mMonth, mDay).show();
        }
    };

    //設定日期格式
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        String month,day;
        //判斷如果是個位數 日期前面要加個零
        if(monthOfYear / 10 == 0){
            month = "0" + String.valueOf(monthOfYear + 1);
        }
        else {
            month = String.valueOf(monthOfYear + 1);
        }
        if (dayOfMonth / 10 == 0){
            day = "0" + String.valueOf(dayOfMonth);
        }
        else {
            day = String.valueOf(dayOfMonth);
        }
        //設定日期格式
        return String.valueOf(year) + "/"
                + month + "/"
                + day;
    }

    private void inidata(){
        data.clear();
        percent.clear();
        cal.clear();
        data.add("早餐");
        data.add("午餐");
        data.add("晚餐");
        data.add("宵夜");
        data.add("點心");
        percent.add((int) breakfastPercent);
        percent.add((int)lunchPercent);
        percent.add((int)dinnerPercent);
        percent.add((int)nightSnackPercent);
        percent.add((int)snackPercent);
        cal.add((int)breakfastCalTotal);
        cal.add((int)lunchCalTotal);
        cal.add((int)dinnerCalTotal);
        cal.add((int)nightSnackCalTotal);
        cal.add((int)snackCalTotal);
    }



    //沒有用的地方
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
