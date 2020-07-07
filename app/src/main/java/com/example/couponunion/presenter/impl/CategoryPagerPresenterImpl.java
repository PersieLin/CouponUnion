package com.example.couponunion.presenter.impl;

import com.example.couponunion.model.Api;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.presenter.ICategoryPagerPresenter;
import com.example.couponunion.utils.LogUtil;
import com.example.couponunion.utils.RetrofitManager;
import com.example.couponunion.utils.UrlUtils;
import com.example.couponunion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    //用于存储对应分类页面的页码
    private Map<Integer, Integer> pageInfo = new HashMap<>();
    private static final int DEFAULT_PAGE = 1;

    //因为不同的分类页面对应不同的fragment，所以要用一个ArrayList来收集对应的ViewCallback
    private List<ICategoryPagerCallback> callbackList = new ArrayList<>();

    //避免每个分类的页面都新建一个Presenter浪费资源，使用单例模式
    private static CategoryPagerPresenterImpl mInstance;
    private int currentPage;

    private CategoryPagerPresenterImpl(){}
    public static CategoryPagerPresenterImpl getInstance() {
        if (mInstance == null) {
            mInstance = new CategoryPagerPresenterImpl();
        }
        return mInstance;
    }


    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbackList) {
            if (categoryId == callback.getCategoryId()) {
                callback.onLoading();
            }
        }

        //获取分类下加载到的页码,并根据分类和页码构建Url来访问数据接口
        Integer targetPage = pageInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pageInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtil.d(CategoryPagerPresenterImpl.this, "responseCode -->" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //正常返回数据
                    HomePagerContent body = response.body();
                    handleHomePageContentResult(body, categoryId);
                } else {
                    //数据请求错误
                    handleHomepageError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtil.e(CategoryPagerPresenterImpl.this, t.toString());
                handleHomepageError(categoryId);
            }
        });

    }

    /**
     * 获取对应分类下的页码数据的逻辑
     *
     * @param categoryId
     * @param targetPage
     * @return
     */
    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        String url = UrlUtils.createUrl(categoryId, targetPage);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePagerContent(url);
    }

    /**
     * 处理请求分类页面数据失败的方法
     *
     * @param categoryId
     */
    private void handleHomepageError(int categoryId) {
        for (ICategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {
                callback.onEmpty();
            }
        }
    }

    /**
     * 处理返回的分类数据
     *
     * @param content
     * @param categoryId
     */
    private void handleHomePageContentResult(HomePagerContent content, int categoryId) {
        //将数据回调给UI接口
        //因为不同的分类页面对应不同的fragment，所以要用一个ArrayList来收集对应的ViewCallback
        List<HomePagerContent.DataBean> data = content.getData();
        for (ICategoryPagerCallback callback : callbackList) {
            if (content == null || content.getData().size() == 0) {
                //在处理逻辑的时候判断是否对应分类下的Fragment，是则回调否则不进行操作
                if (callback.getCategoryId() == categoryId) {
                    callback.onEmpty();
                }
            } else {
                if (callback.getCategoryId() == categoryId) {
                    List<HomePagerContent.DataBean> looperList = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperList);
                    callback.onCategoryContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //加载更多
        currentPage = pageInfo.get(categoryId);
        currentPage++;
        Call<HomePagerContent> task = createTask(categoryId, currentPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    handleLoaderResult(categoryId, result);
                } else {
                    handleLoaderError(categoryId, currentPage);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtil.d(CategoryPagerPresenterImpl.this, t.toString());
                handleLoaderError(categoryId, currentPage);
            }
        });


    }

    /**
     * 处理加载更多失败
     *
     * @param categoryId
     */
    private void handleLoaderError(int categoryId, int currentPage) {
        currentPage--;
        pageInfo.put(categoryId, currentPage);
        for (ICategoryPagerCallback callback : callbackList) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError(categoryId);
            }
        }
    }

    /**
     * 处理加载更多的数据
     *
     * @param categoryId
     * @param result
     */
    private void handleLoaderResult(int categoryId, HomePagerContent result) {
        for (ICategoryPagerCallback callback : callbackList) {
            if (categoryId == callback.getCategoryId()) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoaderMoreEmpty(categoryId);
                } else {
                    callback.onLoaderMore(result.getData());
                }
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        //当callbackList中不包含对应callback时，保存
        if (!callbackList.contains(callback)) {
            callbackList.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbackList.remove(callback);
    }
}
