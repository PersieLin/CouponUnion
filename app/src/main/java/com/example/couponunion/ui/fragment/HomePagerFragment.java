package com.example.couponunion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
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
import com.example.couponunion.presenter.ITicketPresenter;
import com.example.couponunion.presenter.impl.CategoryPagerPresenterImpl;
import com.example.couponunion.ui.activity.TicketActivity;
import com.example.couponunion.ui.adapter.HomeContentListAdapter;
import com.example.couponunion.ui.adapter.LooperPagerAdapter;
import com.example.couponunion.ui.custom.AutoLoopViewPager;
import com.example.couponunion.utils.PresenterManager;
import com.lcodecore.tkrefreshlayout.view.TbNestedScrollView;
import com.example.couponunion.utils.Constants;
import com.example.couponunion.utils.LogUtil;
import com.example.couponunion.utils.SizeUtil;
import com.example.couponunion.utils.ToastUtil;
import com.example.couponunion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LooperPagerAdapter.OnLooperItemClickListener, HomeContentListAdapter.OnHomePagerItemClickListener {


    private ICategoryPagerPresenter mPresenter;
    //当前页面对应的分类ID
    private int materialId;

    @BindView(R.id.home_page_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_refresh_layout)
    public TwinklingRefreshLayout homeRefreshLayout;

    @BindView(R.id.home_pager_title)
    public TextView homePagerTitleTv;

    //包裹looperPager和title和recView的父容器，用于获取RecyclerView所需要的height
    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_header)
    public LinearLayout homePagerHeader;

    @BindView(R.id.home_nested_scroller)
    public TbNestedScrollView homeNestedScroller;


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
        //下拉刷新初始化
        homeRefreshLayout.setEnableRefresh(false);
        homeRefreshLayout.setEnableLoadmore(true);

    }

    @Override
    protected void initListener() {
        //设置轮播图和列表的点击监听
        contentListAdapter.setItemClickListener(this);
        mLooperAdapter.setLooperItemClickListener(this);
        //轮播图监听器，用于监听图片index并切换指示器的位置
        looperPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperAdapter.getDataSize() == 0) {
                    return;
                }
                //伪循环中的position转换成正确的位置，并更新指示器的状态
                int targetPosition = position % mLooperAdapter.getDataSize();
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //加载更多
        homeRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mPresenter != null) {
                    mPresenter.loaderMore(materialId);
                }
            }
        });

        //监听父容器的Layout过程，并返回最终的MeasureHeight值，用于确定RecyclerView的height，从而避免在NestScrollView中直接生成大量的Holder消耗资源
        //在此处同时动态测量和设置需要被外部NestedScrollView的先消费的Height
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homePagerHeader == null) {
                    return;
                }
                int headerHeight = homePagerHeader.getMeasuredHeight();
//                LogUtil.d(HomePagerFragment.this, "homePagerHeader measureHeight -->" + headerHeight);
                homeNestedScroller.setHeaderHeight(headerHeight);
                ViewGroup.LayoutParams layoutParams = mContentList.getLayoutParams();
                int measureHeight = homePagerParent.getMeasuredHeight();
                //将RecyclerView的height设置为父容器测量后的Height
                layoutParams.height = measureHeight;
                mContentList.setLayoutParams(layoutParams);
//                LogUtil.d(HomePagerFragment.this, "homePageParent measureHeight -- > " + measureHeight);
                //当界面测量完毕后解绑监听器，避免重复执行代码
                if (measureHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    /**
     * 列表项目点击回调
     *
     * @param itemContent 对应产品的数据
     */
    @Override
    public void onHomePagerItemClick(HomePagerContent.DataBean itemContent) {
//        LogUtil.d(this, "onHomePagerItemClick ......" + itemContent.getTitle());
        handleItemClick(itemContent);

    }

    /**
     * 轮播图点击回调
     *
     * @param itemContent 对应产品的数据
     */
    @Override
    public void onLooperItemClick(HomePagerContent.DataBean itemContent) {
//        LogUtil.d(this, "onLooperItemClick ......" + itemContent.getTitle());
        handleItemClick(itemContent);
    }

    /**
     * 点击事件处理，跳转到淘口令页面
     * @param itemContent
     */
    private void handleItemClick(HomePagerContent.DataBean itemContent) {
        //TODO 获取淘口令数据并传递到淘口令页面
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(itemContent.getTitle(), itemContent.getClick_url(), itemContent.getPict_url());
        Intent intent = new Intent(getActivity(), TicketActivity.class);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        //当轮播图可见时执行轮播
        looperPager.startLoop();
        LogUtil.d(this, "onResume .....");
    }

    /**
     * 更新指示器的状态
     *
     * @param targetPosition 当前item对应的位置
     */
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            //取出指示器的各个子view，根据当前位置设置状态
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_looper_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_looper_point_normal);
            }
        }
    }

    @Override
    protected void loadData() {
        LogUtil.d(this, "loadData ......");
        Bundle bundle = getArguments();
        title = bundle.getString(Constants.KEY_HOME_CATEGORY_TITLE);
        materialId = bundle.getInt(Constants.KEY_HOME_CATEGORY_MATERIAL_ID);
        LogUtil.d(this, "title-->" + title);
        LogUtil.d(this, "materialId-->" + materialId);
        mPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
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
    public void onLoaderMore(List<HomePagerContent.DataBean> content) {
        contentListAdapter.addData(content);
        if (homeRefreshLayout != null) {
            homeRefreshLayout.finishLoadmore();
//            Toast.makeText(getContext(), "加载了" + content.size() + "条记录", Toast.LENGTH_SHORT).show();
            ToastUtil.showToast("加载了" + content.size() + "条记录");
        }

    }

    @Override
    public void onLoaderMoreError(int categoryId) {
        ToastUtil.showToast("网络异常，请稍后重试");
        if (homeRefreshLayout != null) {
            homeRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoaderMoreEmpty(int categoryId) {
        ToastUtil.showToast("没有更多商品");
        if (homeRefreshLayout != null) {
            homeRefreshLayout.finishLoadmore();
        }
    }

    /**
     * 轮播图数据回调UI接口
     *
     * @param contents
     */
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtil.d(this, "looperList size --> " + contents.size());
        mLooperAdapter.setData(contents);
        int contentSize = contents.size();
        //实现伪无限循环轮播(往返)，将初始化的页面位置设置为int maxvalue的中间值,并使其初始图的index为0
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetPosition = (Integer.MAX_VALUE / 2) - dx;
        looperPager.setCurrentItem(targetPosition);
        //TODO 为什么要清除？
        looperPointContainer.removeAllViews();
        //依次将轮播图指示器添加到容器中
        for (int i = 0; i < contentSize; i++) {
            View pointView = new View(getContext());
            //点的宽高尺寸
            int size = SizeUtil.dp2px(getContext(), 8);
            //将点的宽高尺寸设置为LinearLayoutParams
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtil.dp2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtil.dp2px(getContext(), 5);
            pointView.setLayoutParams(layoutParams);
            looperPointContainer.addView(pointView);
        }
        updateLooperIndicator(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        //当不可见时停止轮播
        looperPager.stopLoop();
        LogUtil.d(this, "onPause ......");
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
