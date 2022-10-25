package com.as3arelyoum.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.as3arelyoum.ui.repositories.CategoryRepository
import com.as3arelyoum.ui.viewModel.CategoryViewModel

@Suppress("UNCHECKED_CAST")
class CategoryViewModelFactory(private val apiHelper: CategoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(apiHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}