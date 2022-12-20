package com.as3arelyoum.data.repository

import com.as3arelyoum.data.remote.response.dto.CategoryDTO
import com.as3arelyoum.data.remote.response.dto.ProductDTO
import com.as3arelyoum.data.remote.response.dto.UserInfoDTO
import com.google.gson.JsonObject

interface AssarRepository {

    suspend fun getAllCategories(): List<CategoryDTO>
    suspend fun getAllProducts(category_id: Int, deviceId: String): List<ProductDTO>
    suspend fun getSimilarProducts(product_id: Int, deviceId: String): List<ProductDTO>
    suspend fun getProductDetails(product_id: Int, deviceId: String): ProductDTO
    suspend fun updateProductDetails(
        product_id: Int,
        params: JsonObject,
        deviceId: String
    ): ProductDTO

    suspend fun search(query: String, deviceId: String): List<ProductDTO>
    suspend fun sendDeviceId(userInfoDTO: UserInfoDTO, deviceId: String): UserInfoDTO
}