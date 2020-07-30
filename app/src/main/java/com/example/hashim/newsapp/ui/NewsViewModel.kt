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
    private var hPageNo = 1


    init {
        hGetBreakingNews("us")
    }

    private fun hGetBreakingNews(hCountryCode: String) {
        viewModelScope.launch {
            hBreakingNewsMutableLiveData.value = ResponseResource.Loading()
            val hBreakingNewsResponse =
                hNewsRepository.hGetBreakingNews(hCountryCode, hPageNo)
            hBreakingNewsMutableLiveData.value = hHandleBreakingNewsResposne(hBreakingNewsResponse)
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
}