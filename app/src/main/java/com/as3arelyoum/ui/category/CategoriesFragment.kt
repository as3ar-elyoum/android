package com.as3arelyoum.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.databinding.FragmentCategoriesBinding
import com.as3arelyoum.ui.product.adapter.ProductsAdapter
import com.as3arelyoum.utils.helper.Constants.getDeviceId
import com.as3arelyoum.utils.helper.PrefUtil

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val categoryAdapter = CategoryAdapter { position -> onCategoryClicked(position) }
    private val deviceId: String by lazy { getDeviceId(requireContext()) }
    private var productsAdapter =
        ProductsAdapter { position -> Log.d("Clicked", position.toString()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        requireActivity().title = getString(R.string.app_name)
        PrefUtil.initPrefUtil(requireContext())
        initRecyclerView()
        initCategoryObserve()
        initRefresh()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendUserToApi()

        Log.d("TAG", "onViewCreated: ${deviceId}")
    }

    private fun initRefresh() {
//        binding.refresh.setOnRefreshListener {
//            binding.refresh.isRefreshing = false
//            initCategoryObserve()
//            sendUserToApi()
//        }
    }

    @SuppressLint("HardwareIds")
    private fun sendUserToApi() {
        val userToken = PrefUtil.getData("token")
        val userInfoDTO = UserInfoDTO(deviceId, userToken)
        if (userToken.isNotEmpty()) {
            categoryViewModel.sendDevice(userInfoDTO, deviceId)
        }
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
        binding.categoriesProgress.isVisible = it
        binding.categoriesRv.isVisible = !it
    }

    private fun initRecyclerView() {
        binding.categoriesRv.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun onCategoryClicked(position: Int) {
        val category = categoryAdapter.differ.currentList[position]
        val action =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(category)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}