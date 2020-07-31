/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.models.Article
import com.example.hashim.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment : Fragment(R.layout.fragment_article) {
    private val hNewsViewModel: NewsViewModel by activityViewModels()
    private val hArguments: ArticleFragmentArgs by navArgs()

    private val hArticle: Article
        get() {
            return hArguments.hArticle
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hSetupView()
        hSetupListners()

    }

    private fun hSetupListners() {
        fab.setOnClickListener { view ->
            hNewsViewModel.hSaveArticle(hArticle)
            Snackbar.make(view, "Article Saved", Snackbar.LENGTH_SHORT)
                .show()
        }
    }


    private fun hSetupView() {
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(hArticle.url)
        }
    }

}