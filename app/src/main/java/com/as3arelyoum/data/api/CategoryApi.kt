package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Category
import com.as3arelyoum.utils.Constants.CATEGORIES
import retrofit2.Response
import retrofit2.http.GET

interface CategoryApi {
    @GET(CATEGORIES)
    suspend fun getAllCategories(): Response<List<Category>>
}