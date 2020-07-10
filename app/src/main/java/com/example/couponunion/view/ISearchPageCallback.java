package com.example.couponunion.view;

import com.example.couponunion.base.IBaseCallback;
import com.example.couponunion.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索成功返回结果
     * @param result 搜索结果
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载更多成功
     * @param result 加载结果
     */
    void onLoaderMoreSuccess(SearchResult result);

    /**
     * 加载更多出错
     */
    void onLoaderMoreError();

    /**
     * 没有更多内容加载了
     */
    void onLoaderMoreEmpty();

    /**
     * 热门搜索词回调
     * @param recommendWordList 热门搜索词集合
     */
    void onLoadedRecommended(List<String> recommendWordList);

    /**
     * 搜索历史结果
     * @param historiesList 搜索历史词集合
     */
    void onLoadedHistories(List<String> historiesList);

}
