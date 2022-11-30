package com.as3arelyoum.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.databinding.FragmentCategoriesBinding
import com.as3arelyoum.utils.helper.PrefUtil
import com.google.android.material.snackbar.Snackbar

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoryViewModel: CategoryViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        requireActivity().title = getString(R.string.app_name)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrefUtil.initPrefUtil(requireContext())
        initRecyclerView()
        initCategoryObserve()
        initRefresh()
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
        val deviceId =
            Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
        val userToken = PrefUtil.getData("token")
        val userInfoDTO = UserInfoDTO(deviceId, userToken)
        if (userToken.isNotEmpty()) {
            categoryViewModel.sendDevice(userInfoDTO)
        }
    }

    private fun initCategoryObserve() {
        categoryViewModel.categoriesList.observe(viewLifecycleOwner) {
            categoryAdapter.differ.submitList(it)
        }

        categoryViewModel.errorMessage.observe(viewLifecycleOwner) {
            Snackbar.make(binding.progressBar, "No Internet Connection", Snackbar.LENGTH_LONG)
                .show()
        }

        categoryViewModel.loading.observe(viewLifecycleOwner) {
            hideProgressBar(it)
        }

        categoryViewModel.getAllCategories()
    }

    private fun hideProgressBar(it: Boolean) {
        binding.progressBar.isVisible = it
        binding.nestedContent.isVisible = !it
    }

    private fun initRecyclerView() {
        categoryAdapter = CategoryAdapter { position -> onCategoryClicked(position) }
        binding.recyclerview.apply {
            setHasFixedSize(true)
            adapter = categoryAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun onCategoryClicked(position: Int) {
        val categoryId = categoryAdapter.differ.currentList[position].id
        val categoryName = categoryAdapter.differ.currentList[position].name

        val firmsFragment =
            CategoriesFragmentDirections.actionCategoriesFragmentToProductsFragment(
                categoryId,
                categoryName,
            )
        findNavController().navigate(firmsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}