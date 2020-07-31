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
import com.example.hashim.newsapp.utils.ResponseResource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import timber.log.Timber


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val hNewsViewModel: NewsViewModel by activityViewModels()
    private lateinit var hNewsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hInitRecycler()
        hSubscribeObservers()
    }

    private fun hSubscribeObservers() {
        hNewsViewModel.hBreakingNewsMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { hResponseResouce ->
                when (hResponseResouce) {
                    is ResponseResource.Success -> {
                        hHideProgressBar()
                        hResponseResouce.hData?.let { newsResponse ->
                            hNewsAdapter.hAsyncListDiffer.submitList(newsResponse.articles)
                        }
                    }
                    is ResponseResource.Error -> {
                        hHideProgressBar()
                        hResponseResouce.hMessage?.let {
                            Timber.d("Response Error $it")
                        }
                    }
                    is ResponseResource.Loading -> {
                        hShowProgressBar()
                        Timber.d("Loading state")
                    }
                }

            })
    }

    private fun hHideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun hShowProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hInitRecycler() {
        hNewsAdapter = NewsAdapter()
        hNewsAdapter.hSetRecyclerCallBack { hArticle ->
            val hBundle = Bundle().apply {
                this.putSerializable(Constants.H_ARTICLE_IC, hArticle)
            }
            findNavController().navigate(
                R.id.action_hBreakingNewsFragment_to_hArticleFragment,
                hBundle
            )
        }
        rvBreakingNews.apply {
            adapter = hNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())

        }
    }

}