/*
 * Copyright (c) 2020/  7/ 31.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.utils

/*A sealed is kind of an abstract class but only the classed defined here are allowed to
* inherit from it*/
sealed class ResponseResource<T>(
    val hData: T? = null,
    val hMessage: String? = null
) {
    class Success<T>(hData: T) : ResponseResource<T>(hData)
    class Error<T>(hMessage: String, hData: T? = null) : ResponseResource<T>(hData, hMessage)
    class Loading<T> : ResponseResource<T>()


}