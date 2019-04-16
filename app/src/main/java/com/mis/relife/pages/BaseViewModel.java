package com.mis.relife.pages;

import android.databinding.BaseObservable;

public abstract class BaseViewModel extends BaseObservable {
    public BaseViewModel(){ myInit(); }
    public abstract void myInit();
}
