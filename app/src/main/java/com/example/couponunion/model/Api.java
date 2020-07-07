package com.example.couponunion.model;

import com.example.couponunion.model.domain.Categories;
import com.example.couponunion.model.domain.HomePagerContent;
import com.example.couponunion.model.domain.TicketParams;
import com.example.couponunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicketResult(@Body TicketParams ticketParam);
}
