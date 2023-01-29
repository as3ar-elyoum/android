package com.as3arelyoum.ui.product.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.repository.AssarRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.*

class ProductDetailsViewModel : ViewModel() {
    private val repository = AssarRepository()
    var job: Job? = null
    val productDetails = MutableLiveData<ProductDTO>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getProductDetails(product_id: Int, fcm_token: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getProductDetails(product_id, fcm_token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productDetails.postValue(response.body())
                    loading.postValue(false)
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun updateProductDetails(product_id: Int, params: JsonObject, fcm_token: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.updateProductDetails(product_id, params, fcm_token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    productDetails.postValue(response.body())
                    loading.postValue(false)
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
