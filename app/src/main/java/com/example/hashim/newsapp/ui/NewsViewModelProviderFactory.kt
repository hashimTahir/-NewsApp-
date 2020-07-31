/*
 * Copyright (c) 2020/  7/ 31.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.hashim.newsapp.ApplicationClass
import com.example.hashim.newsapp.repository.NewsRepository

class NewsViewModelProviderFactory(
    val hApplication: Application,
    val hNewsRepository: NewsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(hApplication, hNewsRepository) as T
    }

}