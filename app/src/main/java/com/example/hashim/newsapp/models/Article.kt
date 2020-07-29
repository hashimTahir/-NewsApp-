/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Articles"
)
data class Article(
    /*? means its nullable*/
    @PrimaryKey(autoGenerate = true)
    var hKey: Int? = null,

    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)