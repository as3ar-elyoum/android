package com.as3arelyoum.domain.usecase.category

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.domain.mapper.category.toCategoryModel
import com.as3arelyoum.domain.model.category.CategoryModel
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val repository: AssarRepository) {
    suspend operator fun invoke(): List<CategoryModel> {
        return repository.getAllCategories().map {
            it.toCategoryModel()
        }
    }
}