package com.as3arelyoum.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.network.RetrofitInstance
import com.as3arelyoum.utils.status.Resource
import com.google.gson.JsonObject

class ProductDetailsViewModel : ViewModel() {
    fun getProductDetails(product_id: Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(RetrofitInstance.productDetailsApi.getProductDetails(product_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }

    fun updateProductDetails(product_id: Int, params: JsonObject) = liveData {
        emit(Resource.loading(null))
        try {
            emit(
                Resource.success(
                    RetrofitInstance.productDetailsApi.updateProductDetails(
                        product_id,
                        params
                    )
                )
            )
        } catch (e: Exception) {
            Log.d("Update Product", e.message.toString())
            emit(Resource.error(null, e.message.toString()))
        }
    }
}
