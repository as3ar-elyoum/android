package com.as3arelyoum.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.*

class SearchViewModel : ViewModel() {
    private val repository = AssarRepository()
    var job: Job? = null
    private val errorMessage = MutableLiveData<String>()
    val searchList = MutableLiveData<List<ProductDTO>>()
    val loading = MutableLiveData<Boolean>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun search(query: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.search(query)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    searchList.postValue(response.body())
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

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}