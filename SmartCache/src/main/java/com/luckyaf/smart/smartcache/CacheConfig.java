package com.luckyaf.smart.smartcache;

import android.app.Application;

import com.luckyaf.smart.smartcache.core.RxCache;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/13
 */
public class CacheConfig {
    private Application application;
    private String directory;
    private int appVersion;
    private long maxSize;

    public CacheConfig(Application application) {
        this.application = application;
        maxSize = 10 * 1024 * 1024;
        directory = "cache";
        appVersion = 1;
    }

    public CacheConfig appVersion(int appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public CacheConfig cacheDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    public CacheConfig maxSize(long cacheSize) {
        this.maxSize = cacheSize;
        return this;
    }

    public void build() {
        RxCache.init(this);
    }

    public Application getApplication() {
        return application;
    }

    public String getDirectory() {
        return directory;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public long getMaxSize() {
        return maxSize;
    }
}
