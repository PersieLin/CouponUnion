package com.example.couponunion.ui.fragment;

import android.view.View;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.model.domain.SearchResult;
import com.example.couponunion.presenter.ISearchPresenter;
import com.example.couponunion.utils.PresenterManager;
import com.example.couponunion.view.ISearchPageCallback;

import java.util.List;

public class SearchFragment extends BaseFragment implements ISearchPageCallback {

    private ISearchPresenter mSearchPresenter;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.getRecommendedWords();
        mSearchPresenter.registerViewCallback(this);
        mSearchPresenter.doSearch("毛衣");
        mSearchPresenter.loaderMore();
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    @Override
    public void onSearchSuccess(SearchResult result) {

    }

    @Override
    public void onLoaderMoreSuccess(SearchResult result) {

    }

    @Override
    public void onLoaderMoreError() {

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoadedRecommended(List<String> recommendWordList) {

    }

    @Override
    public void onLoadedHistories(List<String> historiesList) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }
}
