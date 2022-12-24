package com.as3arelyoum.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.models.User
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.ui.adapters.CategoriesAdapter
import com.as3arelyoum.ui.adapters.ProductsAdapter
import com.as3arelyoum.ui.viewModels.HomeViewModel
import com.as3arelyoum.utils.ads.Banner
import com.as3arelyoum.utils.helper.Constants.getDeviceId
import com.as3arelyoum.utils.helper.PrefUtil.getData
import com.as3arelyoum.utils.helper.PrefUtil.initPrefUtil
import com.google.android.gms.ads.AdView
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.suddenh4x.ratingdialog.AppRating
import com.suddenh4x.ratingdialog.preferences.RatingThreshold
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val deviceId: String by lazy { getDeviceId(this) }
    private val categoryAdapter: CategoriesAdapter by lazy { CategoriesAdapter { position -> onCategoryClicked(position) } }
    private val productsAdapter: ProductsAdapter by lazy { ProductsAdapter { position -> onProductClicked(position) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initHomeDataObserve()
        initProductsData(null, "أفضل المنتجات")
        initCategoriesRV()
        initProductsRV()
        initSearchFragment()
        sendUserInfoToServer()
        backToTopButton()
        onBackPress()
        initPrefUtil(this)
        bannerAdView()
        inAppRating()
        inAppUpdate()
    }

    private fun inAppRating() {
        AppRating.Builder(this)
            .setMinimumLaunchTimes(3)
            .setMinimumDays(3)
            .useGoogleInAppReview()
            .setMinimumLaunchTimesToShowAgain(10)
            .setMinimumDaysToShowAgain(7)
            .setRatingThreshold(RatingThreshold.FOUR)
            .showIfMeetsConditions()
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
        val userInfo = User(deviceId, fcmToken)
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
                hideCategoriesProgressBar()
            }
            fetchCategoryData(deviceId)
        }
    }

    private fun initProductsData(categoryId: Int?, categoryName: String) {
        binding.categoriesTv.text = categoryName
        homeViewModel.apply {
            productList.observe(this@HomeActivity) {
                productsAdapter.setProducts(it)
                hideProductsProgressBar()
            }
            errorMessage.observe(this@HomeActivity) {
                Toast.makeText(this@HomeActivity, it, Toast.LENGTH_SHORT).show()
            }
            getSpecificCategoryData(categoryId, deviceId)
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
        homeViewModel.categoryList.value?.get(position)?.let {
            homeViewModel.getSpecificCategoryData(it.id, deviceId)
            binding.categoriesTv.text = it.name

        }
    }

    private fun onProductClicked(position: Int) {
        val product = productsAdapter.productList[position]
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("productId", product.id)
        intent.putExtra("productPrice", product.price)
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
            binding.nestedScrollView.smoothScrollTo(0, 0, 1000)
        }
    }

    private fun hideProductsProgressBar() {
        binding.productsProgressBar.isVisible = false
        binding.categoriesTv.isVisible = true
        binding.productsRv.isVisible = true
    }

    private fun showProductsProgressBar() {
        binding.categoriesTv.isVisible = false
        binding.productsRv.isVisible = false
        binding.productsProgressBar.isVisible = true
    }

    private fun hideCategoriesProgressBar() {
        binding.categoriesProgress.isVisible = false
        binding.categoriesRv.isVisible = true
    }

    private fun inAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= 3
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    this,
                    0
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                inAppUpdate()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}