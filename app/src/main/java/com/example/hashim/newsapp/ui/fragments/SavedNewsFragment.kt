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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hashim.newsapp.Constants
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.adapters.NewsAdapter
import com.example.hashim.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private val hNewsViewModel: NewsViewModel by activityViewModels()
    private lateinit var hNewsAdapter: NewsAdapter
    private val hItemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val hArticle = hNewsAdapter.hAsyncListDiffer.currentList.get(viewHolder.adapterPosition)
            hNewsViewModel.hDeleteArticle(hArticle)
            view?.let {
                Snackbar.make(it, "Article Deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        hNewsViewModel.hSaveArticle(hArticle)
                    }
                    show()
                }
            }
        }
    }


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
            ItemTouchHelper(hItemTouchHelperCallback).apply {
                attachToRecyclerView(rvSavedNews)
            }

        }
    }

}