package com.as3arelyoum.data.resources.productDetails

import com.as3arelyoum.data.api.ShowProductApi

class Helper(private val apiInterface: ShowProductApi) {
    suspend fun getProductDetails(product_id: Int) = apiInterface.getProductDetails(product_id)
}