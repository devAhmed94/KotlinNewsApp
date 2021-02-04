package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.SearchNewsFragmentBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.viewModel.NewsViewModel
import com.example.newsapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */

class SearchNewsFragment :Fragment() {
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    lateinit var binding :SearchNewsFragmentBinding
    private lateinit var viewMode:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchNewsFragmentBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMode =(activity as NewsActivity).viewModel
        setupRecycler()
        setItemClickRecycler()
        setupEditSearching()
        setupViewModel()

    }

    private fun setItemClickRecycler() {
        newsAdapter.setItemClickListener(object : NewsAdapter.ItemArticleClickListener {
            override fun onItemArticleClickListener(article: Article) {
                findNavController().navigate(
                   SearchNewsFragmentDirections.actionSearchNewsFragmentToArticleFragment(
                        article
                    )
                )
            }
        })
    }

    private fun setupRecycler(){
        settingScrollListener()
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager =LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter =newsAdapter
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }

    }
    private fun setupViewModel(){
        viewMode.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hiddenProgressBar()
                    it.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / 20 + 2
                        isLastPage = viewMode.pageSearching== totalPages
                        if (isLastPage){
                            binding.rvSearchNews.setPadding(0,0,0,0)
                        }
                    }

                }
                is Resource.Error -> {
                    hiddenProgressBar()
                    Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "onViewCreated:${it.message} ")
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun setupEditSearching(){
        val job:Job?=null

        binding.etSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                job?.cancel()
                MainScope().launch {
                    delay(500)
                    if (s.toString().isNotEmpty()){
                        viewMode.getSearchNews(s.toString())
                    }
                }
            }
        })
    }
    private fun hiddenProgressBar(){
        binding.pbSearchNews.visibility =View.GONE
        isLoading =false
    }
    private fun showProgressBar(){
        binding.pbSearchNews.visibility =View.VISIBLE
        isLoading=true
    }
    private fun settingScrollListener() {
        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManger = recyclerView.layoutManager as LinearLayoutManager

                val firstVisibleItemPosition = linearLayoutManger.findFirstVisibleItemPosition()
                val visibleCount = linearLayoutManger.childCount
                val totalItem = linearLayoutManger.itemCount

                val isNotLoadNotLastPage = !isLoading && !isLastPage
                val isAtLastItem = firstVisibleItemPosition + visibleCount >= totalItem
                val isNotBeginning = firstVisibleItemPosition >= 0
                val totalMoreThanVisibleItems = totalItem >= 20
                val shouldPaginate = isNotLoadNotLastPage && isAtLastItem && isNotBeginning
                        && totalMoreThanVisibleItems && isScrolling
                if (shouldPaginate) {
                    viewMode.getSearchNews(binding.etSearch.text.toString())
                    isScrolling = false
                }


            }
        }


    }
}