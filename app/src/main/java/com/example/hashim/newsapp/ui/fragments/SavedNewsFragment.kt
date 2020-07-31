/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hashim.newsapp.Constants
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.adapters.NewsAdapter
import com.example.hashim.newsapp.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private var param1: String? = null
    private var param2: String? = null
    private val hNewsViewModel: NewsViewModel by activityViewModels()
    private lateinit var hNewsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hInitRecycler()
        hSubscribeObservers()
    }

    private fun hSubscribeObservers() {
        hNewsViewModel.hGetSavedNews().observe(viewLifecycleOwner, Observer { hArtilcesList ->
            hNewsAdapter.hAsyncListDiffer.submitList(hArtilcesList)
        })
    }

    private fun hInitRecycler() {
        hNewsAdapter = NewsAdapter()
        hNewsAdapter.hSetRecyclerCallBack { hArticle ->
            val hBundle = Bundle().apply {
                this.putSerializable(Constants.H_ARTICLE_IC, hArticle)
            }
            findNavController().navigate(
                R.id.action_hSavedNewsFragment_to_hArticleFragment,
                hBundle
            )
        }
        rvSavedNews.apply {
            adapter = hNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
    }

}