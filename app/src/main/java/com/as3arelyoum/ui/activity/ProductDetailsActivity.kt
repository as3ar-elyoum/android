package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.data.resources.productDetails.Creator
import com.as3arelyoum.data.resources.status.Status
import com.as3arelyoum.databinding.ActivityDetailsProductBinding
import com.as3arelyoum.ui.factory.ProductDetailsViewModelFactory
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel
import com.bumptech.glide.Glide


class ProductDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productDetailsViewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViewModel()
        obtainListFromServer()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun obtainListFromServer() {
        val productId = intent.getIntExtra("product_id", 0)
        val productSource = intent.getStringExtra("product_source")
        val productPrice = intent.getStringExtra("product_price")

        Log.d("TAG", "obtainListFromServer: $productId")
        productDetailsViewModel.getProduct(productId).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 ->
                        Glide.with(this).load(it1.image_url).into(binding.productImage)
                        binding.nameTv.text = it1.name
                        binding.productSource.text = " من $productSource"
                        binding.priceTv.text = "$productPrice جنيه مصري "
                        binding.productBtn.text = "اشتري من $productSource"
                        binding.productBtn.setOnClickListener {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(it1.url))
                            startActivity(browserIntent)                        }
                        Log.d("TAG", "obtainListFromServer: ${it1.name}")
                    }
                }
                Status.LOADING -> {
                    Toast.makeText(
                        this,
                        "Loading...",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Status.FAILURE -> {
                    Toast.makeText(
                        this,
                        "Failed to load the data ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun setUpViewModel() {
        productDetailsViewModel = ViewModelProvider(
            this,
            ProductDetailsViewModelFactory(Creator.getApiHelperInstance())
        )[ProductDetailsViewModel::class.java]
    }
}