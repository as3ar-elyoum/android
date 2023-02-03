package com.as3arelyoum.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.databinding.FragmentCategoriesBinding
import com.as3arelyoum.ui.main.BaseFragment
import com.as3arelyoum.utils.helper.PrefUtil

class CategoriesFragment : BaseFragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val categoryAdapter = CategoryAdapter { position -> onCategoryClicked(position) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        PrefUtil.initPrefUtil(requireContext())
        initRecyclerView()
        initCategoryObserve()
        initRefresh()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.categories)
        sendUserToApi()
    }

    private fun initRefresh() {
        binding.refresh.setOnRefreshListener {
            binding.refresh.isRefreshing = false
            initCategoryObserve()
            sendUserToApi()
        }
    }

    @SuppressLint("HardwareIds")
    private fun sendUserToApi() {
        val userInfoDTO = UserInfoDTO(getUserToken())
        categoryViewModel.sendDevice(userInfoDTO, getUserToken())
    }

    private fun initCategoryObserve() {
        categoryViewModel.categoriesList.observe(viewLifecycleOwner) {
            categoryAdapter.differ.submitList(it)
        }

        categoryViewModel.errorMessage.observe(viewLifecycleOwner) {}

        categoryViewModel.loading.observe(viewLifecycleOwner) {
            hideProgressBar(it)
        }

        categoryViewModel.getAllCategories()
    }

    private fun hideProgressBar(it: Boolean) {
        binding.progressBar.isVisible = it
        binding.refresh.isVisible = !it
    }

    private fun initRecyclerView() {
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun onCategoryClicked(position: Int) {
        val category = categoryAdapter.differ.currentList[position]
        val action =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(category.id, category.name)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}