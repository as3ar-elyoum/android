package com.as3arelyoum.data.repository

import com.as3arelyoum.data.remote.dto.UserInfoDTO
import com.as3arelyoum.data.remote.network.RetrofitInstance
import com.google.gson.JsonObject

class AssarRepository {
    private val assarApi = RetrofitInstance.assarApiService

    suspend fun getAllCategories() = assarApi.getAllCategories()

    suspend fun getAllProducts(category_id: Int) = assarApi.getAllProducts(category_id)

    suspend fun getProductDetails(product_id: Int) = assarApi.getProductDetails(product_id)

    suspend fun updateProductDetails(product_id: Int, params: JsonObject) =
        assarApi.updateProductDetails(product_id, params)

    suspend fun search(query: String) = assarApi.search(query)

    suspend fun getSimilarProducts(product_id: Int) = assarApi.getSimilarProducts(product_id)

    suspend fun sendDevice(userInfoDTO: UserInfoDTO) = assarApi.sendDevice(userInfoDTO)
}