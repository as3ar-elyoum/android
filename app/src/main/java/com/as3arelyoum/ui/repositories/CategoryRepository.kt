package com.as3arelyoum.ui.repositories

import com.as3arelyoum.data.model.UserInfo
import com.as3arelyoum.data.network.RetrofitInstance

class CategoryRepository {
    suspend fun getAllCategories() = RetrofitInstance.categoryApi.getAllCategories()

    suspend fun addDevice(userInfo: UserInfo) = RetrofitInstance.userApi.addDevice(userInfo)
}