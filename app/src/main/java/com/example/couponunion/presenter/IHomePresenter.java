package com.example.couponunion.presenter;

import com.example.couponunion.base.IBasePresenter;
import com.example.couponunion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {

    //获取分类数据
    void getCategories();

}
