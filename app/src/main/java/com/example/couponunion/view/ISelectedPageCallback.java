package com.example.couponunion.view;

import com.example.couponunion.base.IBaseCallback;
import com.example.couponunion.model.domain.SelecetdPageCategory;
import com.example.couponunion.model.domain.SelectedPageContent;

public interface ISelectedPageCallback extends IBaseCallback {

    /**
     * 分类表信息加载
     * @param categories 分类表信息
     */
    void onCategoriesLoaded(SelecetdPageCategory categories);


    /**
     * 分类子项内容加载
     * @param content 分类子项内容
     */
    void onContentLoaded(SelectedPageContent content);
}
