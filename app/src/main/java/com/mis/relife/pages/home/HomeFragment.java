package com.mis.relife.pages.home;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mis.relife.R;
import com.mis.relife.databinding.FragmentHomeBinding;

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {

    private HomeFragmentModel vm;
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        vm = new HomeFragmentModel(getActivity());
        binding.setHomeFragmentModel(vm);
        View view = binding.getRoot();
        return view;
    }
    public HomeFragmentModel getViewModel(){return this.vm;}
}
