package com.mis.relife.data.dao;

import com.mis.relife.data.MyCallBack;
import com.google.android.gms.tasks.Task;

public interface MyDao<T,L> {
    public Task<Void> insert(Object value);
    public Task<Void> update(String key,Object value);

    public Task<Void> delete(String key);
    public Task<Void> deleteAll();

    public void load(String key,Object value,final MyCallBack<T> myCallback);
    public void loadAll(final MyCallBack<L> myCallback);
}
