package com.as3arelyoum.domain.mapper.category

import com.as3arelyoum.data.remote.response.dto.CategoryDTO
import com.as3arelyoum.domain.model.category.CategoryModel

fun CategoryDTO.toCategoryModel(): CategoryModel {
    return CategoryModel(
        id = this.id,
        name = this.name,
        icon = this.icon
    )
}