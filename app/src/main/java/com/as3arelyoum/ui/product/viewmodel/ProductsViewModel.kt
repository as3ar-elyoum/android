package com.as3arelyoum.ui.product.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.utils.status.Resource

class ProductsViewModel : ViewModel() {
    private val repository = AssarRepository()

    fun getAllProducts(category_id: Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getAllProducts(category_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }
}
