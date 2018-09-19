package com.luckyaf.smart.smartcache.model;

import android.support.annotation.NonNull;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class CacheValue<T>{
    private T data;
    private String key;
    public CacheValue(@NonNull String key,T data){
        this.key = key;
        this.data = data;
    }
    public T getData() {
        return data;
    }

    public boolean matchDataType(@NonNull Class type){
        return null == data || type.equals(this.data.getClass());
    }
    public boolean matchKey(@NonNull String key){
        return this.key.equals(key);
    }
}
