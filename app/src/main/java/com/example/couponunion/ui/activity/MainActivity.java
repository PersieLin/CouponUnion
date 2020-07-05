package com.example.couponunion.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.couponunion.R;
import com.example.couponunion.base.BaseFragment;
import com.example.couponunion.ui.fragment.HomeFragment;
import com.example.couponunion.ui.fragment.RedPacketFragment;
import com.example.couponunion.ui.fragment.SearchFragment;
import com.example.couponunion.ui.fragment.SelectedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        initView();
        initFragments();
        initListener();
    }

    /**
     * 初始化布局
     */
    private void initView() {

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
    private void initListener() {
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
    private void switchFragment(BaseFragment nextFragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_page_container, nextFragment);
        transaction.commit();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑ButterKnife
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
