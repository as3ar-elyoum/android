package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel

@Suppress("UNCHECKED_CAST")
class ProductDetailsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}