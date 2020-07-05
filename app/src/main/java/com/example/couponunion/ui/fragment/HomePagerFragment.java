package com.example.couponunion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.model.domain.Categories;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.presenter.ICategoryPagerPresenter;
import com.example.couponunion.presenter.impl.CategoryPagerPresenterImpl;
import com.example.couponunion.ui.adapter.HomeContentListAdapter;
import com.example.couponunion.ui.adapter.LooperPagerAdapter;
import com.example.couponunion.utils.Constants;
import com.example.couponunion.utils.LogUtil;
import com.example.couponunion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {


    private ICategoryPagerPresenter mPresenter;
    //当前页面对应的分类ID
    private int materialId;

    @BindView(R.id.home_page_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public ViewPager looperPager;


    @BindView(R.id.home_pager_title)
    public TextView homePagerTitleTv;


    private HomeContentListAdapter contentListAdapter;
    private LooperPagerAdapter mLooperAdapter;
    private String title;

    //根据传入的标题和id来新建对应标题的fragment
    public static HomePagerFragment newInstance(Categories.DataBean dataBean) {
        HomePagerFragment mFragment = new HomePagerFragment();
        //将传入的标题和ID等限定参数利用bundle保存到fragment实例中，在加载数据时取出
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_CATEGORY_TITLE, dataBean.getTitle());
        bundle.putInt(Constants.KEY_HOME_CATEGORY_MATERIAL_ID, dataBean.getId());
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //初始化内容列表的适配器
        contentListAdapter = new HomeContentListAdapter();
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.setAdapter(contentListAdapter);
        //设置间距
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });

        //轮播图设置
        mLooperAdapter = new LooperPagerAdapter();
        looperPager.setAdapter(mLooperAdapter);

    }

    @Override
    protected void loadData() {
        LogUtil.d(this, "loadData ......");
        Bundle bundle = getArguments();
        title = bundle.getString(Constants.KEY_HOME_CATEGORY_TITLE);
        materialId = bundle.getInt(Constants.KEY_HOME_CATEGORY_MATERIAL_ID);
        LogUtil.d(this, "title-->" + title);
        LogUtil.d(this, "materialId-->" + materialId);
        mPresenter = CategoryPagerPresenterImpl.getInstance();
        mPresenter.registerViewCallback(this);
        mPresenter.getContentByCategoryId(materialId);
    }

    @Override
    protected void release() {
        super.release();
        mPresenter.unregisterViewCallback(this);
    }


    @Override
    public void onCategoryContentLoaded(List<HomePagerContent.DataBean> contents) {
        setUpState(State.SUCCESS);
        //将数据设置到适配器中去,让适配器将数据展示到UI;
        homePagerTitleTv.setText(title);
        contentListAdapter.setData(contents);
    }

    @Override
    public void onLoaderMore(HomePagerContent.DataBean content) {

    }

    @Override
    public void onLoaderMoreError(int categoryId) {

    }

    @Override
    public void onLoaderMoreEmpty(int categoryId) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtil.d(this, "looperList size --> " + contents.size());
        mLooperAdapter.setData(contents);
    }

    @Override
    public int getCategoryId() {
        return materialId;
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
