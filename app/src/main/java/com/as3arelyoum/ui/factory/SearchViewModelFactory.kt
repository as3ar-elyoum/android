package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.ui.repositories.SearchRepository
import com.as3arelyoum.ui.viewModel.SearchViewModel

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val searchRepository: SearchRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchRepository) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}