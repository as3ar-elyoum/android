package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ActivityProductsBinding
import com.as3arelyoum.ui.adapter.ProductAdapter
import com.as3arelyoum.ui.factory.ProductsViewModelFactory
import com.as3arelyoum.ui.repositories.ProductsRepository
import com.as3arelyoum.ui.viewModel.ProductsViewModel
import com.as3arelyoum.utils.status.Status
import com.hugocastelani.waterfalltoolbar.Dp

class ProductsActivity : AppCompatActivity() {
    private var _binding: ActivityProductsBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<Product> = ArrayList()
    private lateinit var productsViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        initData()
        setUpRecyclerview()
        obtainListFromServer()
    }

    private fun initData() {
        val productsRepository = ProductsRepository()
        productsViewModel = ViewModelProvider(
            this,
            ProductsViewModelFactory(productsRepository)
        )[ProductsViewModel::class.java]

        binding.recyclerview.adapter =
            ProductAdapter(items) { position -> onProductClicked(position) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun obtainListFromServer() {
        val categoryId = intent.getIntExtra("category_id", 0)

        productsViewModel.getAllProducts(categoryId).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> items.addAll(it1) }
                    binding.recyclerview.adapter?.notifyDataSetChanged()
                }
                Status.FAILURE -> {
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun setUpRecyclerview() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ProductsActivity, 2)
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.categories)
        binding.waterfallToolbar.apply {
            recyclerView = binding.recyclerview
            initialElevation = Dp(0F).toPx()
            finalElevation = Dp(10F).toPx()
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = items[position].id
        val productPrice = items[position].price

        val intent = Intent(this@ProductsActivity, ProductDetailsActivity::class.java)
        intent.putExtra("product_id", productId)
        intent.putExtra("product_price", productPrice)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}