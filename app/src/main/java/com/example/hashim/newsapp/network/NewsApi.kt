/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.network

import com.example.hashim.newsapp.models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(NetworkConstants.H_GET_NEWS_URL)
    suspend fun hGetBreakingNews(
        @Query(NetworkConstants.H_COUNTRY_P)
        hCountryCode: String = "us",
        @Query(NetworkConstants.H_PAGE_P)
        hPageNumber: Int = 1,
        @Query(NetworkConstants.H_API_KEY_P)
        hApiKey: String = NetworkConstants.H_API_KEY
    ): Response<NewsResponse>


    @GET(NetworkConstants.H_GET_SEARCH_NEWS_URL)
    suspend fun hSearchNews(
        @Query(NetworkConstants.H_QUERY_P)
        hSearchQuery: String,
        @Query(NetworkConstants.H_PAGE_P)
        hPageNumber: Int = 1,
        @Query(NetworkConstants.H_API_KEY_P)
        hApiKey: String = NetworkConstants.H_API_KEY
    ): Response<NewsResponse>


}