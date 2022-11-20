package com.as3arelyoum.data.api

import com.as3arelyoum.data.model.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("devices")
    suspend fun sendDevice(@Body userInfo: UserInfo): Response<UserInfo>
}