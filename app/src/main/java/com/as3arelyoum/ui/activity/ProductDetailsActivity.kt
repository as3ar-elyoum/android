package com.as3arelyoum.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.ActivityDetailsProductBinding
import com.as3arelyoum.ui.adapter.SimilarProductAdapter
import com.as3arelyoum.ui.factory.ProductDetailsViewModelFactory
import com.as3arelyoum.ui.factory.SimilarProductsViewModelFactory
import com.as3arelyoum.ui.repositories.SimilarProductsRepository
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel
import com.as3arelyoum.ui.viewModel.SimilarProductsViewModel
import com.as3arelyoum.utils.Constants
import com.as3arelyoum.utils.Constants.displayProductDetails
import com.as3arelyoum.utils.Constants.displayProductPrice
import com.as3arelyoum.utils.Constants.toggleArrow
import com.as3arelyoum.utils.ViewAnimation
import com.as3arelyoum.utils.status.Status
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ProductDetailsActivity : AppCompatActivity() {
    private var _binding: ActivityDetailsProductBinding? = null
    private val binding get() = _binding!!
    private val similarList: ArrayList<Product> = ArrayList()
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var similarProductsViewModel: SimilarProductsViewModel
    private val productId by lazy { intent.getIntExtra("product_id", 0) }
    private val productPrice by lazy { intent.getStringExtra("product_price") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initProductsViewModel()
        initSimilarProductsRecyclerView()
        initSimilarProductsRepository()
        initProductDetailsObserve(productId)
        initSimilarProductsObserve(productId)
        toggleDescription()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_ios_back)
            setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun initProductDetailsObserve(productId: Int) {
        productDetailsViewModel.getProductDetails(productId).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { product ->
                        Glide.with(this)
                            .load(product.image_url)
                            .placeholder(R.drawable.ic_downloading)
                            .into(binding.productImage)

                        binding.nameTv.text = product.name
                        binding.productSource.text =
                            displayProductDetails(getString(R.string.from), product.source)
                        binding.productBtn.text =
                            displayProductPrice(
                                getString(R.string.buy_from),
                                product.source,
                                getString(R.string.b),
                                productPrice!!,
                                getString(R.string.egp)
                            )
                        var description = product.description
                        description =
                            if (description.contains("  ") || description.contains("   ")) {
                                description.replace("   ", "\n")
                                    .removePrefix(getString(R.string.amazon_first_line))
                                    .trim()
                            } else {
                                description.removePrefix(getString(R.string.amazon_first_line))
                                    .trim()
                            }
                        binding.descriptionTv.text = description

                        binding.productBtn.setOnClickListener {
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(product.url))
                            startActivity(browserIntent)
                        }
                        setLineChart(product.prices)
                    }
                }
                Status.LOADING -> {}
                Status.FAILURE -> {}
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSimilarProductsObserve(productId: Int) {
        similarProductsViewModel.getSimilarProducts(productId).observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        similarList.addAll(it)
                        binding.rvSimilarProducts.adapter?.notifyDataSetChanged()
                    }
                }
                Status.LOADING -> {}
                Status.FAILURE -> {}
            }
        }
    }

    private fun initProductsViewModel() {
        productDetailsViewModel = ViewModelProvider(
            this,
            ProductDetailsViewModelFactory()
        )[ProductDetailsViewModel::class.java]
    }

    private fun initSimilarProductsRepository() {
        val similarProductsRepository = SimilarProductsRepository()
        similarProductsViewModel = ViewModelProvider(
            this,
            SimilarProductsViewModelFactory(similarProductsRepository)
        )[SimilarProductsViewModel::class.java]
        binding.rvSimilarProducts.apply {
            adapter = SimilarProductAdapter(similarList)
            { position -> onProductClicked(position) }
        }
    }

    private fun initSimilarProductsRecyclerView() {
        binding.rvSimilarProducts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ProductDetailsActivity)
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = similarList[position].id
        val productPrice = similarList[position].price
        val intent = Intent(this@ProductDetailsActivity, ProductDetailsActivity::class.java)
        intent.putExtra("product_id", productId)
        intent.putExtra("product_price", productPrice)
        startActivity(intent)
    }

    private fun setLineChart(prices: Array<Array<String>>) {
        if (prices.count() < 2) {
            binding.graphCard.isVisible = false
            return
        }
        val xAxisData = ArrayList<String>()
        val entries = ArrayList<Entry>()
        val lineDataSet = LineDataSet(entries, "السعر بمرور الوقت")
        lineDataSet.color = ContextCompat.getColor(this, R.color.green)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.green)
        lineDataSet.fillAlpha = 20
        prices.forEachIndexed { index, price ->
            xAxisData.add(price.first())
            entries.add(Entry(price.last().toFloat(), index))
        }

        val data = LineData(xAxisData, lineDataSet)
        binding.lineChart.data = data
        binding.lineChart.apply {
            setBackgroundColor(ContextCompat.getColor(this@ProductDetailsActivity, R.color.white))
            animateXY(1000, 1000)
        }
    }

    private fun toggleDescription() {
        binding.btToggleDescription.setOnClickListener { view ->
            toggleSection(
                view,
                binding.descriptionTv
            )
        }
        toggleArrow(binding.btToggleDescription)
    }


    private fun toggleSection(bt: View, lyt: View) {
        val show = toggleArrow(bt)
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
