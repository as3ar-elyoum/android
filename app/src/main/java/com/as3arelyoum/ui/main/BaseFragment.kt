package com.as3arelyoum.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.as3arelyoum.R
import com.as3arelyoum.utils.firebase.FirebaseEvents
import com.google.android.material.bottomappbar.BottomAppBar

open class BaseFragment:Fragment() {
    private lateinit var bottomNavigationView: BottomAppBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseEvents.sendScreenName(this::class.simpleName.toString())
        bottomNavigationView = activity?.findViewById(R.id.bottomAppBar) as BottomAppBar
    }

    fun showToolbar() {
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.show()
    }

    fun hideToolbar() {
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.hide()
    }

    fun showBottomNavigation() {
        bottomNavigationView.isVisible = true
    }

    fun hideBottomNavigation() {
        bottomNavigationView.isVisible = false
    }
}