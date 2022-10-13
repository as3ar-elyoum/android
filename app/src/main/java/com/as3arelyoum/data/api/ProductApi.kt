package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.Product
import com.as3arelyoum.utils.Constants.COMMENTS
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET(COMMENTS)
    suspend fun getAllComments(@Query("postId") postId: Int): List<Product>
}