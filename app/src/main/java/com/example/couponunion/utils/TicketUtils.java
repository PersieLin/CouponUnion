package com.example.couponunion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.couponunion.model.domain.IBaseInfo;
import com.example.couponunion.presenter.ITicketPresenter;
import com.example.couponunion.ui.activity.TicketActivity;


public class TicketUtils {

    public static void toTicketPage(Context context, IBaseInfo baseInfo) {
        //特惠列表内容被点击
        //处理数据
        String title = baseInfo.getTitle();
        //详情的地址
        String url = baseInfo.getUrl();
        if(TextUtils.isEmpty(url)) {
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        //拿到tiketPresenter去加载数据
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
