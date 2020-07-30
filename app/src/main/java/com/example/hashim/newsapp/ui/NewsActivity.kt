/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.hashim.newsapp.R
import com.example.hashim.newsapp.repository.NewsRepository
import com.example.hashim.newsapp.roomdb.ArticleDatabase
import kotlinx.android.synthetic.main.activity_main.*


class NewsActivity : AppCompatActivity() {

    private var hNewNavHostFragment: NavHostFragment? = null
    private lateinit var hNavController: NavController
    private lateinit var hNewsViewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hNewRepository = NewsRepository(ArticleDatabase(this))
        val hViewModelProviderFactory = NewsViewModelProviderFactory(hNewRepository)
        hNewsViewModel =
            ViewModelProvider(this, hViewModelProviderFactory).get(NewsViewModel::class.java)


        hInitNavController()
    }

    private fun hInitNavController() {
        hNewNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.hNewsNavHostFragment) as NavHostFragment?
        hNavController = hNewNavHostFragment!!.navController
        hNavController.setGraph(R.navigation.main_nav_host_navigation)
        NavigationUI.setupWithNavController(hBottomNavigationView, hNavController)

    }
}