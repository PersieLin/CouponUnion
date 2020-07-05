package com.example.couponunion.presenter;

import com.example.couponunion.base.IBasePresenter;
import com.example.couponunion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    //获取分类页面数据
    void getContentByCategoryId(int categoryId);

    //加载更多
    void loaderMore(int categoryId);

    //重新加载
    void reload(int categoryId);


}
