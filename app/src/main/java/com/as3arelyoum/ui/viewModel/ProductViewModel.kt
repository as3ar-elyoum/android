package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.resources.product.Repo
import com.as3arelyoum.data.resources.status.Resource

class ProductViewModel(private val productsRepo: Repo) : ViewModel() {
    fun getAllProducts(category_id:Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(productsRepo.getAllProducts(category_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }
}