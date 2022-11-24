package com.as3arelyoum.ui.repositories

import com.as3arelyoum.data.network.RetrofitInstance

class SearchRepository {
    suspend fun search(query: String) = RetrofitInstance.searchApi.search(query)
}