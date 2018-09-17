package com.luckyaf.smart.smartcache.callback;

import com.luckyaf.smart.smartcache.model.Response;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public interface CacheCallback<T> {
    void call(Response<T> response);
}
