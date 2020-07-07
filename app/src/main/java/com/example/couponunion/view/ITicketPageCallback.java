package com.example.couponunion.view;

import com.example.couponunion.base.IBaseCallback;
import com.example.couponunion.model.domain.TicketResult;

public interface ITicketPageCallback extends IBaseCallback {
    /**
     * 淘口令加载
     * @param cover   商品图片链接
     * @param result 商品口令结果
     */
    void onTicketLoaded(String cover, TicketResult result);
}
