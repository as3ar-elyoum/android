package com.as3arelyoum.data.repository

import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.remote.network.RetrofitInstance
import com.google.gson.JsonObject

class AssarRepository {
    private val assarApi = RetrofitInstance.assarApiService

    suspend fun getAllCategories(fcmToken: String) = assarApi.getAllCategories(fcmToken)

    fun getHomeData(fcmToken: String) = assarApi.getHomeData(fcmToken)

    suspend fun getAllProducts(category_id: Int, fcmToken: String) =
        assarApi.getAllProducts(category_id, fcmToken)

    suspend fun getProductDetails(product_id: Int, fcmToken: String) =
        assarApi.getProductDetails(product_id, fcmToken)

    suspend fun updateProductDetails(product_id: Int, params: JsonObject, fcmToken: String) =
        assarApi.updateProductDetails(product_id, params, fcmToken)

    suspend fun search(query: String, fcmToken: String) = assarApi.search(query, fcmToken)

    suspend fun getSimilarProducts(product_id: Int, fcmToken: String) =
        assarApi.getSimilarProducts(product_id, fcmToken)

    suspend fun sendDevice(userInfoDTO: UserInfoDTO, fcmToken: String) =
        assarApi.sendDevice(userInfoDTO, fcmToken)
}