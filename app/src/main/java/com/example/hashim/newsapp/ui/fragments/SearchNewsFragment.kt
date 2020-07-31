/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
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
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private var param1: String? = null
    private var param2: String? = null
    private val hNewsViewModel: NewsViewModel by activityViewModels()
    private lateinit var hNewsAdapter: NewsAdapter

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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

        }
    }

    private fun hSubscribeObservers() {
        hNewsViewModel.hSearchNewsMutableLiveData.observe(
            viewLifecycleOwner, Observer { hResponseResouce ->
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

}