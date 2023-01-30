package com.as3arelyoum.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.as3arelyoum.R
import com.as3arelyoum.utils.firebase.FirebaseEvents
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseFragment:Fragment() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var appCompactActivity: AppCompatActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseEvents.sendScreenName(this::class.simpleName.toString())
        appCompactActivity = activity as AppCompatActivity
        bottomNavigationView = activity?.findViewById(R.id.bottomView) as BottomNavigationView
        clearGlideMemory()
        navigateUpIcon()
    }

    private fun clearGlideMemory() {
        Glide.get(requireContext()).clearMemory()
    }

    private fun navigateUpIcon() {
        appCompactActivity.supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_navigate_up)
        }
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