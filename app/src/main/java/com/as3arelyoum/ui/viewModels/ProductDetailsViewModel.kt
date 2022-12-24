package com.as3arelyoum.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.data.repository.AssarRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class ProductDetailsViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val _productDetails = MutableLiveData<Product>()
    val productDetails: LiveData<Product> get() = _productDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getProductDetails(product_id: Int, device_id: String) {
        viewModelScope.launch {
            try {
                _productDetails.postValue(
                    repository.getProductDetails(product_id, device_id).body()
                )
                _loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun updateProductDetails(product_id: Int, params: JsonObject, deviceId: String) {
        viewModelScope.launch {
            try {
                _productDetails.postValue(
                    repository.updateProductDetails(product_id, params, deviceId).body()
                )
                _loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }
}
