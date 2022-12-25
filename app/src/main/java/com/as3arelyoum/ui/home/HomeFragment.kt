package com.as3arelyoum.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.databinding.FragmentHomeBinding
import com.as3arelyoum.utils.helper.Constants
import com.as3arelyoum.utils.helper.PrefUtil

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val deviceId: String by lazy { Constants.getDeviceId(requireContext()) }
    private val homeAdapter = HomeAdapter { position -> onProductClicked(position) }
    private val laptopsAdapter = LaptopsAdapter { position -> onProductClicked(position) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        requireActivity().title = "الرئيسية"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        PrefUtil.initPrefUtil(requireContext())
        setUpRecyclerView()
    }

    private fun setUpViewModel() {
        homeViewModel.mobileList.observe(viewLifecycleOwner) {
            homeAdapter.setProducts(it)
        }

        homeViewModel.laptopList.observe(viewLifecycleOwner) {
            laptopsAdapter.setProducts(it)
        }
    }

    private fun setUpRecyclerView() {
        binding.apply {
            mobilesRv.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = homeAdapter
            }
            homeViewModel.getMobiles(deviceId)
        }

        binding.apply {
            laptopsRv.apply {
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = laptopsAdapter
            }
            homeViewModel.getLaptops(deviceId)
        }
    }

    private fun onProductClicked(position: Int) {
        val product = homeAdapter.productList[position]
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