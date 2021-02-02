package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var binding: BreakingNewsFragmentBinding
    private lateinit var viewMode: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
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
        newsAdapter.setItemClickListener(object : NewsAdapter.ItemArticleClickListener {
            override fun onItemArticleClickListener(article: Article) {
                findNavController().navigate(
                    BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                        article
                    )
                )
            }
        })
        viewMode.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hiddenProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
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

    fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }

    }

    fun hiddenProgressBar() {
        binding.pbBreakingNews.visibility = View.GONE
    }

    fun showProgressBar() {
        binding.pbBreakingNews.visibility = View.VISIBLE
    }
}