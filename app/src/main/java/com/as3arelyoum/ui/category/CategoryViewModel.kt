package com.as3arelyoum.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.repository.AssarRepository
import kotlinx.coroutines.*

class CategoryViewModel : ViewModel() {

    private val repository = AssarRepository()
    var job: Job? = null

    val categoriesList = MutableLiveData<List<CategoryDTO>>()
    private val userData = MutableLiveData<UserInfoDTO>()

    val errorMessage = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getAllCategories() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = repository.getAllCategories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    categoriesList.postValue(response.body())
                    loading.postValue(false)
                } else {
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun sendDevice(userInfoDTO: UserInfoDTO) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
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

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}