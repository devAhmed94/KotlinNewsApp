package com.example.newsapp.network.response

import com.example.newsapp.model.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)