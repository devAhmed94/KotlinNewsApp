package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemArticleBinding
import com.example.newsapp.model.Article


/**
 * Ahmed Ali Ebaid
 * ahmedali26002844@gmail.com
 * 01/02/2021
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallBack = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

     var differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {

        return NewsViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = differ.currentList[position]
        holder.binding.apply {
            Glide.with(this.root).load(currentArticle.urlToImage).into(ivArticleImage)
            tvTitle.text = currentArticle.title
            tvDescription.text = currentArticle.description
            tvSource.text = currentArticle.source.name
            tvPublishedAt.text = currentArticle.publishedAt
            root.setOnClickListener {
                clickListener?.onItemArticleClickListener(currentArticle)
               // clickListener?.let { it.onItemArticleClickListener(currentArticle) }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var clickListener: ItemArticleClickListener? = null

    fun setItemClickListener(listener: ItemArticleClickListener) {
        clickListener =listener
    }


    interface ItemArticleClickListener {
        fun onItemArticleClickListener(article: Article)
    }

}