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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.databinding.FragmentProductDetailsBinding
import com.as3arelyoum.ui.category.CategorySpinnerAdapter
import com.as3arelyoum.ui.home.HomeViewModel
import com.as3arelyoum.ui.product.adapter.SimilarProductAdapter
import com.as3arelyoum.ui.product.adapter.StatusSpinnerAdapter
import com.as3arelyoum.ui.product.viewmodel.ProductDetailsViewModel
import com.as3arelyoum.ui.product.viewmodel.SimilarProductsViewModel
import com.as3arelyoum.utils.helper.Constants
import com.as3arelyoum.utils.helper.Constants.statusList
import com.as3arelyoum.utils.helper.ViewAnimation
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.JsonObject
import java.util.*

class ProductDetailsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val similarList: ArrayList<ProductDTO> = ArrayList()
    private val arguments: ProductDetailsFragmentArgs by navArgs()
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private val similarProductsViewModel: SimilarProductsViewModel by viewModels()
    private val categoryViewModel: HomeViewModel by viewModels()
    private val similarProductAdapter =
        SimilarProductAdapter(similarList) { position -> onProductClicked(position) }
    private val deviceId: String by lazy { Constants.getDeviceId(requireContext()) }
    private lateinit var productDTOInstance: ProductDTO
    private lateinit var categoryDTOList: List<CategoryDTO>
    private lateinit var lineList: ArrayList<String>
    private lateinit var entries: ArrayList<Entry>
    private lateinit var lineData: LineData
    private var lineDataSet: LineDataSet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        initSimilarProductsRecyclerView()
        initProductDetailsObserve(arguments.productId)
        initSimilarProductsObserve(arguments.productId)
        toggleDescription()
        hideProductFilters()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProductSheet()
    }

    private fun initProductSheet() {
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = true
            skipCollapsed = false
        }

        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams

        binding.productPopupBack.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initProductDetailsObserve(productId: Int) {

        productDetailsViewModel.productDetails.observe(viewLifecycleOwner) {
            productDTOInstance = it
            Glide.with(this)
                .load(productDTOInstance.image_url)
                .placeholder(R.drawable.ic_downloading)
                .into(binding.productImage)
            binding.apply {
                nameTv.text = productDTOInstance.name
                productSource.text = Constants.displayProductDetails(
                    getString(R.string.from),
                    productDTOInstance.source
                )
                productBtn.text = Constants.displayProductPrice(
                    getString(R.string.buy_from),
                    productDTOInstance.source,
                    getString(R.string.b),
                    arguments.productPrice,
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
            val status = statusList[binding.statusSpinner.selectedItemPosition]
            val categoryId =
                categoryDTOList[binding.categorySpinner.selectedItemPosition].id

            val params = JsonObject()
            val productObject = JsonObject()
            productObject.addProperty("category_id", categoryId)
            productObject.addProperty("status", status)
            params.add("product", productObject)

            productDetailsViewModel.updateProductDetails(productId, params, deviceId)
        }

        productDetailsViewModel.getProductDetails(productId, deviceId)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSimilarProductsObserve(productId: Int) {
        similarProductsViewModel.similarProductList.observe(viewLifecycleOwner) {
            similarList.addAll(it)
            similarProductAdapter.notifyDataSetChanged()
        }

        similarProductsViewModel.errorMessage.observe(viewLifecycleOwner) {
        }

        similarProductsViewModel.loading.observe(viewLifecycleOwner) {
        }

        similarProductsViewModel.getSimilarProducts(productId, deviceId)
    }

    private fun initSimilarProductsRecyclerView() {
        binding.rvSimilarProducts.apply {
            setHasFixedSize(true)
            adapter = similarProductAdapter
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

        categoryViewModel.getCategoriesSpinner()
    }

    private fun setLineChart(prices: List<List<String>>) {
        val greenColor = ContextCompat.getColor(requireContext(), R.color.green)

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