package com.as3arelyoum.data.resources.category

import com.as3arelyoum.data.api.CategoryApi

class Helper(private val apiInterface: CategoryApi) {
    suspend fun getAllPosts() = apiInterface.getALlPosts()
}