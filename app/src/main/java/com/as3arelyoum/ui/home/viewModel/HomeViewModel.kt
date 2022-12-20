package com.as3arelyoum.ui.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val userData = MutableLiveData<UserInfoDTO>()

    suspend fun fetchCategoryData(deviceId: String): List<CategoryDTO> {
        return withContext(Dispatchers.IO) {
            val categories = async { repository.getAllCategories(deviceId) }
            if (categories.await().isSuccessful) {
                categories.await().body()!!
            } else {
                emptyList()
            }
        }
    }

    suspend fun getSpecificCategoryData(categoryId: Int, deviceId: String): List<ProductDTO> {
        return withContext(Dispatchers.IO) {
            val products = async { repository.getCategoryProducts(categoryId, deviceId) }
            if (products.await().isSuccessful) {
                products.await().body()!!
            } else {
                emptyList()
            }
        }
    }

    fun sendDevice(userInfoDTO: UserInfoDTO) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.sendDevice(userInfoDTO)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userData.postValue(response.body())
                }
            }
        }
    }
}