package com.as3arelyoum.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.launch

class SimilarProductsViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val _similarProductList = MutableLiveData<List<Product>>()
    val similarProductList: LiveData<List<Product>> get() = _similarProductList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getSimilarProducts(product_id: Int, device_id: String) {
        viewModelScope.launch {
            try {
                _similarProductList.postValue(
                    repository.getSimilarProducts(product_id, device_id).body()
                )
                _loading.postValue(false)
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }
}
