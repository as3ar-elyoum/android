package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.model.SearchResponse
import com.as3arelyoum.ui.repositories.SearchRepository
import kotlinx.coroutines.*

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    var job: Job? = null
    val errorMessage = MutableLiveData<String>()
    val searchList = MutableLiveData<SearchResponse>()
    val loading = MutableLiveData<Boolean>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun search(query: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = searchRepository.search(query)
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