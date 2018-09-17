package com.luckyaf.smart.smartcache;

import android.app.Application;
import android.support.annotation.NonNull;

import com.luckyaf.smart.smartcache.callback.CacheCallback;
import com.luckyaf.smart.smartcache.core.DiskLruCache;
import com.luckyaf.smart.smartcache.core.RxCache;
import com.luckyaf.smart.smartcache.model.Response;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class SmartCache {


    public static CacheConfig init(Application application){
        return new CacheConfig(application);
    }

    public static void put(@NonNull String key, Object object){
        RxCache.put(key,object);

    }

    public static void putTemp(@NonNull String key, Object object){
        RxCache.putIntoMemory(key,object);
    }

    public static void remove(@NonNull String key){
        RxCache.remove(key);
    }


    public static  <T> Observable<Response<T>> get(String key){
        return new CacheObservable<>(key);
    }


    public static void clear(){
        RxCache.clear();
    }


}
