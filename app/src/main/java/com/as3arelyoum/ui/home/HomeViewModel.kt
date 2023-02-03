package com.as3arelyoum.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.data.remote.dto.CategoryDTO
import com.as3arelyoum.data.repository.AssarRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val repository = AssarRepository()
    val categoriesList = MutableLiveData<List<CategoryDTO>>()
//    var productsLists = MutableLiveData<List<ProductDTO>>()

    init {
        getHomeData()
    }

    private fun getHomeData() {
        repository.getHomeData().enqueue(object : Callback<List<CategoryDTO>> {
            override fun onResponse(
                call: Call<List<CategoryDTO>>,
                response: Response<List<CategoryDTO>>
            ) {
                if (response.isSuccessful) {
                    categoriesList.value = response.body()
                }
            }

            override fun onFailure(call: Call<List<CategoryDTO>>, t: Throwable) {
                Log.e("HomeViewModel", "onFailure: ${t.message}")
            }
        })
    }
}
