package com.example.couponunion.presenter.impl;

import android.content.pm.PackageManager;

import com.example.couponunion.model.Api;
import com.example.couponunion.model.domain.SearchRecommend;
import com.example.couponunion.model.domain.SearchResult;
import com.example.couponunion.presenter.ISearchPresenter;
import com.example.couponunion.utils.LogUtils;
import com.example.couponunion.utils.RetrofitManager;
import com.example.couponunion.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {

    private final Api mApi;
    private ISearchPageCallback mSearchViewCallback;

    private static final int DEFAULT_PAGE = 0;
    private int mCurrentPage = 0;
    private String mCurrentKeyWord = null;

    public SearchPresenter() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getSearchHistories() {

    }

    @Override
    public void delSearchHistories() {

    }

    @Override
    public void doSearch(String keyWord) {
        //当前的搜索词为null或者传入的搜索词与当前搜素词不同时，赋值
        if (mCurrentKeyWord == null || !mCurrentKeyWord.equals(keyWord)){
            mCurrentKeyWord = keyWord;
            mCurrentPage = DEFAULT_PAGE;
         }
        Call<SearchResult> task = mApi.getSearchResult(mCurrentPage, keyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int resultCode = response.code();
                LogUtils.d(SearchPresenter.this, "doSearch resultCode ==>" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    //处理搜索结果
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onError();
            }
        });
    }

    /**
     * 处理搜索返回的结果
     * @param result  接口返回的结果
     */
    private void handleSearchResult(SearchResult result) {
        if (isResultEmpty(result)) {
            //数据为空
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        } else {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onSearchSuccess(result);
            }
        }

    }

    /**
     * 检测接口返回的搜索结果是否为空
     * @param result 搜索返回的结果
     * @return 是否为空
     */
    private boolean isResultEmpty(SearchResult result) {
        try {
            return (result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0);
        } catch (Exception e) {
            return true;
        }
    }

    private void onError() {
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onError();
        }
    }

    @Override
    public void research() {
        if (mCurrentKeyWord != null){
            doSearch(mCurrentKeyWord);
        } else {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onEmpty();
            }
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void loaderMore() {
        //判断当前搜索关键词是否为空，否则重新执行搜索
        if (mCurrentKeyWord == null) {
            //当前搜索词为空
            return;
        } else {
            mCurrentPage++;
            Call<SearchResult> task = mApi.getSearchResult(mCurrentPage, mCurrentKeyWord);
            task.enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    int resultCode = response.code();
                    LogUtils.d(SearchPresenter.this, "loaderMore resultCode ==>" + resultCode);
                    if (resultCode == HttpURLConnection.HTTP_OK) {
                        handleSearchMoreResult(response.body());
                    } else {
                        onLoaderMoreError();
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    t.printStackTrace();
                    onLoaderMoreError();
                }
            });

        }
    }

    /**
     * 加载更多失败
     */
    private void onLoaderMoreError() {
        //加载更多失败，需要把页码减回去
        mCurrentPage--;
        if (mSearchViewCallback != null) {
            mSearchViewCallback.onLoaderMoreError();
        }
    }

    private void handleSearchMoreResult(SearchResult result) {
        if (isResultEmpty(result)) {
            //数据为空
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onLoaderMoreEmpty();
            }
        } else {
            if (mSearchViewCallback != null) {
                mSearchViewCallback.onLoaderMoreSuccess(result);
            }
        }
    }

    @Override
    public void getRecommendedWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int resultCode = response.code();
                LogUtils.d(SearchPresenter.this, "getRecommendedWords resultCode ==>" + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    //TODO 热门搜索词结果处理

                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
        this.mSearchViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
        this.mSearchViewCallback = null;
    }
}
