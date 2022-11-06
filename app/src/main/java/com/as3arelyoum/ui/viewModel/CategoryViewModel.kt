package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.model.UserInfo
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.utils.status.Resource

class CategoryViewModel(private val categoriesRepo: CategoryRepository) : ViewModel(){
    fun getAllCategories() = liveData {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(categoriesRepo.getAllCategories()))
        } catch (e:Exception){
            emit(Resource.error(null,e.message.toString()))
        }
    }

    fun addDevice(userInfo: UserInfo) = liveData {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(categoriesRepo.addDevice(userInfo)))
        } catch (e:Exception){
            emit(Resource.error(null,e.message.toString()))
        }
    }
}