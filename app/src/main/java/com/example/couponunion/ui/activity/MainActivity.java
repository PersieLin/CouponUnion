package com.example.couponunion.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseActivity;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.ui.adapter.HomeContentListAdapter;
import com.example.couponunion.ui.adapter.HomePagerAdapter;
import com.example.couponunion.ui.adapter.LooperPagerAdapter;
import com.example.couponunion.ui.fragment.HomeFragment;
import com.example.couponunion.ui.fragment.RedPacketFragment;
import com.example.couponunion.ui.fragment.SearchFragment;
import com.example.couponunion.ui.fragment.SelectedFragment;
import com.example.couponunion.utils.LogUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    //View绑定
    @BindView(R.id.main_navigation_bar)
    BottomNavigationView navBar;

    //Unbinder
    Unbinder unbinder;

    //Fragment全局变量
    private FragmentManager fm;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectedFragment;
    private RedPacketFragment mRedPacketFragment;
    private SearchFragment mSearchFragment;

    private BaseFragment lastOneFragment = null;


    @Override
    protected void initPresenter() {

    }

    /**
     * 初始化布局
     */
    protected void initView() {
        initFragments();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    /**
     * 初始化Fragment
     */
    private void initFragments() {
        fm = getSupportFragmentManager();
        mHomeFragment = new HomeFragment();
        mSelectedFragment = new SelectedFragment();
        mRedPacketFragment = new RedPacketFragment();
        mSearchFragment = new SearchFragment();
        //初始化切换fragment到首页
        switchFragment(mHomeFragment);
    }

    /**
     * 初始化监听器
     */
    protected void initEvent() {
        //导航栏按钮回调
        navBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    //切换到首页
                    switchFragment(mHomeFragment);
                } else if (item.getItemId() == R.id.selected) {
                    //切换到精选
                    switchFragment(mSelectedFragment);
                } else if (item.getItemId() == R.id.red_packet) {
                    //切换到特惠
                    switchFragment(mRedPacketFragment);
                } else if (item.getItemId() == R.id.search) {
                    //切换到搜索
                    switchFragment(mSearchFragment);
                }
                //返回True，表示此控件消费了这次点击事件
                return true;
            }
        });

    }



    /**
     * 切换Fragment
     */
    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        //当目标布局尚未被添加到事务中
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        }

        if (lastOneFragment != null) {
            transaction.hide(lastOneFragment);
        }
        transaction.show(targetFragment);
        lastOneFragment = targetFragment;
        transaction.commit();
    }
    @Override
    protected void release() {

    }

}
