package com.as3arelyoum.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.databinding.ActivityCategoryBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.factory.CategoryViewModelFactory
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.ui.viewModel.CategoryViewModel
import com.as3arelyoum.ui.viewModel.SplashScreenViewModel
import com.as3arelyoum.utils.status.Status
import com.hugocastelani.waterfalltoolbar.Dp

class CategoryActivity : AppCompatActivity() {
    private var _binding: ActivityCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashScreenViewModel by viewModels()
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        super.onCreate(savedInstanceState)
        _binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initRepository()
        initToolbar()
        initCategoryObserve()
    }

    private fun initCategoryObserve() {
        categoryViewModel.getAllCategories().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { categoryList ->
                        categoryAdapter.differ.submitList(categoryList)
                    }
                }
                Status.FAILURE -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun initRepository() {
        val categoryRepository = CategoryRepository()
        categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(categoryRepository)
        )[CategoryViewModel::class.java]
    }

    private fun initRecyclerView() {
        categoryAdapter = CategoryAdapter { position -> onCategoryClicked(position) }
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.app_name)
        binding.waterfallToolbar.apply {
            recyclerView = binding.recyclerview
            initialElevation = Dp(0F).toPx()
            finalElevation = Dp(10F).toPx()
        }
    }

    private fun onCategoryClicked(position: Int) {
        val categoryId = categoryAdapter.differ.currentList[position].id
        val categoryName = categoryAdapter.differ.currentList[position].name
        val intent = Intent(this@CategoryActivity, ProductsActivity::class.java)
        intent.putExtra("category_id", categoryId)
        intent.putExtra("category_name", categoryName)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}