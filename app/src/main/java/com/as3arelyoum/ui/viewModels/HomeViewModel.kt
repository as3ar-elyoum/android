package com.as3arelyoum.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.models.Category
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.data.models.User
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val userData = MutableLiveData<User>()
    val categoryList = MutableLiveData<List<Category>>()
    val productList = MutableLiveData<List<Product>>()
    val errorMessage = MutableLiveData<String>()

    fun fetchCategoryData(deviceId: String) {
        viewModelScope.launch {
            try {
                categoryList.postValue(repository.getAllCategories(deviceId).body())
            } catch (e: Exception) {
                errorMessage.postValue(e.message)
            }
        }
    }

    fun getSpecificCategoryData(categoryId: Int?, deviceId: String) {
        viewModelScope.launch {
            try {
                productList.postValue(repository.getCategoryProducts(categoryId, deviceId).body())
            } catch (e: Exception) {
                errorMessage.postValue(e.message)
            }
        }
    }

    fun sendDevice(user: User) {
        viewModelScope.launch {
            try {
                userData.postValue(repository.sendDevice(user).body())
            } catch (e: Exception) {
                errorMessage.postValue(e.message)
            }
        }
    }
}