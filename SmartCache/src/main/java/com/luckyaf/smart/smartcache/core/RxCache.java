package com.luckyaf.smart.smartcache.core;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.luckyaf.smart.smartcache.CacheConfig;
import com.luckyaf.smart.smartcache.model.CacheValue;
import com.luckyaf.smart.smartcache.model.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Executor;

import io.reactivex.internal.schedulers.IoScheduler;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class RxCache {

    private static RxCache Instance;

    private static LruCache<String, Object> mLruCache;

    private static DiskLruCache mDiskLruCache;

    private RxCache(CacheConfig cacheConfig) {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        // 实例化LruCache对象
        mLruCache = new LruCache<>(maxSize);
        File diskCacheDir = new File(cacheConfig.getApplication().getCacheDir() + cacheConfig.getDirectory());
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        try {
            mDiskLruCache = DiskLruCache.open(diskCacheDir,  cacheConfig.getAppVersion(),1, cacheConfig.getMaxSize());
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static <T> T get(String key) {
        String saveKey = CacheUtil.md5(key);
        Object data = null;
        if (mLruCache.snapshot().containsKey(saveKey)) {
            data = mLruCache.get(saveKey);
        } else {
            try {
                //通过设置的 key 去获取缩略对象
                DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                //通过 SnapShot 对象获取流数据
                if(null != snapshot) {
                    InputStream in = snapshot.getInputStream(0);
                    ObjectInputStream ois = new ObjectInputStream(in);
                    //将流数据转换为 Object 对象
                    data = ois.readObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (T) data;
    }

    public static void put(@NonNull String key, Object data) {

        if(null != data) {
            try {
                putIntoDisk(key, data);
                putIntoMemory(key, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                remove(key);
                notifyChanged(key,data);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void putIntoMemory(String key, Object data) {
        String saveKey = CacheUtil.md5(key);
        mLruCache.put(saveKey, data);
        notifyChanged(key, data);
    }

    private static void putIntoDisk(String key, Object data) throws IOException {
        String saveKey = CacheUtil.md5(key);
        DiskLruCache.Editor editor = mDiskLruCache.edit(saveKey);
        OutputStream os = editor.newOutputStream(0);
        //此处存的一个 新闻对象因此用 ObjectOutputStream
        ObjectOutputStream outputStream = new ObjectOutputStream(os);
        outputStream.writeObject(data);
        //别忘了关闭流和提交编辑
        outputStream.close();
        editor.commit();
    }

    public static void remove(@NonNull String key){
        mLruCache.remove(key);
        try {
            mDiskLruCache.remove(key);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * 通知所有人 这个key对应的值更改了
     *
     * @param key
     */
    private static void notifyChanged(String key, Object o) {
        CacheKeyBus.getInstance().post(new CacheValue(key, o));
    }


    public static void clear(){
       try {
           mDiskLruCache.delete();
       }catch (IOException e){
           e.printStackTrace();
       }
    }

    public static void init(CacheConfig cacheConfig) {
        Instance = new RxCache(cacheConfig);
    }


}
