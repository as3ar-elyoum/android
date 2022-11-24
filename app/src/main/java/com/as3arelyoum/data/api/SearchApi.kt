package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun search(@Query("query[q]") query: String): Response<List<Product>>
}
