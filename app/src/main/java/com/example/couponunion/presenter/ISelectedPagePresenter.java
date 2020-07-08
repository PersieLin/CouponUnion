package com.example.couponunion.presenter;

import com.example.couponunion.base.IBasePresenter;
import com.example.couponunion.model.domain.SelecetdPageCategory;
import com.example.couponunion.view.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {
    /**
     * 获取分类表内容
     */
    void getCategories();

    /**
     * 获取分类子项内容
     * @param category  分类
     */
    void getContentByCategory(SelecetdPageCategory.DataBean category);

    /**
     * 重新加载内容
     */
    void reloadContent();
}
