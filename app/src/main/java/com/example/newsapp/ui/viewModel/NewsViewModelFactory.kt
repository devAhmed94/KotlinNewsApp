package com.example.newsapp.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsapp.repository.NewsRepository


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 01/02/2021
 */
class NewsViewModelFactory (val repository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(repository) as T
    }
}