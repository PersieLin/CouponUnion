package com.example.couponunion.presenter;

import com.example.couponunion.base.IBasePresenter;
import com.example.couponunion.view.ISearchPageCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchPageCallback> {


    /**
     * 获取搜索历史
     */
    void getSearchHistories();


    /**
     * 删除搜索历史
     */
    void delSearchHistories();

    /**
     * 执行搜索操作
     * @param keyWork 搜索关键词
     */
    void doSearch(String keyWork);

    /**
     * 网络不好时重新执行搜索
     */
    void research();

    /**
     * 搜索结果获取更多
     */
    void loaderMore();

    /**
     * 获取热门搜索关键词
     */
    void getRecommendedWords();
}
