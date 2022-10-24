package com.as3arelyoum.data.resources.category

class Repo(private val apiHelper: Helper) {
    suspend fun getAllCategories() = apiHelper.getAllCategories()
}