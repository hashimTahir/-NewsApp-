/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private var hBreakingNewsPageNo = 1

    val hSearchNewsMutableLiveData: MutableLiveData<ResponseResource<NewsResponse>> =
        MutableLiveData()
    private var hSearchNewsPageNo = 1

    init {
        hGetBreakingNews("us")
    }

    private fun hGetBreakingNews(hCountryCode: String) {
        viewModelScope.launch {
            hBreakingNewsMutableLiveData.value = ResponseResource.Loading()
            val hBreakingNewsResponse =
                hNewsRepository.hGetBreakingNews(hCountryCode, hBreakingNewsPageNo)
            hBreakingNewsMutableLiveData.value = hHandleBreakingNewsResposne(hBreakingNewsResponse)
        }
    }

    public fun hSearchNews(hQuery: String) {
        viewModelScope.launch {
            hSearchNewsMutableLiveData.value = ResponseResource.Loading()
            val hSearchNewsResponse =
                hNewsRepository.hSearchNews(hQuery, hSearchNewsPageNo)
            hBreakingNewsMutableLiveData.value = hHandleSearchNewsResposne(hSearchNewsResponse)
        }
    }


    private fun hHandleBreakingNewsResposne(hResponse: Response<NewsResponse>): ResponseResource<NewsResponse> {
        if (hResponse.isSuccessful) {
            hResponse.body()?.let { newsResponse ->
                return ResponseResource.Success(newsResponse)
            }
        }
        return ResponseResource.Error(hResponse.message())
    }

    private fun hHandleSearchNewsResposne(hResponse: Response<NewsResponse>): ResponseResource<NewsResponse> {
        if (hResponse.isSuccessful) {
            hResponse.body()?.let { newsResponse ->
                return ResponseResource.Success(newsResponse)
            }
        }
        return ResponseResource.Error(hResponse.message())
    }
}