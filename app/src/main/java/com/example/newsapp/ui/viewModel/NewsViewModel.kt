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
    var pageSearching = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(country: String) {
        viewModelScope.launch {
            breakingNews.postValue(Resource.Loading())
            var response = newsRepository.getBreakNews(country, pageBreaking)
            breakingNews.postValue(handleResponseBreaking(response))

        }

    }

    fun getSearchNews(searchQuery: String) {
        viewModelScope.launch {
            searchNews.postValue(Resource.Loading())
            var response = newsRepository.getSearchNews(searchQuery, pageSearching)
            searchNews.postValue(handleResponseSearching(response))

        }
    }

    private fun handleResponseBreaking(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(null, response.message())
    }

    private fun handleResponseSearching(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
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