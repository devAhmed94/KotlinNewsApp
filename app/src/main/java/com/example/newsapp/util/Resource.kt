package com.example.newsapp.util


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 01/02/2021
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}