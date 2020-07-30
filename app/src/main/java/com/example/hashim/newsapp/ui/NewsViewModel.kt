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
    val hNewsRepository: NewsRepository
) : ViewModel() {
    val hBreakingNewsMutableLiveData: MutableLiveData<ResponseResource<NewsResponse>> =
        MutableLiveData()
    var hPageNo = 1

    fun hGetBreakingNews(hCountryCode: String) {
        viewModelScope.launch {
            hBreakingNewsMutableLiveData.value = ResponseResource.Loading()
            var hBreakingNewsResponseResource =
                hNewsRepository.hGetBreakingNews(hCountryCode, hPageNo)
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