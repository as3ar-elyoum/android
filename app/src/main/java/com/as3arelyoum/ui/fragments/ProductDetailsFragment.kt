package com.as3arelyoum.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Product
import com.as3arelyoum.databinding.FragmentProductDetailsBinding
import com.as3arelyoum.ui.adapter.CategorySpinnerAdapter
import com.as3arelyoum.ui.adapter.SimilarProductAdapter
import com.as3arelyoum.ui.adapter.StatusSpinnerAdapter
import com.as3arelyoum.ui.factory.CategoryViewModelFactory
import com.as3arelyoum.ui.factory.ProductDetailsViewModelFactory
import com.as3arelyoum.ui.factory.ProductsViewModelFactory
import com.as3arelyoum.ui.factory.SimilarProductsViewModelFactory
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.ui.repositories.ProductsRepository
import com.as3arelyoum.ui.repositories.SimilarProductsRepository
import com.as3arelyoum.ui.viewModel.CategoryViewModel
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel
import com.as3arelyoum.ui.viewModel.ProductsViewModel
import com.as3arelyoum.ui.viewModel.SimilarProductsViewModel
import com.as3arelyoum.utils.Constants
import com.as3arelyoum.utils.ViewAnimation
import com.as3arelyoum.utils.status.Status
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ProductDetailsFragment : Fragment() {
    private var _binding: FragmentProductDetailsBinding? = null
    private val binding get() = _binding!!
    private val similarList: ArrayList<Product> = ArrayList()
    private val arguments: ProductDetailsFragmentArgs by navArgs()
    private lateinit var productDetailsViewModel: ProductDetailsViewModel
    private lateinit var similarProductsViewModel: SimilarProductsViewModel

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
        initProductsViewModel()
        initSimilarProductsRecyclerView()
        initSimilarProductsRepository()
        initProductDetailsObserve(arguments.productId)
        initSimilarProductsObserve(arguments.productId)
        toggleDescription()
        initRefresh()
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
                        initCategorySpinnerAdapter(product.category_id)
                        binding.categoryId.text = product.category_id.toString()
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
        similarProductsViewModel.getSimilarProducts(productId).observe(viewLifecycleOwner) { it ->
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
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initStatusSpinnerAdapter(product_id: Int) {
        val filtersEnabled = requireContext().resources.getBoolean(R.bool.ENABLE_FILTERS)
        if (!filtersEnabled) {
            binding.apply {
                spinnerLayout.visibility = View.GONE
                updateProductBtn.visibility = View.GONE
            }
        }

        val productRepository = ProductsRepository()
        val productsViewModel = ViewModelProvider(
            this,
            ProductsViewModelFactory(productRepository)
        )[ProductsViewModel::class.java]

        productsViewModel.getAllProducts(product_id).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { statusList ->

                        val statusSpinnerAdapter =
                            StatusSpinnerAdapter(requireContext(), statusList)
                        statusSpinnerAdapter.setStatus(statusList)
                        binding.statusSpinner.adapter = statusSpinnerAdapter
                        Log.d("data", "initStatusSpinnerAdapter: $statusList")
                    }
                }
                Status.FAILURE -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun initCategorySpinnerAdapter(category_id: Int) {
        val categoryRepo = CategoryRepository()
        val categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(categoryRepo)
        )[CategoryViewModel::class.java]
        categoryViewModel.getAllCategories().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { categoryList ->
                        val categorySpinnerAdapter =
                            CategorySpinnerAdapter(requireContext(), categoryList)
                        categorySpinnerAdapter.setCategory(categoryList)
                        binding.categorySpinner.adapter = categorySpinnerAdapter
                        val selectedCategory = categoryList.find { it.id == category_id }
                        binding.categorySpinner.setSelection(categoryList.indexOf(selectedCategory))
                    }
                }
                Status.FAILURE -> {}
                Status.LOADING -> {}
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

    private fun setLineChart(prices: Array<Array<String>>) {
        if (prices.count() < 2) {
            binding.graphCard.isVisible = false
            return
        }
        val xAxisData = ArrayList<String>()
        val entries = ArrayList<Entry>()
        val lineDataSet = LineDataSet(entries, "السعر بمرور الوقت")
        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.green)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.green)
        lineDataSet.fillAlpha = 20
        prices.forEachIndexed { index, price ->
            xAxisData.add(price.first())
            entries.add(Entry(price.last().toFloat(), index))
        }

        val data = LineData(xAxisData, lineDataSet)
        binding.lineChart.data = data
        binding.lineChart.apply {
            setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
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