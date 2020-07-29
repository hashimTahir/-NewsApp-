/*
 * Copyright (c) 2020/  7/ 30.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.roomdb

import androidx.room.TypeConverter
import com.example.hashim.newsapp.models.Source


/*Used to convert custom objects to and from for the db which only can store primitive data types*/
class TypeConverters {
    @TypeConverter
    fun hConvertFromSource(hSource: Source): String {
        return hSource.name
    }

    @TypeConverter
    fun hConvertToSource(name: String): Source {
        return Source(name, name)
    }
}