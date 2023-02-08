package com.as3arelyoum.data.remote.service

import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.remote.dto.ProductDTO
import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.utils.helper.Constants.CATEGORIES
import com.as3arelyoum.utils.helper.Constants.FCM_TOKEN
import com.as3arelyoum.utils.helper.Constants.PRODUCTS
import com.as3arelyoum.utils.helper.Constants.PRODUCT_DETAILS
import com.as3arelyoum.utils.helper.Constants.SEARCH
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AssarApiService {
    @GET(CATEGORIES)
    suspend fun getAllCategories(
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<List<CategoryDTO>>

    @GET("products/home")
    fun getHomeData(
        @Header(FCM_TOKEN) fcmToken: String
    ): Call<List<CategoryDTO>>

    @GET(PRODUCTS)
    suspend fun getAllProducts(
        @Query("category_id") category_id: Int,
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<List<ProductDTO>>

    @GET(PRODUCT_DETAILS)
    suspend fun getProductDetails(
        @Path("product_id") id: Int,
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<ProductDTO>

    @PUT(PRODUCT_DETAILS)
    suspend fun updateProductDetails(
        @Path("product_id") id: Int,
        @Body product: JsonObject,
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<ProductDTO>

    @GET(SEARCH)
    suspend fun search(
        @Query("query[q]") query: String,
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<List<ProductDTO>>

    @GET("products/{product_id}/similar")
    suspend fun getSimilarProducts(
        @Path("product_id") product_id: Int,
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<List<ProductDTO>>

    @POST("devices")
    suspend fun sendDevice(
        @Body userInfoDTO: UserInfoDTO,
        @Header(FCM_TOKEN) fcmToken: String
    ): Response<UserInfoDTO>
}