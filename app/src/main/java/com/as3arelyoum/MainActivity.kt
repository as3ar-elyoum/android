package com.as3arelyoum

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.databinding.ActivityMainBinding
import com.hugocastelani.waterfalltoolbar.Dp


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<ItemsModel> = ArrayList()
    private val viewModel : SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.app_name)
        setUpRecyclerview()
        setUpToolbar()
    }

    private fun setUpRecyclerview() {
        val adapter = CustomAdapter(this, items)
        fakeData()
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            setAdapter(adapter)
        }
    }

    private fun setUpToolbar() {
        binding.waterfallToolbar.apply {
            recyclerView = binding.recyclerview
            initialElevation = Dp(0F).toPx()
            finalElevation = Dp(30F).toPx()

        }
    }

    private fun fakeData() {
        for (i in 1..20) {
            items.add(ItemsModel("Amazon", R.mipmap.ic_launcher_round))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}