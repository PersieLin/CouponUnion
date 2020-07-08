package com.example.couponunion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.couponunion.model.domain.Categories;
import com.example.couponunion.ui.fragment.HomePagerFragment;
import com.example.couponunion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Categories.DataBean> categoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    /**
     * 设置当前页面的标题
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (categoryList != null)
            return categoryList.get(position).getTitle();
        else
            LogUtils.e(this, "categoryList is empty!");
        return "";
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Categories.DataBean dataBean = categoryList.get(position);
        return HomePagerFragment.newInstance(dataBean);
    }

    @Override
    public int getCount() {
        return categoryList != null ? categoryList.size() : 0;
    }


    public void setCategories(Categories categories) {
        //先清空原有的数组
        this.categoryList.clear();
        categoryList.addAll(categories.getData());
        //设置完数据后通知刷新
        notifyDataSetChanged();
    }
}
