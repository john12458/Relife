package com.mis.relife.pages.sleep.viewPager;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyPieChart {
    private final DayModel vm;
    private final SimpleDateFormat timeSdf;
    private PieChart pieChart;
    private List<PieEntry> entries = new ArrayList<>();

    public MyPieChart(DayModel vm, PieChart pieChart) {
        this.vm = vm;
        this.pieChart = pieChart;
        this.timeSdf = new SimpleDateFormat("HH:mm:ss");
    }

    //重整
    public void resume() {
        pieChart.clear();
        pie_info();
        pie_initial();
    }

    //初始化圖表
    private void pie_initial() {
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        set.setColors(new int[]{Color.rgb(250, 250, 210), Color.rgb(222, 184, 135)});
        set.setSliceSpace(5f);
        set.setValueTextSize(14f);
        set.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setFormSize(18);
        pieChart.getLegend().setTextColor(Color.WHITE);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    //設置圖表資訊
    private void pie_info() {
        entries.clear();
        entries.add(new PieEntry(vm.sleepPercent.get(), "睡眠"));
        entries.add(new PieEntry(vm.wakePercent.get(), "清醒"));
    }
}
