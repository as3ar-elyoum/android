package com.as3arelyoum.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.databinding.ActivitySearchBinding
import com.as3arelyoum.ui.adapters.SearchAdapter
import com.as3arelyoum.ui.viewModels.SearchViewModel
import com.as3arelyoum.utils.helper.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private var job: Job? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter { position -> onProductClicked(position) }
    private val deviceId: String by lazy { Constants.getDeviceId(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        initSearch()

        if (binding.searchInput.requestFocus()) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.searchInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.rippleBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initSearch() {
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            if (text!!.length > 3) {
                job?.cancel()
                job = MainScope().launch {
                    searchViewModel.search(text.toString(), deviceId)
                    searchViewModel.searchList.observe(this@SearchActivity) { productList ->
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
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = searchAdapter.differ.currentList[position].id
        val productPrice = searchAdapter.differ.currentList[position].price
        val intent = Intent(this, ProductDetailsActivity::class.java)
        intent.putExtra("productId", productId)
        intent.putExtra("productPrice", productPrice)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}