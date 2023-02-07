package com.as3arelyoum.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.databinding.FragmentHomeBinding
import com.as3arelyoum.ui.main.BaseFragment

class HomeFragment : BaseFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val homeAdapter =
        HomeAdapter { position1, position2 -> onProductClicked(position1, position2) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().title = getString(R.string.home)
        loadHomeData()
        setUpRefresh()
        return binding.root
    }

    private fun setUpRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            loadHomeData()
        }
    }

    private fun loadHomeData() {
        homeViewModel.categoriesList.observe(viewLifecycleOwner) { category ->
            homeAdapter.setCategories(category)
            binding.progressBar.isVisible = false
            setUpRecyclerView()
        }
        homeViewModel.getHomeData(getUserToken())
    }

    private fun setUpRecyclerView() {
        binding.apply {
            mainRecyclerView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = homeAdapter
            }
        }
    }

    private fun onProductClicked(listPosition: Int, productPosition: Int) {
        val product = homeAdapter.categoriesList[listPosition].products[productPosition]
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