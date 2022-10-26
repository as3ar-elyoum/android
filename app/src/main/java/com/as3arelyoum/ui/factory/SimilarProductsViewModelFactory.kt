package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.ui.repositories.SimilarProductsRepository
import com.as3arelyoum.ui.viewModel.SimilarProductsViewModel

@Suppress("UNCHECKED_CAST")
class SimilarProductsViewModelFactory(private val similarProductRepo: SimilarProductsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimilarProductsViewModel::class.java)) {
            return SimilarProductsViewModel(similarProductRepo) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}