package com.as3arelyoum.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.databinding.ActivityCategoryBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.factory.CategoryViewModelFactory
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.ui.viewModel.CategoryViewModel
import com.as3arelyoum.ui.viewModel.SplashScreenViewModel
import com.as3arelyoum.utils.ads.Interstitial
import com.as3arelyoum.utils.status.Status

class CategoryActivity : AppCompatActivity() {
    private var _binding: ActivityCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashScreenViewModel by viewModels()
    private val interstitial = Interstitial()
    private var handler = Handler(Looper.myLooper()!!)
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
//    private lateinit var adView: AdView
    private lateinit var runnable: Runnable

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
        initCategoryObserve()
        adView()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 120000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private fun adView() {
//        adView = findViewById(R.id.adView)
//        Banner.show(this, adView)
        runnable = Runnable { interstitial.load(this@CategoryActivity) }
        handler.post(runnable)
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