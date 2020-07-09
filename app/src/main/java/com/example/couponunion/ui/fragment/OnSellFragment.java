package com.example.couponunion.ui.fragment;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.model.domain.IBaseInfo;
import com.example.couponunion.model.domain.OnSellContent;
import com.example.couponunion.presenter.IOnSellPagePresenter;
import com.example.couponunion.ui.adapter.OnSellContentAdapter;
import com.example.couponunion.utils.PresenterManager;
import com.example.couponunion.utils.SizeUtils;
import com.example.couponunion.utils.TicketUtils;
import com.example.couponunion.utils.ToastUtils;
import com.example.couponunion.view.IOnSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellPageCallback,OnSellContentAdapter.OnSellPageItemClickListener{


    @BindView(R.id.on_sell_content_list)
    RecyclerView mOnSellContentList;

    @BindView(R.id.on_sell_refresh_layout)
    TwinklingRefreshLayout mOnSellRefreshLayout;

    private IOnSellPagePresenter mOnSellPagePresenter;
    private OnSellContentAdapter mContentAdapter;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //网格列表初始化
        mOnSellContentList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mContentAdapter = new OnSellContentAdapter();
        mOnSellContentList.setAdapter(mContentAdapter);
        //添加边距
        mOnSellContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dp2px(getContext(), 4);
                int leftAndRight = SizeUtils.dp2px(getContext(), 6);
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
            }
        });


        //下拉刷新控件
        mOnSellRefreshLayout.setEnableRefresh(false);
        mOnSellRefreshLayout.setEnableLoadmore(true);
        mOnSellRefreshLayout.setEnableOverScroll(true);
        mOnSellRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                //TODO 刷新
                mOnSellPagePresenter.loaderMore();
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();

        mOnSellRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loaderMore();
                }
            }
        });
        mContentAdapter.setOnSellPageItemClickListener(this);

    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getOnSellContent();
    }

    @Override
    protected void loadData() {
        super.loadData();
    }

    /**
     * 特惠内容数据回调 展示到UI
     *
     * @param content 特惠商品内容
     */
    @Override
    public void onContentLoaded(OnSellContent content) {
        setUpState(State.SUCCESS);
        mContentAdapter.setData(content);
    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        mOnSellRefreshLayout.finishLoadmore();
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showToast("加载了" + size + "个宝贝");
        //添加内容到适配器里
        mContentAdapter.onMoreLoaded(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        mOnSellRefreshLayout.finishLoadmore();
        ToastUtils.showToast("网络异常,请稍后重试.");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mOnSellRefreshLayout.finishLoadmore();
        ToastUtils.showToast("没有更多的内容...");
    }

    @Override
    public void onError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onSellItemClick(IBaseInfo data) {
        TicketUtils.toTicketPage(getContext(), data);
    }
}
