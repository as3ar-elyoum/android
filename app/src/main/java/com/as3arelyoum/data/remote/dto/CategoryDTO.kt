package com.as3arelyoum.data.remote.dto

data class CategoryDTO(
    var id: Int,
    var name: String,
    var icon: String,
    var products: List<ProductDTO>
)
