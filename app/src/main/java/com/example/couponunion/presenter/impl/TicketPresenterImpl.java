package com.example.couponunion.presenter.impl;

import com.example.couponunion.model.Api;
import com.example.couponunion.model.domain.TicketParams;
import com.example.couponunion.model.domain.TicketResult;
import com.example.couponunion.presenter.ITicketPresenter;
import com.example.couponunion.utils.LogUtil;
import com.example.couponunion.utils.RetrofitManager;
import com.example.couponunion.utils.UrlUtils;
import com.example.couponunion.view.ITicketPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketPageCallback mViewCallback = null;

    private String mCover = null;
    private TicketResult mTicketResult = null;

    //数据的加载状态,为防止UI尚未初始化完毕便完成数据的获取从而无法将数据绑定到UI，需要在UI绑定处进行状态的检查从而再次绑定数据
    enum LoadState {
        LOAD_SUCCESS, LOADING, LOAD_ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        mCurrentState = LoadState.LOADING;
        mCover = cover;
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        //给url添加上https
        String targetUrl = UrlUtils.getTicketUrl(url);
        TicketParams ticketParam = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicketResult(ticketParam);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    mCurrentState = LoadState.LOAD_SUCCESS;
                    mTicketResult = response.body();
                    onTicketRequestSuccess();
                } else {
                    mCurrentState = LoadState.LOAD_ERROR;
                    LogUtil.d(TicketPresenterImpl.this, "请求失败! code ==> " + code);
                    onTicketRequestError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                LogUtil.d(TicketPresenterImpl.this, "请求失败!" + t.toString());
            }
        });

    }

    /**
     * 淘口令数据请求失败
     */
    private void onTicketRequestError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    /**
     * 淘口令数据请求成功
     */
    private void onTicketRequestSuccess() {
        if (mViewCallback != null) {
            mViewCallback.onTicketLoaded(mCover, mTicketResult);
        }
    }

    @Override
    public void registerViewCallback(ITicketPageCallback callback) {
        //数据的加载状态,为防止UI尚未初始化完毕便完成数据的获取从而无法将数据绑定到UI，需要在UI绑定处进行状态的检查从而再次绑定数据
        this.mViewCallback = callback;
        if (mCurrentState != LoadState.NONE) {
            if (mCurrentState == LoadState.LOAD_SUCCESS) {
                onTicketRequestSuccess();
            } else if (mCurrentState == LoadState.LOADING) {
                mViewCallback.onLoading();
            } else if (mCurrentState == LoadState.LOAD_ERROR) {
                onTicketRequestError();
            }
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPageCallback callback) {
        this.mViewCallback = null;
    }
}
