package com.as3arelyoum.data.resources.product

class Repo(private val apiHelper: Helper) {
    suspend fun getAllProducts(category_id: Int) = apiHelper.getAllProducts(category_id)
}