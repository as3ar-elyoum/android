package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.data.resources.product.Helper
import com.as3arelyoum.data.resources.product.Repo
import com.as3arelyoum.ui.viewModel.ProductViewModel

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(private val apiHelper: Helper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(Repo(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}