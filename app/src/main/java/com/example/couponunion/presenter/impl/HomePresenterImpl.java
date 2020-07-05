package com.example.couponunion.presenter.impl;

import com.example.couponunion.model.Api;
import com.example.couponunion.model.domain.Categories;
import com.example.couponunion.presenter.IHomePresenter;
import com.example.couponunion.utils.LogUtil;
import com.example.couponunion.utils.RetrofitManager;
import com.example.couponunion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private IHomeCallback mCallback;

    @Override
    public void getCategories() {
        //从网络接口获取数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        //发起异步请求
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //请求响应成功
                int resultCode = response.code();
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    //请求成功,获得返回的categories数据
                    Categories categories = response.body();
                    if (mCallback != null) {
                        if (categories == null || categories.getData().size() == 0) {    //判空
                            //请求返回的数据为空
                            mCallback.onEmpty();
                        } else {
                            mCallback.onCategoriesLoaded(categories);
                        }
                    } else {
                        LogUtil.d(HomePresenterImpl.this, "callback is null!");
                    }
                } else {
                    //当响应的code不为200时，设置状态为error
                    mCallback.onError();
                    LogUtil.e(HomePresenterImpl.this, "resultCode -->" + resultCode);
                }

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //请求响应失败
                if (mCallback != null) {
                    t.printStackTrace();
                    mCallback.onError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        if (mCallback != null) {
            mCallback = null;
        }
    }
}
