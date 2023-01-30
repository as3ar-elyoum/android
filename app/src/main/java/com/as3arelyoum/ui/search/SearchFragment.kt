package com.as3arelyoum.ui.search

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.databinding.FragmentSearchBinding
import com.as3arelyoum.ui.main.BaseFragment
import com.as3arelyoum.utils.helper.Constants

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private val deviceId: String by lazy { Constants.getDeviceId(requireContext()) }
    private var searchAdapter = SearchAdapter { position -> onProductClicked(position) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        initRecyclerView()
        searchProducts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
    }

    private fun showKeyboard() {
        val imm =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.search, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun searchProducts() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                if (!TextUtils.isEmpty(query)) {
                    showProgressBar()
                    performSearch(query)
                }else{
                    searchAdapter.differ.submitList(null)
                    hideProgressBar()
                }
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })
    }

    private fun performSearch(query: String?) {
        if (query!!.length > 3) {
            searchViewModel.search(query, deviceId)
            searchViewModel.searchList.observe(viewLifecycleOwner) { productList ->
                searchAdapter.differ.submitList(productList)
                hideProgressBar()
            }
        }
    }

    private fun initRecyclerView() {
        binding.searchRecyclerview.apply {
            setHasFixedSize(true)
            adapter = searchAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun showProgressBar() {
        binding.apply {
            searchProgressbar.isVisible = true
            searchRecyclerview.isVisible = false
        }
    }

    private fun hideProgressBar() {
        binding.apply {
            searchProgressbar.isVisible = false
            searchRecyclerview.isVisible = true
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = searchAdapter.differ.currentList[position].id
        val productPrice = searchAdapter.differ.currentList[position].price

        val action = SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(
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