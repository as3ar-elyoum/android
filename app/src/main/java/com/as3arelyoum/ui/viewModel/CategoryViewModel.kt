package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.as3arelyoum.data.resources.category.Repo
import com.as3arelyoum.data.resources.status.Resource

class CategoryViewModel(private val categoriesRepo: Repo) : ViewModel(){
    fun getAllPosts() = liveData {
        emit(Resource.loading(null))
        try{
            emit(Resource.success(categoriesRepo.getAllCategories()))
        } catch (e:Exception){
            emit(Resource.error(null,e.message.toString()))
        }
    }
}