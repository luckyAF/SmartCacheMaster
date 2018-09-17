package com.luckyaf.smart.smartcache.core;

import java.lang.reflect.ParameterizedType;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/17
 */
public class ClassType<T>{
    public Class<T> getTClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }



    public static  <T> Class<T> getTheClass() {
        return (new ClassType<T>() {}).getTClass();
    }
}
