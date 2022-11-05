package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.UserInfo
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("devices")
    suspend fun addUser(@Body userInfo: UserInfo): UserInfo
}