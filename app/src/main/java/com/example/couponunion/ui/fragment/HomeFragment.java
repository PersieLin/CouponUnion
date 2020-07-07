package com.example.couponunion.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.model.domain.Categories;
import com.example.couponunion.presenter.IHomePresenter;
import com.example.couponunion.presenter.impl.HomePresenterImpl;
import com.example.couponunion.ui.adapter.HomePagerAdapter;
import com.example.couponunion.utils.PresenterManager;
import com.example.couponunion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    TabLayout tabLayout;

    @BindView(R.id.home_pager)
    ViewPager homePager;

    private IHomePresenter homePresenter;

    private FragmentManager mFm;

    private HomePagerAdapter homePagerAdapter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_home_container, container, false);
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mFm = getChildFragmentManager();
        //初始化控件标题和适配器
        homePagerAdapter = new HomePagerAdapter(mFm);
        tabLayout.setupWithViewPager(homePager);
        homePager.setAdapter(homePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        homePresenter = PresenterManager.getInstance().getHomePresenter();
        //注册回调
        homePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        onLoading();
        //从homePresenter中获取数据并将数据回调给View层显示
        homePresenter.getCategories();
    }

    @Override
    protected void onRetryClick(View view) {
        //点击重试按钮后重新获取数据
        homePresenter.getCategories();
    }

    @Override
    protected void release() {
        super.release();
        //取消注册
        homePresenter.unregisterViewCallback(this);
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        //将回调的数据加载到UI上
        if (homePagerAdapter != null)
            setUpState(State.SUCCESS);
            homePagerAdapter.setCategories(categories);
    }



    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }
}
