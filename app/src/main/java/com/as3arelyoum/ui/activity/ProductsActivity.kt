package com.as3arelyoum.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ActivityMainBinding
import com.as3arelyoum.databinding.ActivityProductsBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.adapter.ProductAdapter
import com.hugocastelani.waterfalltoolbar.Dp

class ProductsActivity : AppCompatActivity() {
    private var _binding: ActivityProductsBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpRecyclerview()
    }

    private fun setUpRecyclerview() {
        val productAdapter = ProductAdapter(this, items)
        fakeData()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@ProductsActivity)
            adapter = productAdapter
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

    private fun fakeData() {
        for (i in 1..20) {
            items.add(Product("إلكترونيات", R.mipmap.light_logo))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}