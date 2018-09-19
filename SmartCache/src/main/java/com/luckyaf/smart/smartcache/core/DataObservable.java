package com.luckyaf.smart.smartcache.core;

import com.luckyaf.smart.smartcache.SmartCache;
import com.luckyaf.smart.smartcache.model.Response;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/17
 */
public class DataObservable<T> extends Observable<Response<T>> {

    private String key;
    private Class<T> clazz;
    private T defaultValue;

    public DataObservable(String key, Class<T> clazz,T defaultValue) {
        this.key = key;
        this.clazz = clazz;
        this.defaultValue = defaultValue;
    }

    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        T data = SmartCache.get(key);
        if(null == data || !data.getClass().equals(clazz)){
            data = defaultValue;
        }
        UpdateListener<T> updateListener = new UpdateListener<>(key, observer, data, this.clazz);
        observer.onSubscribe(updateListener);
    }

    final static class UpdateListener<T> extends MainThreadDisposable {
        private Disposable disposable;
        UpdateListener(String key, Observer<? super Response<T>> observer, T data, Class<T> dataType) {
            disposable = CacheKeyBus
                    .getInstance()
                    .toFlowable(key, dataType)
                    .subscribe(value -> observer.onNext(new Response<>(value.getData(), true)));
            if (null == data || data.getClass().equals(dataType)) {
                observer.onNext(new Response<>(data, false));
            }
        }
        @Override
        protected void onDispose() {
            if (null != disposable && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

}
