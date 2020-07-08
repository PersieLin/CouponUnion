package com.example.couponunion.ui.fragment;

import android.graphics.Rect;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.model.domain.IBaseInfo;
import com.example.couponunion.model.domain.SelecetdPageCategory;
import com.example.couponunion.model.domain.SelectedPageContent;
import com.example.couponunion.presenter.ISelectedPagePresenter;
import com.example.couponunion.utils.LogUtils;
import com.example.couponunion.utils.PresenterManager;
import com.example.couponunion.utils.SizeUtils;
import com.example.couponunion.utils.TicketUtils;
import com.example.couponunion.view.ISelectedPageCallback;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener, SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {


    private ISelectedPagePresenter mSelectedPagePresenter;

    @BindView(R.id.left_category_list)
    RecyclerView mLeftRecyclerView;

    @BindView(R.id.right_content_list)
    RecyclerView mRightRecyclerView;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mContentAdapter;

    @Override
    protected int getRootViewId() {
        return R.layout.fragment_selected;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //左侧分类 标题
        mLeftRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        mLeftRecyclerView.setAdapter(mLeftAdapter);

        //右侧内容列表
        mRightRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentAdapter = new SelectedPageContentAdapter();
        mRightRecyclerView.setAdapter(mContentAdapter);
        mRightRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int topAndBottom = SizeUtils.dp2px(getContext(),4);
                int leftAndRight = SizeUtils.dp2px(getContext(),6);
                outRect.left = leftAndRight;
                outRect.right = leftAndRight;
                outRect.top = topAndBottom;
                outRect.bottom = topAndBottom;
            }
        });
        barTitleTv.setText(getResources().getText(R.string.text_selected_title));
    }

    @Override
    protected void initListener() {
        super.initListener();
        mLeftAdapter.setOnLeftItemClickListener(this);
        mContentAdapter.setContentItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        super.initPresenter();
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }

    @Override
    protected void loadData() {
        super.loadData();
    }


    @Override
    public void onCategoriesLoaded(SelecetdPageCategory categories) {
        setUpState(State.SUCCESS);
        if (mLeftAdapter != null) {
            mLeftAdapter.setData(categories);
        }

    }

    @Override
    public void onContentLoaded(SelectedPageContent content) {
        mContentAdapter.setData(content);
        mRightRecyclerView.scrollToPosition(0);
        LogUtils.d(this, "onContentLoaded ==>" + content.toString());
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

    }

    @Override
    protected void release() {
        super.release();
        mSelectedPagePresenter.unregisterViewCallback(this);
    }


    @Override
    public void onLeftItemClick(SelecetdPageCategory.DataBean category) {
        //左侧的内容被点击了
        mSelectedPagePresenter.getContentByCategory(category);
        LogUtils.d(this, "current selected item -- > " + category.getFavorites_title());

    }

    @Override
    public void onContentItemClick(IBaseInfo item) {
        TicketUtils.toTicketPage(getContext(),item);
    }

    @Override
    protected void onRetryClick(View view) {
        mSelectedPagePresenter.reloadContent();

    }
}


