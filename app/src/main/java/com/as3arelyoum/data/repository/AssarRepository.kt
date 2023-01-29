package com.as3arelyoum.data.repository

import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.remote.network.RetrofitInstance
import com.google.gson.JsonObject

class AssarRepository {
    private val assarApi = RetrofitInstance.assarApiService

    suspend fun getAllCategories() = assarApi.getAllCategories()

    suspend fun getAllProducts(category_id: Int, fcm_token: String) =
        assarApi.getAllProducts(category_id, fcm_token)

    suspend fun getProductDetails(product_id: Int, deviceId: String) =
        assarApi.getProductDetails(product_id, deviceId)

    suspend fun updateProductDetails(product_id: Int, params: JsonObject, fcm_token: String) =
        assarApi.updateProductDetails(product_id, params, fcm_token)

    suspend fun search(query: String, fcm_token: String) = assarApi.search(query, fcm_token)

    suspend fun getSimilarProducts(product_id: Int, fcm_token: String) =
        assarApi.getSimilarProducts(product_id, fcm_token)

    suspend fun sendDevice(userInfoDTO: UserInfoDTO, fcm_token: String) =
        assarApi.sendDevice(userInfoDTO, fcm_token)
}