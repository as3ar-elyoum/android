package com.as3arelyoum.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.databinding.ActivityProductsBinding
import com.as3arelyoum.ui.adapter.ProductsAdapter
import com.as3arelyoum.ui.factory.ProductsViewModelFactory
import com.as3arelyoum.ui.repositories.ProductsRepository
import com.as3arelyoum.ui.viewModel.ProductsViewModel
import com.as3arelyoum.utils.status.Status

class ProductsActivity : AppCompatActivity() {
    private var _binding: ActivityProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var productsAdapter: ProductsAdapter
    private val categoryId by lazy { intent.getIntExtra("category_id", 0) }
    private val categoryName by lazy { intent.getStringExtra("category_name") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initRecyclerView()
        initRepository()
        initProductsObserve()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbarTitle.text = categoryName
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_ios_back)
            setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun initRepository() {
        val productsRepository = ProductsRepository()
        productsViewModel = ViewModelProvider(
            this,
            ProductsViewModelFactory(productsRepository)
        )[ProductsViewModel::class.java]
    }

    private fun initProductsObserve() {
        productsViewModel.getAllProducts(categoryId).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { productsList ->
                        productsAdapter.differ.submitList(productsList)
                    }
                }
                Status.FAILURE -> {
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            productsAdapter = ProductsAdapter { position -> onProductClicked(position) }
            adapter = productsAdapter
            layoutManager = GridLayoutManager(this@ProductsActivity, 2)
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = productsAdapter.differ.currentList[position].id
        val productPrice = productsAdapter.differ.currentList[position].price
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
