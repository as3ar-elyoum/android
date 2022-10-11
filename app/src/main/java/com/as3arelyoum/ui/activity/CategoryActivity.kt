package com.as3arelyoum.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.R
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.viewModel.SplashScreenViewModel
import com.hugocastelani.waterfalltoolbar.Dp
import java.util.*
import kotlin.collections.ArrayList


class CategoryActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<Category> = ArrayList()
    private val viewModel: SplashScreenViewModel by viewModels()

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
        setUpToolbar()
    }

    private fun setUpRecyclerview() {
        val categoryAdapter = CategoryAdapter(this, items)
        { position -> onCategoryClicked(position) }
        fakeData()
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            adapter = categoryAdapter
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

    private fun fakeData() {
        for (i in 1..20) {
            items.add(Category(randomId(), "أمازون", R.mipmap.light_logo))
        }
    }

    private fun randomId(): Int {
        return (1..100).random()
    }

    private fun onCategoryClicked(position: Int) {
        val categoryId = items[position].id
        val intent = Intent(this@CategoryActivity, ProductsActivity::class.java)
        intent.putExtra("categoryId", categoryId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}