/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.repository

import androidx.lifecycle.LiveData
import com.example.hashim.newsapp.models.Article
import com.example.hashim.newsapp.models.NewsResponse
import com.example.hashim.newsapp.network.RetrofitInstance
import com.example.hashim.newsapp.roomdb.ArticleDatabase
import retrofit2.Response

class NewsRepository(
    private val hDatabase: ArticleDatabase
) {
    suspend fun hGetBreakingNews(hCountryCode: String, hPageNo: Int): Response<NewsResponse> {
        return RetrofitInstance.hNewsApi.hGetBreakingNews(hCountryCode, hPageNo)
    }

    suspend fun hSearchNews(hQuery: String, hPageNo: Int): Response<NewsResponse> {
        return RetrofitInstance.hNewsApi.hSearchNews(hQuery, hPageNo)
    }

    suspend fun hUpSertInDb(hArticle: Article): Long {
        return hDatabase.hGetArticleDao().hUpsertArticle(hArticle)
    }

    fun hGetAllArticles(hArticle: Article): LiveData<List<Article>> {
        return hDatabase.hGetArticleDao().hGetAllArticles()
    }

    fun hDeleteArticle(hArticle: Article) {
        hDatabase.hGetArticleDao().hDeleteArticle(hArticle)
    }


}
