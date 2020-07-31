/*
 * Copyright (c) 2020/  7/ 31.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp

import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(object : DebugTree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) {
                    super.log(priority, java.lang.String.format(Constants.hTag, tag), message, t)
                }
            })
        }
    }

}