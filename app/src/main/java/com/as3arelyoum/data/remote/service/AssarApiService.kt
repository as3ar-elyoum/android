package com.as3arelyoum.data.remote.service

import com.as3arelyoum.data.remote.response.base.BaseResponse
import com.as3arelyoum.data.remote.response.dto.CategoryDTO
import com.as3arelyoum.data.remote.response.dto.ProductDTO
import com.as3arelyoum.data.remote.response.dto.UserInfoDTO
import com.google.gson.JsonObject
import retrofit2.http.*

interface AssarApiService {

    /**
     * Categories
     */
    @GET("categories")
    suspend fun getAllCategories(): BaseResponse<CategoryDTO>

    /**
     * Products
     */
    @GET("products")
    suspend fun getAllProducts(
        @Query("category_id") category_id: Int,
        @Header("deviceid") deviceId: String
    ): BaseResponse<ProductDTO>

    @GET("products/{product_id}/similar")
    suspend fun getSimilarProducts(
        @Path("product_id") product_id: Int,
        @Header("deviceid") deviceId: String
    ): BaseResponse<ProductDTO>

    @GET("products/{product_id}")
    suspend fun getProductDetails(
        @Path("product_id") id: Int,
        @Header("deviceid") deviceId: String
    ): ProductDTO

    @PUT("products/{product_id}")
    suspend fun updateProductDetails(
        @Path("product_id") id: Int,
        @Body product: JsonObject,
        @Header("deviceid") deviceId: String
    ): ProductDTO

    /**
     * Search
     */
    @GET("search")
    suspend fun search(
        @Query("query[q]") query: String,
        @Header("deviceid") deviceId: String
    ): BaseResponse<ProductDTO>

    /**
     * User
     */
    @POST("devices")
    suspend fun sendDeviceId(
        @Body userInfoDTO: UserInfoDTO,
        @Header("deviceid") deviceId: String
    ): UserInfoDTO
}