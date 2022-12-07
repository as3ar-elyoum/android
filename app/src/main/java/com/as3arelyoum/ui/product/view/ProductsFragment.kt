package com.as3arelyoum.ui.product.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.databinding.FragmentProductsBinding
import com.as3arelyoum.ui.product.adapter.ProductsAdapter
import com.as3arelyoum.ui.product.viewmodel.ProductsViewModel
import com.as3arelyoum.utils.status.Status

class ProductsFragment : Fragment() {
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView()
        initProductsObserve()
        initRefresh()
    }

    private fun initToolbar() {
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.apply {
            requireActivity().title = arguments.category.name
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_ios_back)
        }
    }

    private fun initRefresh() {
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            initProductsObserve()
        }
    }

    private fun initProductsObserve() {
        productsViewModel.getAllProducts(arguments.category.id).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { productsList ->
                        productsAdapter.differ.submitList(productsList)
                    }
                }
                Status.FAILURE -> {
                }
                Status.LOADING -> {
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = productsAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
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
        _binding = null
    }
}