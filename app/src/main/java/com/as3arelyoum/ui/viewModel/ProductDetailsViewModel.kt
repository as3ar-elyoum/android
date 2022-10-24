package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.resources.productDetails.Repo
import com.as3arelyoum.data.resources.status.Resource

class ProductDetailsViewModel(private val productDetails: Repo) : ViewModel() {
    fun getProduct(product_id: Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(productDetails.getProductDetails(product_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }
}
