/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import androidx.lifecycle.ViewModel
import com.example.hashim.newsapp.repository.NewRepository

class NewsViewModel(
    val hNewsRepository: NewRepository
) : ViewModel() {

}