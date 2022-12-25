package com.as3arelyoum.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    val mobileList = MutableLiveData<List<ProductDTO>>()
    val laptopList = MutableLiveData<List<ProductDTO>>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getMobiles(deviceId: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getAllProducts(1, deviceId)
            if (response.isSuccessful) {
                mobileList.postValue(response.body())
                loading.postValue(false)
            } else {
                onError("Error: ${response.message()}")
            }
        }
    }

    fun getLaptops(deviceId: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = repository.getAllProducts(16, deviceId)
            if (response.isSuccessful) {
                laptopList.postValue(response.body())
                loading.postValue(false)
            } else {
                onError("Error: ${response.message()}")
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }
}
