package com.example.couponunion.presenter;

import com.example.couponunion.base.IBasePresenter;
import com.example.couponunion.view.IOnSellPageCallback;

public interface IOnSellPagePresenter extends IBasePresenter<IOnSellPageCallback> {

    /**
     * 获取特惠内容
     */
    void getOnSellContent();

    /**
     * 根据网络状态等重新加载内容
     */
    void reloadContent();

    /**
     * 加载更多
     */
    void loaderMore();


}
