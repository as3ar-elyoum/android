package com.as3arelyoum.ui.home.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.ui.home.adapters.CategoriesAdapter
import com.as3arelyoum.ui.home.adapters.ProductsAdapter
import com.as3arelyoum.ui.home.viewModel.HomeViewModel
import com.as3arelyoum.ui.productDetails.activity.ProductDetailsActivity
import com.as3arelyoum.ui.search.SearchActivity
import com.as3arelyoum.ui.splach.SplashScreenViewModel
import com.as3arelyoum.utils.ads.Banner
import com.as3arelyoum.utils.helper.Constants.getDeviceId
import com.as3arelyoum.utils.helper.PrefUtil.getData
import com.as3arelyoum.utils.helper.PrefUtil.initPrefUtil
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val splashViewModel: SplashScreenViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val categoryAdapter = CategoriesAdapter { position -> onCategoryClicked(position) }
    private val productsAdapter = ProductsAdapter { position -> onProductClicked(position) }
    private val deviceId: String by lazy { getDeviceId(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply { setKeepOnScreenCondition { splashViewModel.isLoading.value } }
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPrefUtil(this)
        bannerAdView()
        initCategoriesRV()
        initProductsRV()
        initHomeDataObserve()
        initSearchFragment()
        sendUserInfoToServer()
        backToTopButton()
        onBackPress()
    }

    private fun initSearchFragment() {
        binding.searchView.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun bannerAdView() {
        val adView: AdView = findViewById(R.id.adView)
        Banner.show(this, adView)
    }

    private fun sendUserInfoToServer() {
        val fcmToken = getData("token")
        val userInfo = UserInfoDTO(deviceId, fcmToken)
        lifecycleScope.launch {
            if (fcmToken.isNotEmpty()) {
                homeViewModel.sendDevice(userInfo)
            }
        }
    }

    private fun initHomeDataObserve() {
        homeViewModel.apply {
            categoryList.observe(this@HomeActivity) {
                categoryAdapter.setCategoriesList(it)
                initProductsData(null, "أفضل المنتجات")
            }

            errorMessage.observe(this@HomeActivity) {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            }

            loading.observe(this@HomeActivity) { hideCategoriesProgressBar(it) }

            fetchCategoryData(deviceId)
        }
    }

    private fun initProductsData(categoryId: Int?, categoryName: String) {
        homeViewModel.apply {
            productList.observe(this@HomeActivity) {
                Log.d("LOADING...", "Loaded 1")
                hideProductsProgressBar(false)
                productsAdapter.setProductsList(it)

                Log.d("LOADING...", "Data is presented")
            }

            errorMessage.observe(this@HomeActivity) {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            }
//            loading.observe(this@HomeActivity) {
//                Log.d("LOADING...", "Hide progress")
//                hideProductsProgressBar(it)
//            }

            loadProducts(categoryId, deviceId)

            binding.categoriesTv.text = categoryName
        }
    }

    private fun initCategoriesRV() {
        binding.categoriesRv.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initProductsRV() {
        binding.productsRv.apply {
            setHasFixedSize(true)
            adapter = productsAdapter
            layoutManager = GridLayoutManager(this@HomeActivity, 2)
        }
    }

    private fun onCategoryClicked(position: Int) {
        showProductsProgressBar()
        homeViewModel.apply {
            categoryList.value?.get(position)?.let {
                loadProducts(it.id, deviceId)
                binding.categoriesTv.text = it.name
            }
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = productsAdapter.productList[position].id
        val productPrice = productsAdapter.productList[position].price
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("productId", productId)
        intent.putExtra("productPrice", productPrice)
        startActivity(intent)
    }

    private fun onBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nestedScrollPosition = binding.nestedScrollView.scrollY
                if (nestedScrollPosition > 0) {
                    binding.nestedScrollView.smoothScrollTo(0, 0, 1000)
                } else {
                    finish()
                }
            }
        })
    }

    private fun backToTopButton() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                binding.toTopBtn.isVisible = scrollY > 0
            }
        }
        binding.toTopBtn.setOnClickListener {
            binding.nestedScrollView.smoothScrollTo(0, 0, 2000)
        }
    }

    private fun hideProductsProgressBar(it: Boolean) {
        binding.productsProgressBar.isVisible = it
        binding.categoriesTv.isVisible = !it
        binding.productsRv.isVisible = !it

        Log.d("LOADING...", "Progress is hidden")
    }

    private fun showProductsProgressBar() {
        binding.productsProgressBar.isVisible = true
        binding.categoriesTv.isVisible = false
        binding.productsRv.isVisible = false
    }

    private fun hideCategoriesProgressBar(it: Boolean) {
        binding.categoriesProgress.isVisible = it
        binding.categoriesRv.isVisible = !it
    }

    private fun showCategoriesProgressBar(it: Boolean) {
        binding.categoriesProgress.isVisible = !it
        binding.categoriesRv.isVisible = it
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}