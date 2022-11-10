package com.as3arelyoum.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.databinding.FragmentProductsBinding
import com.as3arelyoum.ui.adapter.ProductsAdapter
import com.as3arelyoum.ui.factory.ProductsViewModelFactory
import com.as3arelyoum.ui.repositories.ProductsRepository
import com.as3arelyoum.ui.viewModel.ProductsViewModel
import com.as3arelyoum.utils.status.Status

class ProductsFragment : Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var productsAdapter: ProductsAdapter
    private val arguments: ProductsFragmentArgs by navArgs()

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
        initRepository()
        initProductsObserve()
    }

    private fun initToolbar(){
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.apply {
            requireActivity().title = arguments.categoryName
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_ios_back)
        }
    }

    private fun initRepository() {
        val productsRepository = ProductsRepository()
        productsViewModel = ViewModelProvider(
            this,
            ProductsViewModelFactory(productsRepository)
        )[ProductsViewModel::class.java]
    }

    private fun initProductsObserve() {
        productsViewModel.getAllProducts(arguments.categoryId).observe(viewLifecycleOwner) {
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
            productsAdapter = ProductsAdapter { position -> onProductClicked(position) }
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