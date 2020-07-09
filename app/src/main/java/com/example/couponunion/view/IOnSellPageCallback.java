package com.example.couponunion.view;

import com.example.couponunion.base.IBaseCallback;
import com.example.couponunion.model.domain.OnSellContent;

public interface IOnSellPageCallback extends IBaseCallback {

    /**
     * 加载特惠内容
     * @param content 特惠商品内容
     */
    void onContentLoaded(OnSellContent content);

    /**
     * 加载更多
     */
    void onMoreLoaded(OnSellContent content);

    /**
     * 加载更多错误
     */
    void onMoreLoadedError();


    /**
     * 加载更多为空
     */
    void onMoreLoadedEmpty();
}
