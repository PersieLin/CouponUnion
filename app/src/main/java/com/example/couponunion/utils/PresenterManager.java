package com.example.couponunion.utils;

import com.example.couponunion.presenter.ICategoryPagerPresenter;
import com.example.couponunion.presenter.IHomePresenter;
import com.example.couponunion.presenter.IOnSellPagePresenter;
import com.example.couponunion.presenter.ISearchPresenter;
import com.example.couponunion.presenter.ISelectedPagePresenter;
import com.example.couponunion.presenter.ITicketPresenter;
import com.example.couponunion.presenter.impl.CategoryPagerPresenterImpl;
import com.example.couponunion.presenter.impl.HomePresenterImpl;
import com.example.couponunion.presenter.impl.OnSellPagePresenterImpl;
import com.example.couponunion.presenter.impl.SearchPresenter;
import com.example.couponunion.presenter.impl.SelectedPagePresenterImpl;
import com.example.couponunion.presenter.impl.TicketPresenterImpl;

/**
 * Presenter管理者，单例方式获取Presenter
 */
public class PresenterManager {
    private static PresenterManager mInstance = null;
    private IHomePresenter homePresenter;
    private ICategoryPagerPresenter categoryPagerPresenter;
    private ITicketPresenter ticketPresenter;
    private ISelectedPagePresenter selectedPagePresenter;
    private IOnSellPagePresenter onSellPagePresenter;
    private ISearchPresenter searchPresenter;



    private PresenterManager() {
        categoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        homePresenter = new HomePresenterImpl();
        ticketPresenter = new TicketPresenterImpl();
        selectedPagePresenter = new SelectedPagePresenterImpl();
        onSellPagePresenter = new OnSellPagePresenterImpl();
        searchPresenter = new SearchPresenter();

    }

    public static PresenterManager getInstance() {
        if (mInstance == null) {
            mInstance = new PresenterManager();
        }
        return mInstance;
    }


    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }


    public ICategoryPagerPresenter getCategoryPagerPresenter() {
        return categoryPagerPresenter;
    }


    public ITicketPresenter getTicketPresenter() {
        return ticketPresenter;
    }


    public ISelectedPagePresenter getSelectedPagePresenter() {
        return selectedPagePresenter;
    }

    public IOnSellPagePresenter getOnSellPagePresenter() {
        return onSellPagePresenter;
    }

    public ISearchPresenter getSearchPresenter() {
        return searchPresenter;
    }
}
