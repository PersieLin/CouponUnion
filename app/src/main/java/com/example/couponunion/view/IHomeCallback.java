package com.example.couponunion.view;

import com.example.couponunion.base.IBaseCallback;
import com.example.couponunion.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {

    //通知UI加载数据
    void onCategoriesLoaded(Categories categories);

}
