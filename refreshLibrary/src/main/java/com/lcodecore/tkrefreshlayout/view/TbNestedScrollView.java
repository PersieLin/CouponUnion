package com.lcodecore.tkrefreshlayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;

public class TbNestedScrollView extends NestedScrollView {

    private int mHeaderHeight = 0;
    private RecyclerView mRecyclerView;
    private int originScroll = 0;

    public TbNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int headerHeight) {
        mHeaderHeight = headerHeight;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) target;
        }
        //当消费的滑动距离小于需要滑动掉的Header部分的值时，继续消费滑动事件
        if (originScroll < mHeaderHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type);
    }

    //在此方法中设置外部ScrollView已消费的scroll距离
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        //
        this.originScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * 判断子类是否已经滑动到底部
     * @return
     */
    public boolean isToBottom(){
        //判断垂直方向是否能够滑动
        if (mRecyclerView != null) {
            //判断子View是否已滑到底部
            boolean isBottom = !mRecyclerView.canScrollVertically(1);
//            Log.d("TbNestedScrollerView","mRecyclerView isBottom == > " + isBottom);
            return isBottom;
        } else {
            return false;
        }
    }
}
