package com.mis.relife.pages.eat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mis.relife.R;

@SuppressLint("ValidFragment")
public class eat_new_viewpager_new  extends Fragment {

    Context context;
    private TextView tv_eat;
    String eat;
    private EditText ed_search;
    private ListView lv_new;
    public EatSearchData eatSearchData;

    public eat_new_viewpager_new(Context context,String eat) {
        this.context = context;
        this.eat = eat;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.eat_new_viewpager_new,container,false);
        tv_eat = view.findViewById(R.id.tv_eat);
        ed_search = view.findViewById(R.id.ed_search);
        lv_new = view.findViewById(R.id.listview_new);
        eatSearchData = new EatSearchData(ed_search,lv_new,getActivity(),getLayoutInflater());
        return view;
    }
}
