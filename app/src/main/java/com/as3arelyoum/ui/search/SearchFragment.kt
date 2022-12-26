package com.as3arelyoum.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.databinding.FragmentSearchBinding
import com.as3arelyoum.ui.main.BaseFragment
import com.as3arelyoum.utils.helper.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment() {
    private var _binding: FragmentSearchBinding? = null
    private var job: Job? = null
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
        search()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        hideToolbar()
        showKeyboard()
        binding.rippleBack.setOnClickListener { findNavController().navigateUp() }
    }

    private fun showKeyboard() {
        if (binding.searchInput.requestFocus()) {
            val imm =
                requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchInput, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun search() {
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            if (text!!.length > 3) {
                job?.cancel()
                job = MainScope().launch {
                    searchViewModel.search(text.toString(), deviceId)
                    searchViewModel.searchList.observe(viewLifecycleOwner) { productList ->
                        searchAdapter.differ.submitList(productList)
                    }
                }
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
        showBottomNavigation()
        showToolbar()
        _binding = null
    }
}