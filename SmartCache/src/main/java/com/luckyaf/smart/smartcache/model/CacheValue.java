package com.luckyaf.smart.smartcache.model;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class CacheValue {
    private Object data;
    private String key;
    public CacheValue(@NonNull String key, Object data){
        this.key = key;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public boolean matchDataType(@NonNull Class type){
        if(data != null) {
            Log.d("this.data.getClass()", this.data.getClass().getName());
        }
        Log.d("matchDataType",type.getName());
        return data == null || type.equals(this.data.getClass()) || type.equals(Object.class);
    }

    public boolean matchKey(@NonNull String key){
        Log.d("matchKey",key);
        return this.key.equals(key);
    }
}
