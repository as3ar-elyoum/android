package com.as3arelyoum.ui.repositories

import com.as3arelyoum.data.network.RetrofitInstance

class SimilarProductsRepository {
    suspend fun getSimilarProducts(product_id: Int) =
        RetrofitInstance.similarProductApi.getSimilarProducts(product_id)
}