package com.as3arelyoum.ui.home

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
    val categoriesList = MutableLiveData<List<CategoryDTO>>()
    val productsList = MutableLiveData<List<ProductDTO>>()

    private val userData = MutableLiveData<UserInfoDTO>()

    fun getHomeData() {
        viewModelScope.launch {
            coroutineScope {
                val categoryResponse = repository.getAllCategories()
                val productsResponse = async { repository.getAllProducts() }

                if (categoryResponse.isSuccessful) {
                    categoriesList.value = categoryResponse.body()
                    productsResponse.await().let {
                        if (it.isSuccessful) {
                            productsList.value = it.body()
                        }
                    }
                }
            }
        }
    }


    fun getCategoriesSpinner() {
        viewModelScope.launch {
            coroutineScope {
                val categoryResponse = repository.getAllCategories()
                if (categoryResponse.isSuccessful) {
                    categoriesList.value = categoryResponse.body()
                }
            }
        }
    }

    fun sendDevice(userInfoDTO: UserInfoDTO, deviceId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.sendDevice(userInfoDTO, deviceId)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userData.postValue(response.body())
                }
            }
        }
    }
}