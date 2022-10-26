package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface SimilarProductsApi {
    @GET("products/{product_id}/similar")
    suspend fun getSimilarProducts(@Path("product_id") product_id: Int): List<Product>
}