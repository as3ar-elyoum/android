package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ShowProductApi {
    @GET("products/{product_id}")
    suspend fun getProductDetails(@Path("product_id") id: Int): Product
}