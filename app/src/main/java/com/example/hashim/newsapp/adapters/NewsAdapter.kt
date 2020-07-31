/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    private val hDiffUtilCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private var hRecyclerCallBack: ((Article) -> Unit)? = null

    fun hSetRecyclerCallBack(listener: (Article) -> Unit) {
        hRecyclerCallBack = listener
    }

    val hAsyncListDiffer = AsyncListDiffer(this, hDiffUtilCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return hAsyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(articleViewHolder: ArticleViewHolder, position: Int) {
        val article = hAsyncListDiffer.currentList.get(position)
        articleViewHolder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text = article.source?.name
            tvDescription.text = article.description
            tvTitle.text = article.title
            tvPublishedAt.text = article.publishedAt
            setOnClickListener {
                hRecyclerCallBack?.let {
                    it(article)
                }
            }
        }
    }

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

}