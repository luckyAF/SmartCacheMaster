package com.luckyaf.smart.smartcachemaster;

import android.app.Application;

import com.luckyaf.smart.smartcache.SmartCache;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/14
 */
public class App extends Application{
    @Override
    public void onCreate() {
        SmartCache.newBuilder(this).build();
        super.onCreate();
    }
}
