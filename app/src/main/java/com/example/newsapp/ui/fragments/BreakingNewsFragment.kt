package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.BreakingNewsFragmentBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.viewModel.NewsViewModel
import com.example.newsapp.util.Resource


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
class BreakingNewsFragment : Fragment() {
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    lateinit var binding: BreakingNewsFragmentBinding
    private lateinit var viewMode: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BreakingNewsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMode = (activity as NewsActivity).viewModel
        setupRecyclerView()
        handleClickItemRecycler()
        getDataFromViewModel()


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
                    viewMode.getBreakingNews("us")
                    isScrolling = false
                }


            }
        }


    }

    private fun getDataFromViewModel() {
        viewMode.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hiddenProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPages = it.totalResults / 20 + 2
                        isLastPage = viewMode.pageBreaking == totalPages
                        if (isLastPage){
                            binding.rvBreakingNews.setPadding(0,0,0,0)
                        }
                    }
                    //newsAdapter.differ.submitList(response.data?.articles)
                }
                is Resource.Error -> {
                    hiddenProgressBar()
                    response.message.let {
                        Log.d("TAG", "onViewCreated: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

    }

    private fun handleClickItemRecycler() {
        newsAdapter.setItemClickListener(object : NewsAdapter.ItemArticleClickListener {
            override fun onItemArticleClickListener(article: Article) {
                findNavController().navigate(
                    BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                        article
                    )
                )
            }
        })
    }

    fun setupRecyclerView() {
        settingScrollListener()
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
            addOnScrollListener(scrollListener)
        }

    }

    fun hiddenProgressBar() {
        binding.pbBreakingNews.visibility = View.GONE
        isLoading = false
    }

    fun showProgressBar() {
        binding.pbBreakingNews.visibility = View.VISIBLE
        isLoading = true
    }
}