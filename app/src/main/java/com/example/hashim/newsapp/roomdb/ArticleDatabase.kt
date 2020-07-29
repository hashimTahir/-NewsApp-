/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hashim.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun hGetArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var hArticleDatabaseInstance: ArticleDatabase? = null
        private var H_LOCK = Any()

        operator fun invoke(context: Context) =
            hArticleDatabaseInstance ?: synchronized(H_LOCK) {
                hArticleDatabaseInstance
                    ?: hCreateDataBase(context).also { it -> hArticleDatabaseInstance = it }
            }

        private fun hCreateDataBase(context: Context): () -> ArticleDatabase = {
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "ArticlesDb.db"
            ).build()
        }
    }

}