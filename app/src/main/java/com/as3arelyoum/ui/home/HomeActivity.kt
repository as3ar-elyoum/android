package com.as3arelyoum.ui.home

import android.os.Build
import android.os.Bundle
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
        onBackPress()
        backToTopButton()
        sendUserInfoToServer()
    }

    private fun bannerAdView() {
        val adView: AdView = findViewById(R.id.adView)
        Banner.show(this, adView)
    }

    private fun sendUserInfoToServer() {
        val fcmToken = getData("token")
        val userInfo = UserInfoDTO(fcmToken, deviceId)
        lifecycleScope.launch {
            if (fcmToken.isNotEmpty()) {
                homeViewModel.sendDevice(userInfo)
            }
        }
    }

    private fun initHomeDataObserve() {
        lifecycleScope.launch {
            val categories = homeViewModel.fetchCategoryData(deviceId)
            categoryAdapter.setCategoriesList(categories)
            val products = homeViewModel.getSpecificCategoryData(categories[0].id, deviceId)
            productsAdapter.setProductsList(products)
            binding.categoriesTv.text = categories[0].name
            hideCategoriesProgressBar()
            hideProductsProgressBar()
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
        val category = categoryAdapter.categoryList[position]
        lifecycleScope.launch {
            val getSpecificCategoryProducts =
                homeViewModel.getSpecificCategoryData(category.id, deviceId)
            productsAdapter.setProductsList(getSpecificCategoryProducts)
            binding.categoriesTv.text = category.name
            hideProductsProgressBar()
        }
    }

    private fun onProductClicked(position: Int) {
        val product = productsAdapter.productList[position]
        Toast.makeText(this, product.name, Toast.LENGTH_SHORT).show()
    }

    private fun hideProductsProgressBar() {
        binding.productsProgressBar.isVisible = false
        binding.categoriesTv.isVisible = true
        binding.productsRv.isVisible = true
    }

    private fun showProductsProgressBar() {
        binding.productsProgressBar.isVisible = true
        binding.categoriesTv.isVisible = false
        binding.productsRv.isVisible = false
    }

    private fun hideCategoriesProgressBar() {
        binding.categoriesProgress.isVisible = false
        binding.categoriesRv.isVisible = true
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}