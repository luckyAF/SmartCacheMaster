package com.luckyaf.smart.smartcache;

import com.luckyaf.smart.smartcache.core.RxCache;
import com.luckyaf.smart.smartcache.model.Response;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/14
 */
public class OnceCacheObservable<T> extends Observable<Response<T>> {

    private String key;

    public OnceCacheObservable(String key){
        this.key = key;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        T data = RxCache.get(key);
        observer.onNext(new Response<T>(data,false));
    }
}
