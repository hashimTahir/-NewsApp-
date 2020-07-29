/*
 * Copyright (c) 2020/  7/ 29.  Created by Hashim Tahir
 */

package com.example.hashim.newsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.hashim.newsapp.R
import kotlinx.android.synthetic.main.activity_main.*


class NewsActivity : AppCompatActivity() {

    private var hNewNavHostFragment: NavHostFragment? = null
    private lateinit var hNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hInitNavController()
    }

    private fun hInitNavController() {
        hNewNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.hNewsNavHostFragment) as NavHostFragment?
        hNavController = hNewNavHostFragment!!.navController
        hNavController?.setGraph(R.navigation.main_nav_host_navigation)
        NavigationUI.setupWithNavController(hBottomNavigationView, hNavController)

    }
}