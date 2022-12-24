package com.as3arelyoum.data.api

import com.as3arelyoum.data.models.Category
import com.as3arelyoum.data.models.Product
import com.as3arelyoum.data.models.User
import com.as3arelyoum.utils.helper.Constants.CATEGORIES
import com.as3arelyoum.utils.helper.Constants.PRODUCTS
import com.as3arelyoum.utils.helper.Constants.PRODUCT_DETAILS
import com.as3arelyoum.utils.helper.Constants.SEARCH
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface AssarApiService {
    @GET(CATEGORIES)
    suspend fun getAllCategories(
        @Header("deviceid") deviceId: String
    ): Response<List<Category>>

    @GET(PRODUCTS)
    suspend fun getCategoryProducts(
        @Query("category_id") category_id: Int?,
        @Header("deviceid") deviceId: String
    ): Response<List<Product>>

    @GET(PRODUCT_DETAILS)
    suspend fun getProductDetails(
        @Path("product_id") id: Int,
        @Header("deviceid") deviceId: String
    ): Response<Product>

    @PUT(PRODUCT_DETAILS)
    suspend fun updateProductDetails(
        @Path("product_id") id: Int,
        @Body product: JsonObject,
        @Header("deviceid") deviceId: String
    ): Response<Product>

    @GET(SEARCH)
    suspend fun search(
        @Query("query[q]") query: String,
        @Header("deviceid") deviceId: String
    ): Response<List<Product>>

    @GET("products/{product_id}/similar")
    suspend fun getSimilarProducts(
        @Path("product_id") product_id: Int,
        @Header("deviceid") deviceId: String
    ): Response<List<Product>>

    @POST("devices")
    suspend fun sendDevice(
        @Body user: User,
    ): Response<User>
}