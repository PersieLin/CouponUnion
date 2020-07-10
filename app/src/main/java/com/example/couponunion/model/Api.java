package com.example.couponunion.model;

import com.example.couponunion.model.domain.Categories;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.model.domain.OnSellContent;
import com.example.couponunion.model.domain.SearchRecommend;
import com.example.couponunion.model.domain.SearchResult;
import com.example.couponunion.model.domain.SelecetdPageCategory;
import com.example.couponunion.model.domain.SelectedPageContent;
import com.example.couponunion.model.domain.TicketParams;
import com.example.couponunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicketResult(@Body TicketParams ticketParam);

    @GET("recommend/categories")
    Call<SelecetdPageCategory> getSelectedPageCategories();

    @GET
    Call<SelectedPageContent> getSelectedPageContents(@Url String url);

    @GET
    Call<OnSellContent> getOnSellPageContent(@Url String url);


    //获取热门搜索词接口
    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    /**
     * 获取搜索结果
     * @param page 页码
     * @param keyword 关键词
     * @return  搜索结果
     */
    @GET("search")
    Call<SearchResult> getSearchResult(@Query("page")int page, @Query("keyword")String keyword);

}

