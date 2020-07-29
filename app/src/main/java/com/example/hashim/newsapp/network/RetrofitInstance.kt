/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        /*lazy means its initilized only once*/
        private val hRetrofitInstance by lazy {
            val hHttpLoggingInterceptor = HttpLoggingInterceptor()
            hHttpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val hOkHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(hHttpLoggingInterceptor)
                .build()

            Retrofit.Builder()
                .baseUrl(NetworkConstants.H_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(hOkHttpClientBuilder)
                .build()
        }
        val hNewsApi by lazy {
            hRetrofitInstance.create(NewsApi::class.java)
        }
    }
}