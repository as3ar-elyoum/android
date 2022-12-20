package com.as3arelyoum.data.repository

import com.as3arelyoum.data.remote.response.dto.CategoryDTO
import com.as3arelyoum.data.remote.response.dto.ProductDTO
import com.as3arelyoum.data.remote.response.dto.UserInfoDTO
import com.as3arelyoum.data.remote.service.AssarApiService
import com.google.gson.JsonObject
import javax.inject.Inject

class AssarRepositoryImpl @Inject constructor(
    private val assarApiService: AssarApiService
) : AssarRepository {
    override suspend fun getAllCategories(): List<CategoryDTO> {
        return assarApiService.getAllCategories().result
    }

    override suspend fun getAllProducts(category_id: Int, deviceId: String): List<ProductDTO> {
        return assarApiService.getAllProducts(category_id, deviceId).result
    }

    override suspend fun getSimilarProducts(product_id: Int, deviceId: String): List<ProductDTO> {
        return assarApiService.getSimilarProducts(product_id, deviceId).result
    }

    override suspend fun getProductDetails(product_id: Int, deviceId: String): ProductDTO {
        return assarApiService.getProductDetails(product_id, deviceId)
    }

    override suspend fun updateProductDetails(
        product_id: Int,
        params: JsonObject,
        deviceId: String
    ): ProductDTO {
        return assarApiService.updateProductDetails(product_id, params, deviceId)
    }

    override suspend fun search(query: String, deviceId: String): List<ProductDTO> {
        return assarApiService.search(query, deviceId).result
    }

    override suspend fun sendDeviceId(userInfoDTO: UserInfoDTO, deviceId: String): UserInfoDTO {
        return assarApiService.sendDeviceId(userInfoDTO, deviceId)
    }
}