package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.data.resources.productDetails.Helper
import com.as3arelyoum.data.resources.productDetails.Repo
import com.as3arelyoum.ui.viewModel.ProductDetailsViewModel

@Suppress("UNCHECKED_CAST")
class ProductDetailsViewModelFactory(private val apiHelper: Helper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            return ProductDetailsViewModel(Repo(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}