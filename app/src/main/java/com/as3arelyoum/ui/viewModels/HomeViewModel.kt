package com.as3arelyoum.ui.viewModels

import androidx.lifecycle.LiveData
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

    private val _categoryList = MutableLiveData<List<Category>>()
    val categoryList: LiveData<List<Category>> get() = _categoryList
    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = _productList

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchCategoryData(deviceId: String) {
        viewModelScope.launch {
            try {
                _categoryList.postValue(repository.getAllCategories(deviceId).body())
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun getSpecificCategoryData(categoryId: Int?, deviceId: String) {
        viewModelScope.launch {
            try {
                _productList.postValue(repository.getCategoryProducts(categoryId, deviceId).body())
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun sendDevice(user: User) {
        viewModelScope.launch {
            try {
                userData.postValue(repository.sendDevice(user).body())
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }
}