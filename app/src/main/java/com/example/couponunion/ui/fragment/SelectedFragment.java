package com.example.couponunion.ui.fragment;

import android.view.View;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;

public class SelectedFragment extends BaseFragment {


    @Override
    protected int getRootViewId() {
        return R.layout.fragment_selected;
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
    }

    @Override
    protected void loadData() {
        super.loadData();
    }
}


