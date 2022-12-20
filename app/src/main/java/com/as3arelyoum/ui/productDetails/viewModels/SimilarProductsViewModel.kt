package com.as3arelyoum.ui.productDetails.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.*

class SimilarProductsViewModel : ViewModel() {
    private val repository = AssarRepository()
    var job: Job? = null
    val similarProductList = MutableLiveData<List<ProductDTO>>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getSimilarProducts(product_id: Int, device_id: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getSimilarProducts(product_id, device_id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    similarProductList.postValue(response.body())
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
