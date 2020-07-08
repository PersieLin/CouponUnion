package com.example.couponunion.presenter.impl;

import com.example.couponunion.model.Api;
import com.example.couponunion.model.domain.SelecetdPageCategory;
import com.example.couponunion.model.domain.SelectedPageContent;
import com.example.couponunion.presenter.ISelectedPagePresenter;
import com.example.couponunion.utils.LogUtils;
import com.example.couponunion.utils.RetrofitManager;
import com.example.couponunion.utils.UrlUtils;
import com.example.couponunion.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {

    private Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
    private Api mApi = retrofit.create(Api.class);

    private ISelectedPageCallback mViewCallback;


    @Override
    public void getCategories() {
        mViewCallback.onLoading();
        //获取数据内容
        Call<SelecetdPageCategory> task = mApi.getSelectedPageCategories();
        task.enqueue(new Callback<SelecetdPageCategory>() {
            @Override
            public void onResponse(Call<SelecetdPageCategory> call, Response<SelecetdPageCategory> response) {
                int resultCode = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this, "resultCode == >" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelecetdPageCategory categoryResult = response.body();
                    LogUtils.d(SelectedPagePresenterImpl.this, "getCategories ==>" + categoryResult.toString());
                    if (mViewCallback != null) {
                        mViewCallback.onCategoriesLoaded(categoryResult);
                    } else {
                        onLoadedError();
                    }
                }
            }

            @Override
            public void onFailure(Call<SelecetdPageCategory> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelecetdPageCategory.DataBean category) {
        int categoryId = category.getFavorites_id();
        //获取分类内容的拼接URL
        String targetUrl = UrlUtils.getSelectedContentUrl(categoryId);
        LogUtils.d(this, "getSelectedContentUrl == > " +targetUrl);
        Call<SelectedPageContent> task = mApi.getSelectedPageContents(targetUrl);
        //根据分类id请求内容数据
        task.enqueue(new Callback<SelectedPageContent>() {
            @Override
            public void onResponse(Call<SelectedPageContent> call, Response<SelectedPageContent> response) {
                int resultCode = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this, "resultCode == >" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedPageContent contentResult = response.body();
                    if (mViewCallback != null) {
                        mViewCallback.onContentLoaded(contentResult);
                    } else {
                        onLoadedError();
                    }
                }
            }

            @Override
            public void onFailure(Call<SelectedPageContent> call, Throwable t) {
                onLoadedError();
            }
        });
    }

    @Override
    public void reloadContent() {
        getCategories();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mViewCallback = null;
    }
}
