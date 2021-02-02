package com.example.newsapp.network

import com.example.newsapp.network.response.NewsResponse
import com.example.newsapp.util.Constants.Companion.MY_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
interface ApiServices {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        country:String="us" ,
        @Query("page")
        pageNumber:Int =1 ,
        @Query("apiKey")
        apiKey:String = MY_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun getSearchNews(
        @Query("q")
        searchQuery:String ,
        @Query("page")
        pageNumber:Int =1 ,
        @Query("apiKey")
        apiKey:String = MY_KEY
    ):Response<NewsResponse>
}