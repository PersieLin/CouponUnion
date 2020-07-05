package com.example.couponunion.view;

import com.example.couponunion.base.IBaseCallback;
import com.example.couponunion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    //回调数据到UI显示
    void onCategoryContentLoaded(List<HomePagerContent.DataBean> contents);

    //加载更多
    void onLoaderMore(HomePagerContent.DataBean content);

    //加载更多错误
    void onLoaderMoreError(int categoryId);

    //没有更多内容可以加载了
    void onLoaderMoreEmpty(int categoryId);

    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();

}
