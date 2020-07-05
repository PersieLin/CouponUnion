package com.example.couponunion.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    private List<HomePagerContent.DataBean> dataList = new ArrayList<>();


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        //动态载入ImageView布局
        ImageView iv = new ImageView(context);
        //设置ImageView的宽高属性
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        //将图片资源导入
        String picUrl = UrlUtils.getCoverPath(dataList.get(position).getPict_url());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(picUrl).into(iv);
        //记得将动态导入的View add到container中
        container.addView(iv);
        return iv;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        dataList.clear();
        dataList.addAll(contents);
        //切记要通知
        notifyDataSetChanged();

    }
}
