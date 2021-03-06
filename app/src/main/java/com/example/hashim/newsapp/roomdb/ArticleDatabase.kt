/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.hashim.newsapp.models.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun hGetArticleDao(): ArticleDao

    companion object {
        @Volatile
        private var hArticleDatabaseInstance: ArticleDatabase? = null
        private var H_LOCK = Any()

        /*invoke runs when the object is initilized
        * synchoronized lock dosent let anyone else access the object while its being accessed*/
        operator fun invoke(context: Context) =
            hArticleDatabaseInstance ?: synchronized(H_LOCK) {
                hArticleDatabaseInstance
                    ?: hCreateDataBase(context).also { hArticleDatabaseInstance = it }
            }

        private fun hCreateDataBase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "ArticlesDb.db"
            ).build()
    }
}

