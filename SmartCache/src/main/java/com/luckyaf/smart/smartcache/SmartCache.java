package com.luckyaf.smart.smartcache;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.luckyaf.smart.smartcache.core.CacheKeyBus;
import com.luckyaf.smart.smartcache.core.CacheUtil;
import com.luckyaf.smart.smartcache.core.DataObservable;
import com.luckyaf.smart.smartcache.model.CacheValue;
import com.luckyaf.smart.smartcache.model.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import io.reactivex.Observable;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
@SuppressWarnings("unused")
public class SmartCache {

    private static SmartCache mInstance;
    private LruCache<String, Object> mLruCache;
    private DiskLruCache mDiskLruCache;
    private String cacheDir;
    private int maxSize;
    private int appVersion;


    private SmartCache(Application context, String cacheFile, int appVersion, int size) {
        this.appVersion = appVersion;
        this.maxSize = size;
        this.cacheDir = context.getFilesDir().getAbsolutePath() + cacheFile;
        int memorySize = (int) (Runtime.getRuntime().maxMemory() / 8);
        // 实例化LruCache对象
        mLruCache = new LruCache<>(memorySize);
    }

    private DiskLruCache produceCache() {
        if (null == mDiskLruCache || mDiskLruCache.isClosed()) {
            try {
                File diskCacheDir = new File(cacheDir);
                if (!diskCacheDir.exists() && !diskCacheDir.mkdirs()) {
                    throw new Exception("can't create cache dir");
                }
                mDiskLruCache = DiskLruCache.open(diskCacheDir, appVersion, 1, maxSize);
            } catch (Exception e) {
                e.printStackTrace();
                mDiskLruCache = null;
            }
        }
        return mDiskLruCache;

    }


    public static void put(@NonNull String key, Serializable value) {
        String saveKey = CacheUtil.md5(key);

        try {
            DiskLruCache cache = getInstance().produceCache();
            if (null == cache) {
                return;
            }
            DiskLruCache.Editor editor = cache.edit(saveKey);

            if (null == value) {
                cache.remove(saveKey);
                getInstance().getLruCache().remove(saveKey);
                notifyChanged(key, null);
                return;
            }
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                outputStream.write(CacheUtil.object2ByteArray(value));
                editor.commit();
                cache.flush();
            }
            closeCache(cache);
            getInstance().getLruCache().put(saveKey, value);
            notifyChanged(key, value);
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public static <T> T get(@NonNull String key) {
        String saveKey = CacheUtil.md5(key);
        T data;
        try {
            data = (T) getInstance().getLruCache().get(key);
        } catch (Exception e) {
            data = null;
        }
        if (null == data) {
            try {
                DiskLruCache cache = getInstance().produceCache();
                if (cache == null) {
                    return null;
                }
                DiskLruCache.Snapshot snapShot = cache.get(saveKey);
                if (snapShot != null) {
                    InputStream is = snapShot.getInputStream(0);
                    data = (T) CacheUtil.byteArray2Object(CacheUtil.input2ByteArray(is));
                    if (data != null) {
                        getInstance().getLruCache().put(saveKey, data);
                    }
                }
                closeCache(cache);
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }
        return data;
    }

    public static Observable<Response<Boolean>> observeBoolean(@NonNull String key) {
        return observe(key, Boolean.class);
    }

    public static Observable<Response<Byte>> observeByte(@NonNull String key) {
        return observe(key, Byte.class);
    }

    public static Observable<Response<Character>> observeCharacter(@NonNull String key) {
        return observe(key, Character.class);
    }

    public static Observable<Response<Short>> observeShort(@NonNull String key) {
        return observe(key, Short.class);
    }

    public static Observable<Response<Integer>> observeInteger(@NonNull String key) {
        return observe(key, Integer.class);
    }

    public static Observable<Response<Long>> observeLong(@NonNull String key) {
        return observe(key, Long.class);
    }

    public static Observable<Response<Float>> observeFloat(@NonNull String key) {
        return observe(key, Float.class);
    }

    public static Observable<Response<Double>> observeDouble(@NonNull String key) {
        return observe(key, Double.class);
    }

    public static Observable<Response<String>> observeString(@NonNull String key) {
        return observe(key, String.class);
    }

    @SuppressWarnings("unchecked")
    public static <T> Observable<Response<T>> observe(@NonNull String key, @NonNull Class<T> clazz) {
        Object defaultValue;
        if (clazz.equals(Boolean.class)) {
            defaultValue = Boolean.FALSE;
        } else if (clazz.equals(Byte.class)) {
            defaultValue = 0;
        } else if (clazz.equals(Character.class)) {
            defaultValue = Character.MIN_VALUE;
        } else if (clazz.equals(Short.class)) {
            defaultValue = 0;
        } else if (clazz.equals(Integer.class)) {
            defaultValue = 0;
        } else if (clazz.equals(Long.class)) {
            defaultValue = 0;
        } else if (clazz.equals(Float.class)) {
            defaultValue = 0.0f;
        } else if (clazz.equals(Double.class)) {
            defaultValue = 0.0;
        } else {
            defaultValue = null;
        }
        return new DataObservable<>(key, clazz, (T) defaultValue);
    }

    public static <T> Observable<Response<T>> observe(@NonNull String key, @NonNull Class<T> clazz, T defaultValue) {
        return new DataObservable<>(key, clazz, defaultValue);
    }


    public static void clear() {
        getInstance().getLruCache().evictAll();
        try {
            DiskLruCache cache = getInstance().produceCache();
            cache.delete();
            closeCache(cache);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean removeData(@NonNull String key) {
        boolean success = false;
        try {
            DiskLruCache cache = getInstance().produceCache();
            cache.delete();
            getInstance().getLruCache().remove(key);
            success = true;
            notifyChanged(key, null);
            closeCache(cache);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 通知所有人 这个key对应的值更改了
     *
     * @param key key
     */
    private static <T> void notifyChanged(String key, T data) {
        CacheKeyBus.getInstance().post(new CacheValue<>(key, data));
    }


    private static SmartCache getInstance() {
        if (null == mInstance) {
            throw new RuntimeException("need init first");
        }
        return mInstance;
    }


    private static void closeCache(DiskLruCache cache) {
        try {
            if (null != cache) {
                cache.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private LruCache<String, Object> getLruCache() {
        return mLruCache;
    }


    public static SmartCacheBuilder newBuilder(Application app) {
        return new SmartCacheBuilder(app);
    }

    public static class SmartCacheBuilder {
        private Application context;
        private String directory;
        private int maxSize;
        private int appVersion;

        SmartCacheBuilder(@NonNull Application context) {
            this.context = context;
            this.maxSize = 50 * 1024 * 1024;
            this.directory = "Cache";
            this.appVersion = 1;
        }

        SmartCacheBuilder maxSize(int size) {
            this.maxSize = size;
            return this;
        }

        SmartCacheBuilder directory(String file) {
            this.directory = file;
            return this;
        }

        SmartCacheBuilder appVersion(int version) {
            this.appVersion = version;
            return this;
        }

        public void build() {
            mInstance = new SmartCache(context, directory, appVersion, maxSize);
        }


    }

}
