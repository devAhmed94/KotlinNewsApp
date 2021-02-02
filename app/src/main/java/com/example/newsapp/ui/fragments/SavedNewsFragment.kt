package com.example.newsapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.SavedNewsFragmentBinding
import com.example.newsapp.model.Article
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 30/01/2021
 */
class SavedNewsFragment : Fragment() {
    lateinit var binding: SavedNewsFragmentBinding
    private lateinit var viewMode: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SavedNewsFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewMode = (activity as NewsActivity).viewModel
        setupRecycler()
        setupClickItemRecycler()
        getDataList()
        deleteItem()
    }

    private fun deleteItem() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val article = newsAdapter.differ.currentList[viewHolder.adapterPosition]
                viewMode.delete(article)

                Snackbar.make(binding.root, "delete the Item", Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO") {
                        viewMode.insert(article)
                    }

                }.show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(binding.rvSavedNews)
    }

    private fun getDataList() {
        viewMode.getAllSavedNews().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })
    }

    private fun setupClickItemRecycler() {
        newsAdapter.setItemClickListener(object : NewsAdapter.ItemArticleClickListener {
            override fun onItemArticleClickListener(article: Article) {
                findNavController().navigate(
                    SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(
                        article
                    )
                )
            }
        })
    }

    private fun setupRecycler() {
        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            adapter = newsAdapter
        }
    }
}