package com.example.couponunion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.couponunion.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private FrameLayout baseContainer;

    //重试按钮回调
    @OnClick(R.id.network_error_tips)
    public void retry(View view) {
        onRetryClick(view);
    }

    protected enum State {
        EMPTY, SUCCESS, ERROR, LOADING, NONE
    }

    private State currentState = State.NONE;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater, container, savedInstanceState);
        baseContainer = rootView.findViewById(R.id.base_fragment_container);
        loadStateView(inflater, container);
        //初始化ButterKnife
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;

    }

    /**
     * 加载各种状态的View并添加到根布局中
     *
     */
    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        mSuccessView = loadSuccessView(inflater, container);
        mLoadingView = inflater.inflate(R.layout.fragment_loading, container,false);
        mErrorView = inflater.inflate(R.layout.fragment_error, container,false);
        mEmptyView = inflater.inflate(R.layout.fragment_empty, container,false);
        //将各个状态的View添加到容器中
        baseContainer.addView(mSuccessView);
        baseContainer.addView(mLoadingView);
        baseContainer.addView(mErrorView);
        baseContainer.addView(mEmptyView);
        //首次加载显示空布局
        setUpState(State.NONE);

    }

    private View loadSuccessView(LayoutInflater inflater, ViewGroup container) {
        int resId = getRootViewId();
        return inflater.inflate(resId, container,false);
    }


    /**
     * 子类继承父类时要实现这个方法传入正常状态的View布局
     * @return
     */
    protected abstract int getRootViewId();

    /**
     * 设置布局状态
     * @param state
     */
    protected void setUpState(State state) {
        currentState = state;
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    /**
     * 初始化各种控件
     */
    protected void initView(View rootView) {

    }

    /**
     * 初始化监听器
     */
    protected void initListener() {

    }
    /**
     * 初始化Presenter
     */
    protected void initPresenter() {

    }

    /**
     * 加载数据
     */
    protected void loadData() {

    }

    /**
     * 网络出错重试回调
     * @param view
     */
    protected void onRetryClick(View view) {

    }


    /**
     * 释放资源和取消回调
     */
    protected void release() {
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 加载根布局
     * @return
     */
    protected View loadRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_container, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }
}
