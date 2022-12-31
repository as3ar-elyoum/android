package com.as3arelyoum.data.remote.network

import com.as3arelyoum.data.remote.service.AssarApiService
import com.as3arelyoum.utils.helper.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val retrofit: Retrofit by lazy {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC)
        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .callTimeout(1, TimeUnit.MINUTES)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

    val assarApiService: AssarApiService by lazy {
        retrofit.create(AssarApiService::class.java)
    }
}