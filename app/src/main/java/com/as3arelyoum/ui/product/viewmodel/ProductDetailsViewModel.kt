package com.as3arelyoum.ui.product.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.utils.status.Resource
import com.google.gson.JsonObject

class ProductDetailsViewModel : ViewModel() {
    private val repository = AssarRepository()

    fun getProductDetails(product_id: Int) = liveData {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(repository.getProductDetails(product_id)))
        } catch (e: Exception) {
            emit(Resource.error(null, e.message.toString()))
        }
    }

    fun updateProductDetails(product_id: Int, params: JsonObject) = liveData {
        emit(Resource.loading(null))
        try {
            emit(
                Resource.success(
                    repository.updateProductDetails(
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
