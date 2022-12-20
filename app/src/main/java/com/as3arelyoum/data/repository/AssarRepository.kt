package com.as3arelyoum.data.repository

import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.remote.network.RetrofitInstance
import com.google.gson.JsonObject

class AssarRepository {
    private val assarApi = RetrofitInstance.assarApiService

    suspend fun getAllCategories(deviceId: String) = assarApi.getAllCategories(deviceId)

    suspend fun getCategoryProducts(category_id: Int, deviceId: String) = assarApi.getCategoryProducts(category_id, deviceId)

    suspend fun getProductDetails(product_id: Int, deviceId: String) =
        assarApi.getProductDetails(product_id, deviceId)

    suspend fun updateProductDetails(product_id: Int, params: JsonObject, deviceId: String) =
        assarApi.updateProductDetails(product_id, params, deviceId)

    suspend fun search(query: String, deviceId: String) = assarApi.search(query, deviceId)

    suspend fun getSimilarProducts(product_id: Int, deviceId: String) =
        assarApi.getSimilarProducts(product_id, deviceId)

    suspend fun sendDevice(userInfoDTO: UserInfoDTO) =
        assarApi.sendDevice(userInfoDTO)
}