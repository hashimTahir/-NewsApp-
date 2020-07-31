/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.hashim.newsapp.ApplicationClass
import com.example.hashim.newsapp.models.Article
import com.example.hashim.newsapp.models.NewsResponse
import com.example.hashim.newsapp.repository.NewsRepository
import com.example.hashim.newsapp.utils.ResponseResource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    hApplication: Application,
    private val hNewsRepository: NewsRepository
) : AndroidViewModel(hApplication) {
    val hBreakingNewsMutableLiveData: MutableLiveData<ResponseResource<NewsResponse>> =
        MutableLiveData()
    public var hBreakingNewsPageNo = 1
    private var hBreakingNewsResponse: NewsResponse? = null

    val hSearchNewsMutableLiveData: MutableLiveData<ResponseResource<NewsResponse>> =
        MutableLiveData()
    public var hSearchNewsPageNo = 1
    private var hSearchNewsResponse: NewsResponse? = null

    init {
        hGetBreakingNews("us")
    }

    public fun hGetBreakingNews(hCountryCode: String) {
        viewModelScope.launch {
            hBreakingNewsMutableLiveData.value = ResponseResource.Loading()
            try {
                if (hHasInternet()) {
                    val hBreakingNewsResponse =
                        hNewsRepository.hGetBreakingNews(hCountryCode, hBreakingNewsPageNo)
                    hBreakingNewsMutableLiveData.value =
                        hHandleBreakingNewsResposne(hBreakingNewsResponse)
                } else {
                    hBreakingNewsMutableLiveData.value =
                        ResponseResource.Error("No internet connection")
                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> hBreakingNewsMutableLiveData.value =
                        ResponseResource.Error("Network failure")
                    else -> hBreakingNewsMutableLiveData.value =
                        ResponseResource.Error("Conversion Error")
                }
            }

        }
    }

    fun hSearchNews(hQuery: String) {
        viewModelScope.launch {
            hSearchNewsMutableLiveData.value = ResponseResource.Loading()
            try {
                if (hHasInternet()) {
                    val hSearchNewsResponse =
                        hNewsRepository.hSearchNews(hQuery, hSearchNewsPageNo)
                    hSearchNewsMutableLiveData.value =
                        hHandleSearchNewsResposne(hSearchNewsResponse)
                } else {
                    hBreakingNewsMutableLiveData.value =
                        ResponseResource.Error("No internet connection")
                }

            } catch (t: Throwable) {
                when (t) {
                    is IOException -> hBreakingNewsMutableLiveData.value =
                        ResponseResource.Error("Network failure")
                    else -> hBreakingNewsMutableLiveData.value =
                        ResponseResource.Error("Conversion Error")
                }
            }

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


    private fun hHasInternet(): Boolean {
        val hConnectivityManager =
            getApplication<ApplicationClass>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val hActiveNetwork = hConnectivityManager.activeNetwork
        hActiveNetwork ?: return false
        val hCapabilities =
            hConnectivityManager.getNetworkCapabilities(hActiveNetwork) ?: return false
        return when {
            hCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            hCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            hCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }

    }
}