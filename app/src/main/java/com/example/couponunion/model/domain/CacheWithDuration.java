package com.example.couponunion.model.domain;

/**
 * 带有时间有数据的缓存Bean
 */
public class CacheWithDuration {
    String cache;
    long duration;

    public CacheWithDuration(String cache, long duration) {
        this.cache = cache;
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public long getDuration() {
        return duration;
    }
}
