package com.as3arelyoum.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.as3arelyoum.databinding.ActivitySearchBinding
import com.as3arelyoum.ui.adapter.SearchAdapter
import com.as3arelyoum.ui.factory.SearchViewModelFactory
import com.as3arelyoum.ui.repositories.SearchRepository
import com.as3arelyoum.ui.viewModel.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private var job: Job? = null
    private val binding get() = _binding!!
    private val searchRepository = SearchRepository()
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            searchRepository
        )
    }
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        search()
        binding.rippleBack.setOnClickListener { finish() }
    }

    private fun search() {
        binding.searchInput.doOnTextChanged { text, _, _, _ ->
            job?.cancel()
            job = MainScope().launch {
                searchViewModel.search(text.toString())
                searchViewModel.searchList.observe(this@SearchActivity) { productList ->

                    searchAdapter.differ.submitList(productList)
                }
            }
        }
    }

//    private fun autoCompleteTextView() {
//        val arrayAdapter = ArrayAdapter(
//            this@SearchActivity,
//            android.R.layout.select_dialog_item,
//            productList.map { it.name })
//        binding.searchInput.threshold = 1
//        binding.searchInput.setAdapter(arrayAdapter)
//    }

    private fun initRecyclerView() {
        binding.searchRecyclerview.apply {
            setHasFixedSize(true)
            searchAdapter = SearchAdapter { position -> onProductClicked(position) }
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }
    }

    private fun onProductClicked(position: Int) {
        val productId = searchAdapter.differ.currentList[position].id
        val productPrice = searchAdapter.differ.currentList[position].price
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}