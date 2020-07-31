/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hashim.newsapp.models.Article
import com.example.hashim.newsapp.models.NewsResponse
import com.example.hashim.newsapp.repository.NewsRepository
import com.example.hashim.newsapp.utils.ResponseResource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    private val hNewsRepository: NewsRepository
) : ViewModel() {
    val hBreakingNewsMutableLiveData: MutableLiveData<ResponseResource<NewsResponse>> =
        MutableLiveData()
    public var hBreakingNewsPageNo = 1
    private var hBreakingNewsResponse: NewsResponse? = null

    val hSearchNewsMutableLiveData: MutableLiveData<ResponseResource<NewsResponse>> =
        MutableLiveData()
    private var hSearchNewsPageNo = 1
    private var hSearchNewsResponse: NewsResponse? = null

    init {
        hGetBreakingNews("us")
    }

    public fun hGetBreakingNews(hCountryCode: String) {
        viewModelScope.launch {
            hBreakingNewsMutableLiveData.value = ResponseResource.Loading()
            val hBreakingNewsResponse =
                hNewsRepository.hGetBreakingNews(hCountryCode, hBreakingNewsPageNo)
            hBreakingNewsMutableLiveData.value = hHandleBreakingNewsResposne(hBreakingNewsResponse)
        }
    }

    fun hSearchNews(hQuery: String) {
        viewModelScope.launch {
            hSearchNewsMutableLiveData.value = ResponseResource.Loading()
            val hSearchNewsResponse =
                hNewsRepository.hSearchNews(hQuery, hSearchNewsPageNo)
            hSearchNewsMutableLiveData.value = hHandleSearchNewsResposne(hSearchNewsResponse)
        }
    }


    private fun hHandleBreakingNewsResposne(hResponse: Response<NewsResponse>): ResponseResource<NewsResponse> {
        if (hResponse.isSuccessful) {
            hResponse.body()?.let { newsResponse ->
                hBreakingNewsPageNo++
                if (hBreakingNewsResponse == null) {
                    hBreakingNewsResponse = newsResponse
                } else {
                    val hOldArticles = hBreakingNewsResponse?.articles
                    val hNewArticles = newsResponse.articles
                    hOldArticles?.addAll(hNewArticles)
                }
                return ResponseResource.Success(hBreakingNewsResponse ?: newsResponse)
            }
        }
        return ResponseResource.Error(hResponse.message())
    }

    private fun hHandleSearchNewsResposne(hResponse: Response<NewsResponse>): ResponseResource<NewsResponse> {
        if (hResponse.isSuccessful) {
            hResponse.body()?.let { newsResponse ->
                hSearchNewsPageNo++
                if (hSearchNewsResponse == null) {
                    hSearchNewsResponse = newsResponse
                } else {
                    val hOldArticles = hSearchNewsResponse?.articles
                    val hNewArticles = newsResponse.articles
                    hOldArticles?.addAll(hNewArticles)
                }

                return ResponseResource.Success(hSearchNewsResponse ?: newsResponse)
            }
        }
        return ResponseResource.Error(hResponse.message())
    }


    fun hSaveArticle(hArticle: Article) {
        viewModelScope.launch {
            hNewsRepository.hUpSertInDb(hArticle)
        }
    }

    fun hGetSavedNews(): LiveData<List<Article>> {
        return hNewsRepository.hGetSavedArticles()
    }

    fun hDeleteArticle(hArticle: Article) {
        viewModelScope.launch {
            hNewsRepository.hDeleteArticle(hArticle)
        }
    }
}