package com.luckyaf.smart.smartcache.model;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class Response<T> {
    private boolean update;
    private T data;

    @SuppressWarnings("unchecked")
    public Response(Object value, boolean isUpdate){
        try {
            this.data = (T)value;
        }catch (ClassCastException e){
            this.data = null;
        }
        this.update = isUpdate;
    }

    public boolean isUpdate() {
        return update;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "update=" + update +
                ", data=" + data +
                '}';
    }
}
