package com.example.couponunion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.couponunion.base.BaseApplication;
import com.example.couponunion.model.domain.CacheWithDuration;
import com.google.gson.Gson;

/**
 * 使用SharePreference缓存Json数据的工具类
 * 1.获取sp，将需要保存的数据处理成Json格式后保存
 * 2.可以保存任意类型的数据，使用泛型进行处理
 */
public class JsonCacheUtils {
    private static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    //单例模式
    private JsonCacheUtils() {
        //SharedPreferences 的获取
        mSharedPreferences = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();

    }

    private JsonCacheUtils mInstance = null;

    public JsonCacheUtils getInstance() {
        if (mInstance == null) {
            mInstance = new JsonCacheUtils();
        }
        return mInstance;
    }


    /**
     * 保存缓存数据（默认duration为-1，即没有过期时间)0
     *
     * @param key   键
     * @param value 值
     */
    public void saveCache(String key, Object value) {
        this.saveCache(key, value, -1L);
    }

    /**
     * 保存缓存数据
     *
     * @param key      键
     * @param value    值
     * @param duration 过期时间
     */
    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        //先将传入的值转为String，再结合保存时长，保存在一个有时间有数据的bean类中，最后再以键值对的形式保存到sp中
        String valueStr = mGson.toJson(value);
        if (duration != -1){
            //当前时间加上保存时间
            duration += System.currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(valueStr, duration);
        //再将有时间有数据的内容转为json保存到SP中
        String cacheWithTime = mGson.toJson(cacheWithDuration);
        editor.putString(key, cacheWithTime);
        //切记要应用修改
        editor.apply();
    }

    /**
     * 删除缓存数据
     * @param key
     */
    public void deleteCache(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    /**
     * 修改缓存数据
     *
     * @param key
     * @param modifiedValue
     */
    public void modifyCache(String key, String modifiedValue) {

    }

    /**
     * 可以保存任意类型的数据，因此需要使用泛型的方式来返回类型值
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends Class<T>> T getCache(String key, Class<T> clazz) {
        String valueStr = mSharedPreferences.getString(key, null);
        if (valueStr != null) {
            //当取出的结果不为空时，需要转为对应类型的数据
            CacheWithDuration cacheWithDuration = mGson.fromJson(valueStr, CacheWithDuration.class);
            long duration = cacheWithDuration.getDuration();
            if (duration != -1 && duration - System.currentTimeMillis() <= 0) {
                //判断保存时长，如果过期则返回空值
                return null;
            } else {
                //返回相对应类型的数据
                return mGson.fromJson(cacheWithDuration.getCache(), clazz);
            }
        } else {
            return null;
        }
    }


}
