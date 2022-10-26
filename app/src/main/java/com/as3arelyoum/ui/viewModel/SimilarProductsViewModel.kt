package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.ui.repositories.SimilarProductsRepository
import com.as3arelyoum.utils.status.Resource

class SimilarProductsViewModel(private val similarProductsRepo: SimilarProductsRepository) : ViewModel() {
    fun getSimilarProducts(product_id:Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(similarProductsRepo.getSimilarProducts(product_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }
}
