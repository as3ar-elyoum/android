package com.as3arelyoum.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    var productsLists: MutableList<List<ProductDTO>> = ArrayList()
    var productList: List<ProductDTO> = ArrayList()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getProducts(categoryId: Int, deviceId: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getAllProducts(categoryId, deviceId)
            if (response.isSuccessful) {
                productList = ArrayList(response.body())
                productsLists.add(productList)

                loading.postValue(false)
            } else {
                onError("Error: ${response.message()}")
            }
        }
    }

    private fun onError(message: String) {
        loading.postValue(false)
    }
}
