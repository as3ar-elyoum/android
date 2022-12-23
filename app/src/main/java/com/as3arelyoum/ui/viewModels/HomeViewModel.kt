package com.as3arelyoum.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.models.Category
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.data.models.User
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val userData = MutableLiveData<User>()
    val categoryList = MutableLiveData<List<Category>>()
    val productList = MutableLiveData<List<Product>>()
    val errorMessage = MutableLiveData<String>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun fetchCategoryData(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = async { repository.getAllCategories(deviceId) }
            withContext(Dispatchers.Main) {
                if (response.await().isSuccessful) {
                    categoryList.postValue(response.await().body())
                } else {
                    onError("Error: ${response.await().message()}")
                }
            }
        }
    }

    fun getSpecificCategoryData(categoryId: Int?, deviceId: String){
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = async { repository.getCategoryProducts(categoryId, deviceId) }
            withContext(Dispatchers.Main) {
                if (response.await().isSuccessful) {
                    productList.postValue(response.await().body())
                } else {
                    onError("Error: ${response.await().message()}")
                }
            }
        }
    }

    fun sendDevice(user: User) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = repository.sendDevice(user)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userData.postValue(response.body())
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.postValue(message)
    }
}