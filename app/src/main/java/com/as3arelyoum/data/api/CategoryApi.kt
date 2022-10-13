package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Category
import com.as3arelyoum.utils.Constants.POSTS
import retrofit2.http.GET

interface CategoryApi {
    @GET(POSTS)
    suspend fun getALlPosts(): List<Category>
}