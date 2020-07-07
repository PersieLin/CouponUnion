package com.example.couponunion.presenter;

import com.example.couponunion.base.IBasePresenter;
import com.example.couponunion.view.ITicketPageCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPageCallback> {

    /**
     * 获取淘口令
     * @param title 商品标题
     * @param url   商品链接
     * @param cover 商品图链接
     */
    void getTicket(String title, String url, String cover);
}
