package com.as3arelyoum.data.network

import com.as3arelyoum.data.api.CategoryApi
import com.as3arelyoum.data.api.ProductDetailsApi
import com.as3arelyoum.data.api.ProductsApi
import com.as3arelyoum.data.api.SimilarProductsApi
import com.as3arelyoum.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val retrofit: Retrofit by lazy {
        val mHttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val mOkHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(mHttpLoggingInterceptor)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkHttpClient)
            .build()
    }

    val categoryApi: CategoryApi by lazy {
        retrofit.create(CategoryApi::class.java)
    }

    val productsApi: ProductsApi by lazy {
        retrofit.create(ProductsApi::class.java)
    }

    val productDetailsApi: ProductDetailsApi by lazy {
        retrofit.create(ProductDetailsApi::class.java)
    }

    val similarProductApi: SimilarProductsApi by lazy {
        retrofit.create(SimilarProductsApi::class.java)
    }
}