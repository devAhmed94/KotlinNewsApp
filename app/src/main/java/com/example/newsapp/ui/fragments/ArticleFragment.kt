package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsapp.databinding.ArticleFragmentBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
class ArticleFragment :Fragment() {
    lateinit var binding :ArticleFragmentBinding
    private lateinit var viewMode :NewsViewModel
    private val args:ArticleFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ArticleFragmentBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMode =(activity as NewsActivity).viewModel
        val article = args.article
        binding.wvArticle.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        binding.fbArticle.setOnClickListener{
            viewMode.insert(article)
            Snackbar.make(it,"successful insert Item",Snackbar.LENGTH_SHORT).show()
        }


    }
}