/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.ui.NewsActivity
import com.example.hashim.newsapp.ui.NewsViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ArticleFragment : Fragment(R.layout.fragment_article) {
    private var param1: String? = null
    private var param2: String? = null
    private val hNewsViewModel: NewsViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BreakingNewsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}