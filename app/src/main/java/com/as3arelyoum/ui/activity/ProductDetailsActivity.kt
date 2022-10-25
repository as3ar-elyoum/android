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
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter as ValueFormatter1


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
        val productPrice = intent.getStringExtra("product_price")

        Log.d("TAG", "obtainListFromServer: $productId")
        productDetailsViewModel.getProduct(productId).observe(this) {
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

                        displayChart(product.prices)
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

    fun displayChart(prices: Array<Array<String>>) {
        binding.getTheGraph.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM;

            val stringFormatter = object : ValueFormatter1() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return value.toInt().toString()
                }
            }

            valueFormatter = stringFormatter
        }

        val xAxisData = ArrayList<String>()

        val entries = ArrayList<Entry>()

        prices.forEachIndexed { index, price ->
            xAxisData.add(price.first())
            entries.add(Entry(index.toFloat(), price.last().toFloat()))
            Log.d(price.first(), price.last())
        }


        //We connect our data to the UI Screen
        binding.getTheGraph.xAxis.valueFormatter = object : ValueFormatter1() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return xAxisData[value.toInt()]
            }
        }

        var data = LineDataSet(entries, "Price Over Data")
        binding.getTheGraph.data = LineData(data)

        binding.getTheGraph.animateXY(2000, 2000, Easing.EaseInCubic)
    }
}