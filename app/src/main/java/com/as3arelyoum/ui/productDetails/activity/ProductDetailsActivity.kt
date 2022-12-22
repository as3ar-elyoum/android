package com.as3arelyoum.ui.productDetails.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.ActivityProductDetailsBinding
import com.as3arelyoum.ui.home.viewModel.HomeViewModel
import com.as3arelyoum.ui.productDetails.adapter.CategorySpinnerAdapter
import com.as3arelyoum.ui.productDetails.adapter.SimilarProductAdapter
import com.as3arelyoum.ui.productDetails.adapter.StatusSpinnerAdapter
import com.as3arelyoum.ui.productDetails.viewModels.ProductDetailsViewModel
import com.as3arelyoum.ui.productDetails.viewModels.SimilarProductsViewModel
import com.as3arelyoum.utils.helper.Constants
import com.as3arelyoum.utils.helper.ViewAnimation
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val similarProductsViewModel: SimilarProductsViewModel by viewModels()
    private val categoryViewModel: HomeViewModel by viewModels()
    private val similarProductAdapter =
        SimilarProductAdapter { position -> onProductClicked(position) }
    private val deviceId: String by lazy { Constants.getDeviceId(this) }
    private val productId: Int by lazy { intent.getIntExtra("productId", 0) }
    private val productPrice: String by lazy { intent.getStringExtra("productPrice")!! }

    private lateinit var productDTOInstance: ProductDTO
    private lateinit var categoryDTOList: List<CategoryDTO>
    private lateinit var lineList: ArrayList<String>
    private lateinit var entries: ArrayList<Entry>
    private lateinit var lineData: LineData
    private var lineDataSet: LineDataSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initSimilarProductsRecyclerView()
        initProductDetailsObserve(productId)
        initSimilarProductsObserve(productId)
        toggleDescription()
        hideProductFilters()
        initClick()
    }

    private fun initClick() {
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initProductDetailsObserve(productId: Int) {
        productDetailsViewModel.productDetails.observe(this) {
            productDTOInstance = it
            Glide.with(this)
                .load(productDTOInstance.image_url)
                .placeholder(R.drawable.ic_downloading)
                .into(binding.productImage)
            binding.apply {
                nameTv.text = productDTOInstance.name
                productSource.text = productDTOInstance.source
                productBtn.text = Constants.displayProductPrice(
                    getString(R.string.buy_from),
                    productDTOInstance.source,
                    getString(R.string.b),
                    productPrice,
                    getString(R.string.egp)
                )
                var description = productDTOInstance.description
                description =
                    if (description.contains("  ") || description.contains("   ")) {
                        description.replace("   ", "\n")
                            .removePrefix(getString(R.string.amazon_first_line))
                            .trim()
                    } else {
                        description.removePrefix(getString(R.string.amazon_first_line))
                            .trim()
                    }
                descriptionTv.text = description

                productBtn.setOnClickListener {
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(productDTOInstance.url))
                    startActivity(browserIntent)
                }

                initCategorySpinnerAdapter()
                initStatusSpinnerAdapter(productDTOInstance.status)
                setLineChart(productDTOInstance.prices)
            }
        }

        binding.updateProductBtn.setOnClickListener {
            val productName = productDTOInstance.name.substring(0, 12)
            val status = Constants.statusList[binding.statusSpinner.selectedItemPosition]
            val categoryId =
                categoryDTOList[binding.categorySpinner.selectedItemPosition].id

            val params = JsonObject()
            val productObject = JsonObject()
            productObject.addProperty("category_id", categoryId)
            productObject.addProperty("status", status)
            params.add("product", productObject)
            productDetailsViewModel.updateProductDetails(productId, params, deviceId)
            Toast.makeText(this, "$productName Updated Successfully", Toast.LENGTH_SHORT).show()
        }

        productDetailsViewModel.getProductDetails(productId, deviceId)
    }

    private fun initSimilarProductsObserve(productId: Int) {
        similarProductsViewModel.similarProductList.observe(this) {
            similarProductAdapter.setSimilarProductsList(it)
        }

        similarProductsViewModel.errorMessage.observe(this) {
        }

        similarProductsViewModel.loading.observe(this) {
        }

        similarProductsViewModel.getSimilarProducts(productId, deviceId)
    }

    private fun initSimilarProductsRecyclerView() {
        binding.rvSimilarProducts.apply {
            setHasFixedSize(true)
            adapter = similarProductAdapter
            layoutManager = LinearLayoutManager(this@ProductDetailsActivity)
        }
    }

    private fun initStatusSpinnerAdapter(status: String) {
        val statusSpinnerAdapter = StatusSpinnerAdapter(this, Constants.statusList)
        binding.statusSpinner.adapter = statusSpinnerAdapter
        val selectedStatus = Constants.statusList.find { it == status }
        binding.statusSpinner.setSelection(Constants.statusList.indexOf(selectedStatus))
    }

    private fun initCategorySpinnerAdapter() {
        lifecycleScope.launch {
            categoryViewModel.fetchCategoryData(deviceId)

            categoryViewModel.categoryList.observe(this@ProductDetailsActivity){
                categoryDTOList = it
                val categorySpinnerAdapter = CategorySpinnerAdapter(this@ProductDetailsActivity, it)
                binding.categorySpinner.adapter = categorySpinnerAdapter
                val selectedCategory = it.find { it.id == productDTOInstance.category_id }
                binding.categorySpinner.setSelection(it.indexOf(selectedCategory))
            }
        }
    }

    private fun setLineChart(prices: List<List<String>>) {
        val greenColor = ContextCompat.getColor(this, R.color.green)
        try {
            lineList = ArrayList()
            entries = ArrayList()

            if (prices.count() < 2) {
                binding.graphCard.isVisible = false
                return
            }

            prices.forEachIndexed { index, price ->
                lineList.add(price.first())
                entries.add(Entry(index.toFloat(), price.last().toFloat()))
            }

            binding.lineChart.xAxis.apply {
                position = XAxis.XAxisPosition.TOP
                valueFormatter = IndexAxisValueFormatter(lineList)
                setLabelCount(4, true)
                setDrawGridLines(true)
                setDrawAxisLine(true)
                setDrawLabels(true)
                textColor = Color.BLACK
                textSize = 10F
            }

            lineDataSet = LineDataSet(entries, "الـسـعـر بـمـرور الـوقـت")
            lineDataSet!!.apply {
                valueTextColor = greenColor
                valueTextSize = 18F
                fillColor = randomColor()
                fillAlpha = 20
                setColors(greenColor)
                setDrawFilled(true)
                setDrawCircles(true)
                setDrawValues(true)
            }

            lineData = LineData(lineDataSet)
            binding.lineChart.data = lineData
            binding.lineChart.apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setPinchZoom(true)
                legend.textSize = 12F
                legend.form = Legend.LegendForm.CIRCLE
                animateXY(1000, 1000)
            }

        } catch (e: Exception) {
            Log.d("LineChart Crash", e.message.toString())
        }
    }

    private fun randomColor(): Int {
        val rnd = Random()
        return ColorTemplate.MATERIAL_COLORS[rnd.nextInt(ColorTemplate.MATERIAL_COLORS.size)]
    }

    private fun hideProductFilters() {
        val filtersEnabled = resources.getBoolean(R.bool.ENABLE_FILTERS)
        if (!filtersEnabled) {
            binding.debugCard.isVisible = false
        }
    }

    private fun onProductClicked(position: Int) {
        val similarProduct = similarProductAdapter.similarProductsList[position]
        Toast.makeText(this, similarProduct.name, Toast.LENGTH_SHORT).show()
    }

    private fun toggleDescription() {
        binding.btToggleDescription.setOnClickListener { view ->
            toggleSection(
                view,
                binding.descriptionTv
            )
        }
        Constants.toggleArrow(binding.btToggleDescription)
    }

    private fun toggleSection(bt: View, lyt: View) {
        val show = Constants.toggleArrow(bt)
        if (show) {
            ViewAnimation.expand(lyt) {
                Constants.nestedScrollTo(binding.nestedScrollView, lyt)
            }
        } else {
            ViewAnimation.collapse(lyt)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}