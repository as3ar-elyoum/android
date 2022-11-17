package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Product
import com.as3arelyoum.utils.Constants.PRODUCT_DETAILS
import com.google.gson.JsonObject
import retrofit2.http.*

interface ProductDetailsApi {
    @GET(PRODUCT_DETAILS)
    suspend fun getProductDetails(@Path("product_id") id: Int): Product

    @PUT(PRODUCT_DETAILS)
    suspend fun updateProductDetails(
        @Path("product_id") id: Int,
        @Body product: JsonObject,
    ): Product
}