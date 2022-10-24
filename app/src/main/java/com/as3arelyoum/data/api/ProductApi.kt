package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Product
import com.as3arelyoum.utils.Constants.PRODUCTS
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET(PRODUCTS)
    suspend fun getAllProducts(@Query("category_id") category_id: Int): List<Product>
}