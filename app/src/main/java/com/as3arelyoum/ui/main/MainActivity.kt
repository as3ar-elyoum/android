package com.as3arelyoum.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.ui.category.CategoryViewModel
import com.as3arelyoum.ui.splach.SplashScreenViewModel
import com.as3arelyoum.utils.ads.Banner
import com.as3arelyoum.utils.ads.Interstitial
import com.as3arelyoum.utils.firebase.FirebaseEvents.Companion.sendFirebaseEvent
import com.as3arelyoum.utils.helper.PrefUtil
import com.google.android.gms.ads.AdView

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val splashViewModel: SplashScreenViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val handler = Handler(Looper.getMainLooper())
    private val interstitialAd = Interstitial()
    private val repository = AssarRepository()
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply { setKeepOnScreenCondition { splashViewModel.isLoading.value } }
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        sendUserToApi()
        notificationPermission()
        adView()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, 180000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    private fun adView() {
        val adView: AdView = findViewById(R.id.adView)
        Banner.show(this, adView)
        runnable = Runnable { interstitialAd.load(this@MainActivity) }
        handler.post(runnable)
    }

    private fun setupNavController(): NavController {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.show()

        val bottomNavView = binding.bottomView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        return navController
    }

    private fun notificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 99
            )
        }
    }

    private fun sendUserToApi() {
        val userInfoDTO = UserInfoDTO(getUserToken())
        categoryViewModel.sendDevice(userInfoDTO, getUserToken())
    }

    private fun getUserToken(): String {
        return PrefUtil.getData("fcm_token")
    }

    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_url))
        startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        val eventName = "ShareApp"
        sendFirebaseEvent("ShareApp", eventName)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            Navigation.findNavController(this@MainActivity, R.id.fragmentContainerView)
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shareApp -> {
                shareApp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val NOTIFICATION = "NOTIFY_TAG"
    }
}