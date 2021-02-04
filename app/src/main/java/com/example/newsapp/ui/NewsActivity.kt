package com.example.newsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.R
import com.example.newsapp.core.BaseApplication
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.db.ArticleDataBase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.ui.viewModel.NewsViewModel
import com.example.newsapp.ui.viewModel.NewsViewModelFactory

class NewsActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsBinding
    lateinit var navController: NavController
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, NewsViewModelFactory(application,NewsRepository(ArticleDataBase(this))))
                .get(NewsViewModel::class.java)

        var hostFragment =
            supportFragmentManager.findFragmentById(R.id.fHostContainer) as NavHostFragment
        navController = hostFragment.findNavController()

        with(binding) {
            bottomNavNews.setupWithNavController(navController)
        }
    }
}