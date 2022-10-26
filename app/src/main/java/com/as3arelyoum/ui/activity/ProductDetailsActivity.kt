package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.R
import com.as3arelyoum.databinding.ActivityDetailsProductBinding
import com.as3arelyoum.ui.factory.ProductDetailsViewModelFactory
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel
import com.as3arelyoum.utils.status.Status
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ProductDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var productDetailsViewModel: ProductDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Navigation Up
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setUpViewModel()
        obtainListFromServer()
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun obtainListFromServer() {
        val productId = intent.getIntExtra("product_id", 0)
        val productPrice = intent.getStringExtra("product_price")

        Log.d("TAG", "obtainListFromServer: $productId")
        productDetailsViewModel.getProductDetails(productId).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { product ->
                        Glide.with(this).load(product.image_url).into(binding.productImage)
                        Log.d("Product", product.prices.first().first());
                        Log.d("Product", product.prices.first().last());

                        binding.nameTv.text = product.name
                        binding.productSource.text = "من ${product.source}"
                        binding.priceTv.text = "$productPrice جنيه مصري "
                        binding.productBtn.text = "اشتري من ${product.source}"
                        binding.productBtn.setOnClickListener {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(product.url))
                            startActivity(browserIntent)
                        }
                        Log.d("TAG", "obtainListFromServer: ${product.name}")
                        setLineChart(product.prices)
                    }
                }
                Status.LOADING -> {}
                Status.FAILURE -> {}
            }
        }
    }

    private fun setUpViewModel() {
        productDetailsViewModel = ViewModelProvider(
            this,
            ProductDetailsViewModelFactory()
        )[ProductDetailsViewModel::class.java]
    }

    private fun setLineChart(prices: Array<Array<String>>) {
        if (prices.count() < 2) {
            return
        }
        val xAxisData = ArrayList<String>()
        val entries = ArrayList<Entry>()
        val lineDataSet = LineDataSet(entries, "السعر بمرور الوقت")
        lineDataSet.color = resources.getColor(R.color.green)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = resources.getColor(R.color.green)
        lineDataSet.fillAlpha = 20
        prices.forEachIndexed { index, price ->
            xAxisData.add(price.first())
            entries.add(Entry(price.last().toFloat(), index))
        }

        val data = LineData(xAxisData, lineDataSet)
        binding.getTheGraph.data = data
        binding.getTheGraph.apply {
            setBackgroundColor(resources.getColor(android.R.color.white))
            animateXY(1000, 1000)
        }
    }
}

