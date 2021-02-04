package com.example.newsapp.ui.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.newsapp.core.BaseApplication
import com.example.newsapp.model.Article
import com.example.newsapp.network.response.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

import java.lang.Boolean.TYPE


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 01/02/2021
 */
class NewsViewModel(
    val app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

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
            safeBreakNews(country)
        }

    }

    fun getSearchNews(searchQuery: String) {
        viewModelScope.launch {
            safeSearchNews(searchQuery)

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

    private suspend fun safeBreakNews(country: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (checkInternetConnection()) {
                val response = newsRepository.getBreakNews(country, pageBreaking)
                breakingNews.postValue(handleResponseBreaking(response))
            } else {
                breakingNews.postValue(Resource.Error(null, "no internet Connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error(null, "Network Failure"))
                else -> breakingNews.postValue(Resource.Error(null, "conversion Error"))
            }
        }
    }

    private suspend fun safeSearchNews(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (checkInternetConnection()) {
                val response = newsRepository.getSearchNews(searchQuery, pageSearching)
                searchNews.postValue(handleResponseSearching(response))
            } else {
                searchNews.postValue(Resource.Error(null, "no internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error(null, "network Failure"))
                else -> breakingNews.postValue(Resource.Error(null, "conversion Error"))
            }
        }
    }


    fun checkInternetConnection(): Boolean {
        val connectionManager =
            getApplication<BaseApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectionManager.activeNetwork ?: return false
            val capabilities =
                connectionManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectionManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
        return false
    }
}