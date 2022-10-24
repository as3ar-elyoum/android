package com.as3arelyoum.data.resources.product

import com.as3arelyoum.data.api.ProductApi

class Helper(private val apiInterface: ProductApi) {
    suspend fun getAllProducts(getAllProducts: Int) = apiInterface.getAllProducts(getAllProducts)
}