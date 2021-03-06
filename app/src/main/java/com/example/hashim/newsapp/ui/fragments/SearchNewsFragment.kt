/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hashim.newsapp.Constants
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.adapters.NewsAdapter
import com.example.hashim.newsapp.ui.NewsViewModel
import com.example.hashim.newsapp.utils.ResponseResource
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private val hNewsViewModel: NewsViewModel by activityViewModels()
    private lateinit var hNewsAdapter: NewsAdapter


    private var hIsScrolling = false
    private var hIsLastPage = false
    private var hIsLoading = false

    private val hScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val hLinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val hFirstVisibleItemPosition = hLinearLayoutManager.findFirstVisibleItemPosition()
            val hVisibleChildCount = hLinearLayoutManager.childCount
            val hTotalItemCount = hLinearLayoutManager.itemCount

            val hIsNotLodaingAndNotLastPage = !hIsLoading && !hIsLastPage
            val hIsAtLastItem = hFirstVisibleItemPosition + hVisibleChildCount >= hTotalItemCount
            val hIsNotAtBeginning = hFirstVisibleItemPosition >= 0
            val hIsTotalMoreThanVisible = hTotalItemCount >= Constants.H_PAGE_SIZE

            val hShouldPaginate = hIsNotLodaingAndNotLastPage && hIsAtLastItem
                    && hIsNotAtBeginning && hIsTotalMoreThanVisible && hIsScrolling

            if (hShouldPaginate) {
                hNewsViewModel.hSearchNews(etSearch.text.toString())
                hIsScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                hIsScrolling = true
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hInitRecycler()
        hAddTextChangeListenerWithDelay()
        hSubscribeObservers()

    }

    private fun hAddTextChangeListenerWithDelay() {
        var hJob: Job? = null
        etSearch.addTextChangedListener {
            hJob?.cancel()
            hJob = MainScope().launch {
                delay(Constants.H_500MILLS_DELAY)
                if (it.toString().isNotEmpty()) {
                    hNewsViewModel.hSearchNews(it.toString())
                }
            }
        }

    }

    private fun hInitRecycler() {
        hNewsAdapter = NewsAdapter()
        hNewsAdapter.hSetRecyclerCallBack { hArticle ->
            val hBundle = Bundle().apply {
                this.putSerializable(Constants.H_ARTICLE_IC, hArticle)
            }
            findNavController().navigate(
                R.id.action_hSearchNewsFragment_to_hArticleFragment,
                hBundle
            )
        }
        rvSearchNews.apply {
            adapter = hNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(hScrollListener)

        }
    }

    private fun hSubscribeObservers() {
        hNewsViewModel.hSearchNewsMutableLiveData.observe(
            viewLifecycleOwner, Observer { hResponseResouce ->
                when (hResponseResouce) {
                    is ResponseResource.Success -> {
                        hHideProgressBar()
                        hResponseResouce.hData?.let { newsResponse ->
                            hNewsAdapter.hAsyncListDiffer.submitList(newsResponse.articles.toList())

                            val hTotalPages = newsResponse.totalResults / Constants.H_PAGE_SIZE + 2
                            hIsLastPage = hNewsViewModel.hSearchNewsPageNo == hTotalPages
                            if (hIsLastPage) {
                                rvSearchNews.setPadding(0, 0, 0, 0)
                            }

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
        hIsLoading = false
    }

    private fun hShowProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        hIsLoading = true
    }

}