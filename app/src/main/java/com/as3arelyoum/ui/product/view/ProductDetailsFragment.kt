package com.as3arelyoum.ui.product.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.FragmentProductDetailsBinding
import com.as3arelyoum.ui.category.CategorySpinnerAdapter
import com.as3arelyoum.ui.category.CategoryViewModel
import com.as3arelyoum.ui.product.adapter.SimilarProductAdapter
import com.as3arelyoum.ui.product.adapter.StatusSpinnerAdapter
import com.as3arelyoum.ui.product.viewmodel.ProductDetailsViewModel
import com.as3arelyoum.ui.product.viewmodel.SimilarProductsViewModel
import com.as3arelyoum.utils.helper.Constants
import com.as3arelyoum.utils.helper.ViewAnimation
import com.as3arelyoum.utils.status.Status
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.JsonObject

class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val similarList: ArrayList<ProductDTO> = ArrayList()
    private val arguments: ProductDetailsFragmentArgs by navArgs()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val similarProductsViewModel: SimilarProductsViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var productDTOInstance: ProductDTO
    private lateinit var categoryDTOList: List<CategoryDTO>
    private lateinit var lineList: ArrayList<String>
    private lateinit var entries: ArrayList<Entry>
    private lateinit var lineData: LineData
    private var lineDataSet: LineDataSet? = null
    private val statusList = listOf(
        "inactive",
        "active",
        "disabled",
        "duplicate"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSimilarProductsRecyclerView()
        initSimilarProductsRepository()
        initProductDetailsObserve(arguments.productId)
        initSimilarProductsObserve(arguments.productId)
        toggleDescription()
        initRefresh()
        hideProductFilters()
    }

    private fun initRefresh() {
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            initProductDetailsObserve(arguments.productId)
        }
    }

    private fun initToolbar() {
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.apply {
            requireActivity().title = getString(R.string.product_details)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_ios_back)
        }
    }

    private fun initProductDetailsObserve(productId: Int) {
        productDetailsViewModel.getProductDetails(productId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { product ->
                        productDTOInstance = product
                        Glide.with(this)
                            .load(product.image_url)
                            .placeholder(R.drawable.ic_downloading)
                            .into(binding.productImage)

                        binding.nameTv.text = product.name
                        binding.productSource.text =
                            Constants.displayProductDetails(
                                getString(R.string.from),
                                product.source
                            )
                        binding.productBtn.text =
                            Constants.displayProductPrice(
                                getString(R.string.buy_from),
                                product.source,
                                getString(R.string.b),
                                arguments.productPrice,
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

                        initCategorySpinnerAdapter()
                        initStatusSpinnerAdapter(product.status)
                        setLineChart(productDTOInstance.prices)

                        binding.updateProductBtn.setOnClickListener {
                            val status = statusList[binding.statusSpinner.selectedItemPosition]
                            val categoryId =
                                categoryDTOList[binding.categorySpinner.selectedItemPosition].id

                            val params = JsonObject()
                            val productObject = JsonObject()
                            productObject.addProperty("category_id", categoryId)
                            productObject.addProperty("status", status)
                            params.add("product", productObject)

                            productDetailsViewModel.updateProductDetails(
                                product.id,
                                params
                            ).observe(viewLifecycleOwner) {
                                when (it.status) {
                                    Status.SUCCESS -> {
                                        Toast.makeText(
                                            requireContext(),
                                            "تم تعديل المنتج",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Status.LOADING -> {}
                                    Status.FAILURE -> {}
                                }
                            }
                        }
                    }
                }
                Status.LOADING -> {}
                Status.FAILURE -> {}
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSimilarProductsObserve(productId: Int) {
        similarProductsViewModel.getSimilarProducts(productId).observe(viewLifecycleOwner) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        similarList.clear()
                        similarList.addAll(it)
                        binding.rvSimilarProducts.adapter?.notifyDataSetChanged()
                    }
                }
                Status.LOADING -> {}
                Status.FAILURE -> {}
            }
        }
    }

    private fun initSimilarProductsRepository() {
        binding.rvSimilarProducts.apply {
            adapter = SimilarProductAdapter(similarList)
            { position -> onProductClicked(position) }
        }
    }

    private fun initSimilarProductsRecyclerView() {
        binding.rvSimilarProducts.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initStatusSpinnerAdapter(status: String) {
        val statusSpinnerAdapter = StatusSpinnerAdapter(requireContext(), statusList)
        binding.statusSpinner.adapter = statusSpinnerAdapter
        val selectedStatus = statusList.find { it == status }
        binding.statusSpinner.setSelection(statusList.indexOf(selectedStatus))
    }

    private fun initCategorySpinnerAdapter() {
        categoryViewModel.categoriesList.observe(viewLifecycleOwner) {
            categoryDTOList = it
            val categorySpinnerAdapter =
                CategorySpinnerAdapter(requireContext(), categoryDTOList)
            binding.categorySpinner.adapter = categorySpinnerAdapter

            val selectedCategory =
                categoryDTOList.find { it.id == productDTOInstance.category_id }
            binding.categorySpinner.setSelection(categoryDTOList.indexOf(selectedCategory))
        }

        categoryViewModel.errorMessage.observe(viewLifecycleOwner) {
            Log.e("error", it)
        }

        categoryViewModel.loading.observe(viewLifecycleOwner) {}
        categoryViewModel.getAllCategories()
    }

    private fun setLineChart(prices: Array<Array<String>>) {
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
            val xAxis = binding.lineChart.xAxis
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.valueFormatter = IndexAxisValueFormatter(lineList)
            xAxis.setLabelCount(2, false)

            lineDataSet = LineDataSet(entries, "الـسـعـر بـمـرور الـوقـت")
            lineData = LineData(lineDataSet)
            binding.lineChart.data = lineData
            lineDataSet!!.apply {
                valueTextColor = ContextCompat.getColor(requireContext(), R.color.chart_prices)
                valueTextSize = 13f
                fillColor = Color.BLACK
                fillAlpha = 20
                setColors(*ColorTemplate.JOYFUL_COLORS)
                setDrawFilled(true)
                setDrawCircles(true)
                setDrawValues(true)
            }
            binding.lineChart.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                animateXY(1000, 1000)
            }

        } catch (e: Exception) {
            Log.d("LineChart Crash", e.message.toString())
        }
    }

    private fun hideProductFilters() {
        val filtersEnabled = requireContext().resources.getBoolean(R.bool.ENABLE_FILTERS)
        if (!filtersEnabled) {
            binding.apply {
                spinnerLayout.visibility = View.GONE
                updateProductBtn.visibility = View.GONE
            }
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = similarList[position].id
        val productPrice = similarList[position].price
        val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentSelf(
            productId,
            productPrice
        )
        findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}