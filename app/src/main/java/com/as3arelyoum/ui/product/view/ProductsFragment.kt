package com.as3arelyoum.ui.product.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.databinding.FragmentProductsBinding
import com.as3arelyoum.ui.main.BaseFragment
import com.as3arelyoum.ui.main.MainActivity
import com.as3arelyoum.ui.product.adapter.ProductsAdapter
import com.as3arelyoum.ui.product.viewmodel.ProductsViewModel

class ProductsFragment : BaseFragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val arguments: ProductsFragmentArgs by navArgs()
    private val productsViewModel: ProductsViewModel by viewModels()
    private var productsAdapter = ProductsAdapter { position -> onProductClicked(position) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        initToolbar()
        initRecyclerView()
        initProductsObserve()
        initRefresh()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
    }

    private fun initToolbar() {
        (activity as MainActivity).supportActionBar?.title = arguments.categoryName
    }

    private fun initRefresh() {
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            initProductsObserve()
        }
    }

    private fun initProductsObserve() {

        productsViewModel.productList.observe(viewLifecycleOwner) {
            productsAdapter.differ.submitList(it)
        }

        productsViewModel.errorMessage.observe(viewLifecycleOwner) {
        }

        productsViewModel.loading.observe(viewLifecycleOwner) {
            hideProgressBar(it)
        }

        productsViewModel.getAllProducts(arguments.categoryId, getUserToken())
    }

    private fun initRecyclerView() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun hideProgressBar(it: Boolean) {
        binding.progressBar.isVisible = it
        binding.refresh.isVisible = !it
    }

    private fun onProductClicked(position: Int) {
        val productId = productsAdapter.differ.currentList[position].id
        val productPrice = productsAdapter.differ.currentList[position].price
        val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(
            productId,
            productPrice
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigation()
        _binding = null
    }
}