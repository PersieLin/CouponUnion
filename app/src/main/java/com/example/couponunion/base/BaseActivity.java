package com.example.couponunion.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        unbinder = ButterKnife.bind(this);
        initView();
        initPresenter();
        initEvent();
    }

    /**
     * 初始化Presenter
     */
    protected abstract void initPresenter();


    /**
     * 初始化视图内容
     */
    protected abstract void initView();

    /**
     * 设置布局ID
     * @return 布局ID
     */
    protected abstract int getLayoutResId();

    /**
     * 初始化监听器
     */
    protected void initEvent() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑ButterKnife
        if (unbinder != null) {
            unbinder.unbind();
        }
        release();
    }

    protected abstract void release();
}
