package com.example.couponunion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.utils.LogUtil;
import com.example.couponunion.utils.ToastUtil;
import com.example.couponunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> dataList = new ArrayList<>();
    private OnLooperItemClickListener itemClickListener;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //实现伪无限循环滚动，vg的position需要求模来恢复为数据源的对应位置
        int realPosition = position % dataList.size();
        Context context = container.getContext();
        //动态载入ImageView布局
        ImageView iv = new ImageView(context);
        //设置ImageView的宽高属性
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        //将图片资源导入
        int measuredWidth = container.getMeasuredWidth();
        int measuredHeight = container.getMeasuredHeight();
        int size = Math.max(measuredHeight, measuredWidth) / 2;
        String picUrl = UrlUtils.getCoverPath(dataList.get(realPosition).getPict_url(), size);
//        LogUtil.d(LooperPagerAdapter.this, "picUrl -- >" + picUrl);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(picUrl).into(iv);
        //记得将动态导入的View add到container中
        container.addView(iv);
        //设置轮播图点击监听
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onLooperItemClick(dataList.get(realPosition));
                }
            }
        });
        return iv;
    }

    public void setLooperItemClickListener(OnLooperItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public int getCount() {
        /**
         * 将Viewpager的count设置为Integer的最大值，实现伪无限循环。
         * 缺点：会产生太多实例
         */
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public int getDataSize() {
        return dataList.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        dataList.clear();
        dataList.addAll(contents);
        //切记要通知
        notifyDataSetChanged();

    }


    public interface OnLooperItemClickListener {
        void onLooperItemClick(HomePagerContent.DataBean dataBean);
    }
}
