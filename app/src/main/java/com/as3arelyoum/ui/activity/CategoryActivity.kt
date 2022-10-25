package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
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
import com.as3arelyoum.utils.status.Status
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.factory.CategoryViewModelFactory
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.ui.viewModel.CategoryViewModel
import com.as3arelyoum.ui.viewModel.SplashScreenViewModel
import com.as3arelyoum.utils.Constants.setAppLocale
import com.hugocastelani.waterfalltoolbar.Dp


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
        categoryViewModel.getAllCategories().observe(this) {
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
        val categoryRepository = CategoryRepository()
        categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(categoryRepository)
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

    private fun onCategoryClicked(position: Int) {
        val categoryId = items[position].id
        val intent = Intent(this@CategoryActivity, ProductsActivity::class.java)
        intent.putExtra("category_id", categoryId)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}