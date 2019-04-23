package com.mis.relife.pages.sleep.day;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mis.relife.R;
import com.mis.relife.databinding.SleepDayFragmentBinding;

public class DayFragment extends Fragment {

    private DayModel vm;

    public DayFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SleepDayFragmentBinding binding = (SleepDayFragmentBinding)DataBindingUtil.inflate(inflater, R.layout.sleep_day_fragment, container, false);
        vm = new DayModel(getContext(),binding);
        binding.setDayModel(vm);
        View view = binding.getRoot();
        return view;
    }






}
