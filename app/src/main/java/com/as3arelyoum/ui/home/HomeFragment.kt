package com.as3arelyoum.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.databinding.FragmentHomeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val categoryAdapter = CategoriesAdapter { position -> onCategoryClicked(position) }
    private val productsAdapter = ProductsAdapter { position -> onProductClicked(position) }
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        initRecyclerView()
        initSearchFragment()
        initHomeDataObserve()
        return binding.root
    }

    private fun initSearchFragment() {
        binding.searchView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    private fun initHomeDataObserve() {
        job = MainScope().launch {
            homeViewModel.apply {
                categoriesList.observe(viewLifecycleOwner) { categories ->
                    categoryAdapter.differ.submitList(categories)
                }
                delay(500)
                productsList.observe(viewLifecycleOwner) { products ->
                    productsAdapter.differ.submitList(products)
                    hideProgressBar()
                }
                getHomeData()
            }
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            progressBar.isVisible = false
            nestedContent.isVisible = true
        }
    }

//    private fun noInternetConnection() {
//        binding.noInternetConnection.isVisible = true
//        binding.nestedContent.isVisible = false
//    }

    private fun initRecyclerView() {
        binding.categoriesRv.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        binding.productsRv.apply {
            setHasFixedSize(true)
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun onCategoryClicked(position: Int) {
        val category = categoryAdapter.differ.currentList[position]
        val action = HomeFragmentDirections.actionHomeFragmentToProductsFragment(category)
        findNavController().navigate(action)
    }

    private fun onProductClicked(position: Int) {
        val product = productsAdapter.differ.currentList[position]
        Toast.makeText(requireContext(), product.name, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}