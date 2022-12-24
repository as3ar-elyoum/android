package com.as3arelyoum.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = AssarRepository()
    private val _searchList = MutableLiveData<List<Product>>()
    val searchList: LiveData<List<Product>> = _searchList

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun search(query: String, device_id: String) {
        viewModelScope.launch {
            try {
                _searchList.postValue(
                    repository.search(query, device_id).body()
                )
                _loading.postValue(false)
            } catch (e: Exception) {
                onError(e.message ?: "Error while fetching data")
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.postValue(message)
        _loading.postValue(false)
    }
}