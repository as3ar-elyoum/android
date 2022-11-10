package com.as3arelyoum.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.as3arelyoum.R
import com.as3arelyoum.data.model.UserInfo
import com.as3arelyoum.databinding.FragmentCategoriesBinding
import com.as3arelyoum.ui.adapter.CategoryAdapter
import com.as3arelyoum.ui.factory.CategoryViewModelFactory
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.ui.viewModel.CategoryViewModel
import com.as3arelyoum.utils.PrefUtil
import com.as3arelyoum.utils.status.Status

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryViewModel: CategoryViewModel
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
        initRepository()
        initCategoryObserve()
        addUserToApi()
    }

    @SuppressLint("HardwareIds")
    private fun addUserToApi() {
        val deviceId =
            Settings.Secure.getString(activity?.contentResolver, Settings.Secure.ANDROID_ID)
        val userToken = PrefUtil.getData("token")
        val userInfo = UserInfo(deviceId, userToken)

        if (userToken.isNotEmpty()) {
            categoryViewModel.addDevice(userInfo).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.SUCCESS -> {}
                    Status.FAILURE -> {}
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun initCategoryObserve() {
        categoryViewModel.getAllCategories().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { categoryList ->
                        categoryAdapter.differ.submitList(categoryList)
                    }
                }
                Status.FAILURE -> {}
                Status.LOADING -> {}
            }
        }
    }

    private fun initRepository() {
        val categoryRepository = CategoryRepository()
        categoryViewModel = ViewModelProvider(
            this,
            CategoryViewModelFactory(categoryRepository)
        )[CategoryViewModel::class.java]
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