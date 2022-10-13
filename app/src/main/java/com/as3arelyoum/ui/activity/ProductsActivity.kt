package com.as3arelyoum.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.databinding.ActivityProductsBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.adapter.ProductAdapter
import com.as3arelyoum.ui.viewModel.ProductViewModel
import com.hugocastelani.waterfalltoolbar.Dp

class ProductsActivity : AppCompatActivity() {
    private var _binding: ActivityProductsBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<Product> = ArrayList()
    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpViewModel()
        setUpRecyclerview()
    }

    private fun setUpViewModel() {
        productViewModel.fakeData()
        productViewModel.productLiveData.observe(this) {
            items = it as ArrayList<Product>
            productAdapter = ProductAdapter(this, items)
            binding.recyclerview.adapter = productAdapter
        }
    }

    private fun setUpRecyclerview() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProductsActivity)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.products)
        binding.waterfallToolbar.apply {
            recyclerView = binding.recyclerview
            initialElevation = Dp(0F).toPx()
            finalElevation = Dp(10F).toPx()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}