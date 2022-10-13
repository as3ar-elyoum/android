package com.as3arelyoum.network

import com.as3arelyoum.data.model.Category
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("")
    suspend fun getAsteroids():List<Category>
}

object NetworkService {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()




    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val Service: ApiService by lazy { retrofit.create(ApiService::class.java) }

}
