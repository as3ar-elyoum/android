package com.as3arelyoum.ui.home.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val userData = MutableLiveData<UserInfoDTO>()
    val categoryList = MutableLiveData<List<CategoryDTO>>()
    val productList = MutableLiveData<List<ProductDTO>>()
    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun fetchCategoryData(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = async { repository.getAllCategories(deviceId) }
            withContext(Dispatchers.Main) {
                if (response.await().isSuccessful) {
                    categoryList.postValue(response.await().body())
                    loading.postValue(false)
                } else {
                    onError("Error: ${response.await().message()}")
                }
            }
        }
    }

    fun loadProducts(categoryId: Int?, deviceId: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = async { repository.getCategoryProducts(categoryId, deviceId) }
            withContext(Dispatchers.Main) {
                if (response.await().isSuccessful) {
                    Log.d("LOADING...", "Loaded 2")
                    productList.postValue(response.await().body())
//                    loading.postValue(false)
                } else {
                    onError("Error: ${response.await().message()}")
                }
            }
        }
    }

    fun sendDevice(userInfoDTO: UserInfoDTO) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val response = repository.sendDevice(userInfoDTO)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userData.postValue(response.body())
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
}