package com.luckyaf.smart.smartcache;

import android.widget.TextView;

import com.luckyaf.smart.smartcache.core.CacheKeyBus;
import com.luckyaf.smart.smartcache.core.ClassType;
import com.luckyaf.smart.smartcache.core.RxCache;
import com.luckyaf.smart.smartcache.model.CacheValue;
import com.luckyaf.smart.smartcache.model.Response;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 类描述：观察有更新的key
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class CacheObservable<T> extends Observable<Response<T>> {

    private String key;

    public CacheObservable(String key){
        this.key = key;
    }


    @Override
    protected void subscribeActual(Observer<? super Response<T>> observer) {
        T data = RxCache.get(key);
        Class dataType  = data == null ? Object.class : data.getClass();
        UpdateListener<T> updateListener = new UpdateListener(key,observer,data,dataType);
        observer.onSubscribe(updateListener);
    }

    final static class UpdateListener<T> extends MainThreadDisposable {

        private Disposable disposable;

        public UpdateListener(String key, Observer<Response<T>> observer,T data,Class<T> dataType){
            disposable = CacheKeyBus
                    .getInstance()
                    .toFlowable(key,dataType)
                    .subscribe(value -> observer.onNext(new Response(value.getData(),true))) ;
            observer.onNext(new Response<T>(data,false));
        }

        @Override
        protected void onDispose() {
            if(!disposable.isDisposed()){
                disposable.dispose();
            }
        }
    }

}
