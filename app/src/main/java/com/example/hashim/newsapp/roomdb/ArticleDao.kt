/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.hashim.newsapp.models.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun hUpsertArticle(hArticle: Article): Long

    @Query(RoomQuerys.H_SELECT_ALL_ARTICLES)
    fun hGetSavedArticles(): LiveData<List<Article>>

    @Delete
    suspend fun hDeleteArticle(hArticle: Article)

}