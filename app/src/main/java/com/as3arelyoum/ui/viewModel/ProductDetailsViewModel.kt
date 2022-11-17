package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.network.RetrofitInstance
import com.as3arelyoum.utils.status.Resource

class ProductDetailsViewModel : ViewModel() {
    fun getProductDetails(product_id: Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(RetrofitInstance.productDetailsApi.getProductDetails(product_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }

//    fun updateProductDetails(product_id: Int) = liveData {
//        emit(Resource.loading(null))
//        try {
//            emit(Resource.success(RetrofitInstance.productDetailsApi.updateProductDetails(product_id)))
//        } catch (e: Exception) {
//            emit(Resource.error(null, e.message.toString()))
//        }
//    }
}
