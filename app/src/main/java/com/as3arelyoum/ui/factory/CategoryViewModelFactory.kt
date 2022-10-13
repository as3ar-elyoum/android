package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.data.resources.category.Helper
import com.as3arelyoum.data.resources.category.Repo
import com.as3arelyoum.ui.viewModel.CategoryViewModel

@Suppress("UNCHECKED_CAST")
class CategoryViewModelFactory(private val apiHelper: Helper) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(Repo(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}