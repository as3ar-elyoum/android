package com.as3arelyoum.data.resources.productDetails

class Repo(private val apiHelper: Helper) {
    suspend fun getProductDetails(product_id: Int) = apiHelper.getProductDetails(product_id)
}