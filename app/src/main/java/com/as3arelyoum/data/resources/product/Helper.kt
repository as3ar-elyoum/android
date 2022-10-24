package com.as3arelyoum.data.resources.product

import com.as3arelyoum.data.api.ProductApi

class Helper(private val apiInterface: ProductApi) {
    suspend fun getAllProducts(category_id:Int) = apiInterface.getAllProducts(category_id)
}