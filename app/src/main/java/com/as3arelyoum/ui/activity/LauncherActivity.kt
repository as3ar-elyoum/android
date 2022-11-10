package com.as3arelyoum.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.as3arelyoum.R
import com.as3arelyoum.databinding.ActivityLauncherBinding
import com.as3arelyoum.ui.viewModel.SplashScreenViewModel

class LauncherActivity : AppCompatActivity() {
    private var _binding: ActivityLauncherBinding? = null
    private val binding get() = _binding!!
    private val splashViewModel: SplashScreenViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply { setKeepOnScreenCondition { splashViewModel.isLoading.value } }
        super.onCreate(savedInstanceState)
        _binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
    }

    private fun setupNavController(): NavController {
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.show()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        NavigationUI.setupActionBarWithNavController(this, navController)

        return navController
    }

    override fun setTitle(title: CharSequence?) {
        binding.toolbarTitle.text = title
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}