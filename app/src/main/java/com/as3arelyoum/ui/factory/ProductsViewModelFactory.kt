package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.ui.repositories.ProductsRepository
import com.as3arelyoum.ui.viewModel.ProductsViewModel

@Suppress("UNCHECKED_CAST")
class ProductsViewModelFactory(private val productsRepo: ProductsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductsViewModel::class.java)) {
            return ProductsViewModel(productsRepo) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}