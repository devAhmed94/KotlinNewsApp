package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var binding :SearchNewsFragmentBinding
    private lateinit var viewMode:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
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
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            layoutManager =LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
            setHasFixedSize(true)
            adapter =newsAdapter
        }

    }
    private fun setupViewModel(){
        viewMode.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hiddenProgressBar()
                    newsAdapter.differ.submitList(it.data?.articles)
                }
                is Resource.Error -> {
                    hiddenProgressBar()
                    Log.d("TAG", "onViewCreated:${it.message} ")
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }
    private fun setupEditSearching(){
        var job:Job?=null

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
    }
    private fun showProgressBar(){
        binding.pbSearchNews.visibility =View.VISIBLE
    }
}