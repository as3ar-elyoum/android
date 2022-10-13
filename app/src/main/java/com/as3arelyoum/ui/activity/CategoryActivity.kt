package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.data.resources.category.Creator
import com.as3arelyoum.data.resources.status.Status
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.factory.CategoryViewModelFactory
import com.as3arelyoum.ui.viewModel.CategoryViewModel
import com.as3arelyoum.ui.viewModel.SplashScreenViewModel
import com.hugocastelani.waterfalltoolbar.Dp
import java.util.*


class CategoryActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<Category> = ArrayList()
    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        setAppLocale("ar")
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRecyclerview()
        initData()
        setUpToolbar()
        obtainListFromServer()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun obtainListFromServer() {
        categoryViewModel.getAllPosts().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> items.addAll(it1) }
                    binding.recyclerview.adapter?.notifyDataSetChanged()
                }
                Status.FAILURE -> {
                    Toast.makeText(
                        this,
                        "Failed to load the data ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Status.LOADING -> {
                    Toast.makeText(
                        this,
                        "Loading...",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initData() {
        categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(Creator.getApiHelperInstance())
        )[CategoryViewModel::class.java]

        binding.recyclerview.adapter =
            CategoryAdapter(items) { position -> onCategoryClicked(position) }
    }

    private fun setUpRecyclerview() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.app_name)
        binding.waterfallToolbar.apply {
            recyclerView = binding.recyclerview
            initialElevation = Dp(0F).toPx()
            finalElevation = Dp(10F).toPx()

        }
    }

    private fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    private fun onCategoryClicked(position: Int) {
        val postId = items[position].userId
        val intent = Intent(this@CategoryActivity, ProductsActivity::class.java)
        intent.putExtra("postId", postId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}