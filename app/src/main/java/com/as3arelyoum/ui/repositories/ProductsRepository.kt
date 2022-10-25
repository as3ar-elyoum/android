package com.as3arelyoum.ui.repositories

import com.as3arelyoum.data.network.RetrofitInstance

class ProductsRepository {
    suspend fun getAllProducts(category_id: Int) =
        RetrofitInstance.productsApi.getAllProducts(category_id)
}