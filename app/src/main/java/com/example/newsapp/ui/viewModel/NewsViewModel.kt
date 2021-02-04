package com.example.newsapp.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.Article
import com.example.newsapp.network.response.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 01/02/2021
 */
class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var pageBreaking = 1
    var breakingResponse: NewsResponse? = null
    var pageSearching = 1
    var searchingResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(country: String) {
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            val response = newsRepository.getBreakNews(country, pageBreaking)
            breakingNews.postValue(handleResponseBreaking(response))

        }

    }

    fun getSearchNews(searchQuery: String) {
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            val response = newsRepository.getSearchNews(searchQuery, pageSearching)
            searchNews.postValue(handleResponseSearching(response))

        }
    }

    private fun handleResponseBreaking(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                pageBreaking++
                if (breakingResponse == null) {
                    breakingResponse = it
                } else {
                    val oldRes = breakingResponse?.articles
                    val newRes = it.articles
                    oldRes?.addAll(newRes)
                }

                return Resource.Success(breakingResponse ?: it)
            }
        }
        return Resource.Error(null, response.message())
    }

    private fun handleResponseSearching(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {

            response.body()?.let {
                pageSearching++
                if (searchingResponse == null) {
                    searchingResponse = it
                } else {
                    val oldRes = searchingResponse?.articles
                    val newRes = it.articles
                    oldRes?.addAll(newRes)

                }
                return Resource.Success(searchingResponse ?: it)
            }
        }
        return Resource.Error(null, response.message())
    }

    fun insert(article: Article) {
        viewModelScope.launch {
            newsRepository.insert(article)
        }

    }

    fun delete(article: Article) {
        viewModelScope.launch {
            newsRepository.delete(article)
        }
    }


    fun getAllSavedNews() = newsRepository.getAllSavedNews()
}