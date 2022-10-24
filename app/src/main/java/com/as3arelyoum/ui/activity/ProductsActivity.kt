package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.data.resources.product.Creator
import com.as3arelyoum.data.resources.status.Status
import com.as3arelyoum.databinding.ActivityProductsBinding
import com.as3arelyoum.ui.adapter.ProductAdapter
import com.as3arelyoum.ui.factory.ProductViewModelFactory
import com.as3arelyoum.ui.viewModel.ProductViewModel
import com.hugocastelani.waterfalltoolbar.Dp

class ProductsActivity : AppCompatActivity() {
    private var _binding: ActivityProductsBinding? = null
    private val binding get() = _binding!!
    private var items: ArrayList<Product> = ArrayList()
    private lateinit var productViewModel: ProductViewModel

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
        productViewModel = ViewModelProvider(
            this,
            ProductViewModelFactory(Creator.getApiHelperInstance())
        )[ProductViewModel::class.java]

        binding.recyclerview.adapter =
            ProductAdapter(items) { position -> onProductClicked(position) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun obtainListFromServer() {
        val categoryId = intent.getIntExtra("category_id", 0)
        productViewModel.getAllProducts(categoryId).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> items.addAll(it1) }
                    binding.recyclerview.adapter?.notifyDataSetChanged()
                }
                Status.FAILURE -> {
                    Toast.makeText(
                        this,
                        "Failed to load the data ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Status.LOADING -> {
                    Toast.makeText(
                        this,
                        "Loading...",
                        Toast.LENGTH_LONG
                    ).show()
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
        binding.toolbar.title = getString(R.string.products)
        binding.waterfallToolbar.apply {
            recyclerView = binding.recyclerview
            initialElevation = Dp(0F).toPx()
            finalElevation = Dp(10F).toPx()
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = items[position].id
        val productPrice = items[position].price
        val productSource = items[position].source

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