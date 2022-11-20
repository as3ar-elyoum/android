package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.data.model.UserInfo
import com.as3arelyoum.ui.repositories.CategoryRepository
import kotlinx.coroutines.*

class CategoryViewModel(private val categoriesRepo: CategoryRepository) : ViewModel() {
    var job: Job? = null
    val errorMessage = MutableLiveData<String>()
    val categoriesList = MutableLiveData<List<Category>>()
    val loading = MutableLiveData<Boolean>()
    private val userData = MutableLiveData<UserInfo>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getAllCategories() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = categoriesRepo.getAllCategories()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    categoriesList.postValue(response.body())
                    loading.postValue(false)
                }else{
                    onError("Error: ${response.message()}")
                }
            }
        }
    }

    fun sendDevice(userInfo: UserInfo) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = categoriesRepo.sendDevice(userInfo)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userData.postValue(response.body())
                    loading.postValue(false)
                }else{
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


//    fun getAllCategories() = liveData {
//        emit(Resource.loading(null))
//        try{
//            emit(Resource.success(categoriesRepo.getAllCategories()))
//        } catch (e:Exception){
//            emit(Resource.error(null,e.message.toString()))
//        }
//    }
//
//    fun addDevice(userInfo: UserInfo) = liveData {
//        emit(Resource.loading(null))
//        try{
//            emit(Resource.success(categoriesRepo.sendDevice(userInfo)))
//        } catch (e:Exception){
//            emit(Resource.error(null,e.message.toString()))
//        }
//    }
}