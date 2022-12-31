package com.as3arelyoum.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.databinding.FragmentHomeBinding
import com.as3arelyoum.ui.category.CategoryViewModel
import com.as3arelyoum.ui.main.BaseFragment
import com.as3arelyoum.utils.helper.Constants

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val deviceId: String by lazy { Constants.getDeviceId(requireContext()) }
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var categories: List<CategoryDTO>? = null
    private val homeAdapter =
        HomeAdapter { position1, position2 -> onProductClicked(position1, position2) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().title = getString(R.string.home)
        loadCategories()
        loadProducts()
        setUpRefresh()
        return binding.root
    }

    private fun setUpRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            loadCategories()
            loadProducts()
        }
    }

    private fun loadCategories() {
        categoryViewModel.categoriesList.observe(viewLifecycleOwner) {
            categories = it
            homeAdapter.setCategories(categories!!)
            setUpRecyclerView()
        }
        if (categories.isNullOrEmpty()) {
            categoryViewModel.getAllCategories()
        }
    }

    private fun loadProducts() {
        homeViewModel.loading.observe(viewLifecycleOwner) {
            homeAdapter.setProductsLists(homeViewModel.productsLists)
            binding.progressBar.isVisible = it
        }

        homeViewModel.failure.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "لا يوجد إنترنت", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecyclerView() {
        binding.apply {
            mainRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = homeAdapter
            }
            categories!!.forEach { category ->
                if (homeViewModel.productsLists.isEmpty()) {
                    homeViewModel.getProducts(category.id, deviceId)
                }
            }
        }
    }

    private fun onProductClicked(listPosition: Int, productPosition: Int) {
        val product = homeAdapter.productLists[listPosition][productPosition]
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(
            product.id,
            product.price,
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}