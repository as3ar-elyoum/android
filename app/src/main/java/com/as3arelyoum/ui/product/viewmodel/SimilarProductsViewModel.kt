package com.as3arelyoum.ui.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.utils.status.Resource

class SimilarProductsViewModel : ViewModel() {
    private val repository = AssarRepository()

    fun getSimilarProducts(product_id: Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getSimilarProducts(product_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }
}
