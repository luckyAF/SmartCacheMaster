package com.luckyaf.smart.smartcache.core;

import android.support.annotation.NonNull;

import com.luckyaf.smart.smartcache.model.CacheValue;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class CacheKeyBus {

    private final FlowableProcessor<Object> bus;

    // PublishSubject只会把在订阅发生的时间点之后来自原始Flowable的数据发射给观察者

    private CacheKeyBus()
    {
        bus = PublishProcessor.create().toSerialized();
    }

    public static CacheKeyBus getInstance() {
        return RxBusHolder.INSTANCE;
    }

    private static class RxBusHolder {
        private static final CacheKeyBus INSTANCE = new CacheKeyBus();
    }


    /**
     * 发送有更新的key value
     */
    public void post(CacheValue value) {
        bus.onNext(value);
    }

    /**
     * 根据传递的 key值 类型返回特定key的 被观察者
     * @return  Flowable<String>
     */
    public <T> Flowable<CacheValue> toFlowable(@NonNull String key,@NonNull Class<T> type) {
        return bus.ofType(CacheValue.class)
                .filter(tCacheValue -> tCacheValue.matchKey(key))
                .filter(tCacheValue -> tCacheValue.matchDataType(type))
                ;
    }


}
