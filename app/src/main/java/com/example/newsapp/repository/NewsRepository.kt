package com.example.newsapp.repository

import androidx.room.Delete
import com.example.newsapp.db.ArticleDataBase
import com.example.newsapp.model.Article
import com.example.newsapp.network.RetrofitInstance
import com.example.newsapp.network.response.NewsResponse
import retrofit2.Response


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 01/02/2021
 */
class NewsRepository(val db: ArticleDataBase) {
   suspend fun getBreakNews(country:String,page:Int):Response<NewsResponse>{
        return RetrofitInstance.api.getBreakingNews(country, page)
   }

    suspend fun getSearchNews(searchQuery :String,page:Int): Response<NewsResponse> {
        return RetrofitInstance.api.getSearchNews(searchQuery,page)
    }
    suspend fun insert(article:Article){
        db.getArticleDao().insert(article)
    }
    suspend fun delete(article: Article){
         db.getArticleDao().delete(article)
    }
    fun getAllSavedNews() =db.getArticleDao().getAllNews()
}
