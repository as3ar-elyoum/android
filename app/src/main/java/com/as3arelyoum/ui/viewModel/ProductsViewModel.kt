package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.utils.status.Resource
import com.as3arelyoum.ui.repositories.ProductsRepository

class ProductsViewModel(private val productsRepo: ProductsRepository) : ViewModel() {
    fun getAllProducts(category_id:Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(productsRepo.getAllProducts(category_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }
}
