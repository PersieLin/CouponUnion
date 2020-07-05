package com.example.couponunion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private volatile static RetrofitManager instance;
    private Retrofit mRetrofit;


    private RetrofitManager() {
        //初始化Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null)
                    instance = new RetrofitManager();
            }
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

}
