package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.utils.status.Status
import com.as3arelyoum.databinding.ActivityDetailsProductBinding
import com.as3arelyoum.ui.factory.ProductDetailsViewModelFactory
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
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

                        displayChart(product.prices)
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

    private fun displayChart(prices: Array<Array<String>>) {
        binding.getTheGraph.xAxis.apply {
            val stringFormatter = object : ValueFormatter1() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                    return value.toInt().toString()
                }
            }
            labelRotationAngle = 90f;
            valueFormatter = stringFormatter
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
        }

        binding.getTheGraph.axisLeft.isEnabled = false

        val legend: Legend = binding.getTheGraph.legend
        legend.form = Legend.LegendForm.LINE;
        legend.textSize = 11f;
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT;
        legend.orientation = Legend.LegendOrientation.HORIZONTAL;
        legend.setDrawInside(false);

        val xAxisData = ArrayList<String>()

        val entries = ArrayList<Entry>()

        prices.forEachIndexed { index, price ->
            if (index == 0 || index == (prices.count() - 2)) { // first and last only
                xAxisData.add(price.first())
            } else {
                xAxisData.add("")
            }
            entries.add(Entry(index.toFloat(), price.last().toFloat()))
        }


        //We connect our data to the UI Screen
        binding.getTheGraph.xAxis.valueFormatter = object : ValueFormatter1() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return xAxisData[value.toInt()]
            }
        }

        var set1 = LineDataSet(entries, "Price Over Time")

        binding.getTheGraph.data = LineData(set1)
        binding.getTheGraph.animateXY(0, 0, Easing.EaseInCubic)
//        binding.getTheGraph.rotationX = Legend.LegendHorizontalAlignment.LEFT
    }
}