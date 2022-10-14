package com.as3arelyoum.data.resources.category

import android.app.Application
import com.as3arelyoum.data.api.CategoryApi
import com.as3arelyoum.data.request.RetrofitClientCalling.client

class Creator : Application() {
    companion object {
        private var apiHelper: Helper? = null
        fun getApiHelperInstance(): Helper {
            if (apiHelper == null) {
                apiHelper = Helper(client!!.create(CategoryApi::class.java))
            }
            return apiHelper!!
        }
    }
}