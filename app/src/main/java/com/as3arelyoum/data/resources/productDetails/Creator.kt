package com.as3arelyoum.data.resources.productDetails

import android.app.Application
import com.as3arelyoum.data.api.ShowProductApi
import com.as3arelyoum.data.request.RetrofitClientCalling.client

class Creator : Application() {
    companion object {
        private var apiHelper: Helper? = null
        fun getApiHelperInstance(): Helper {
            if (apiHelper == null) {
                apiHelper = Helper(client!!.create(ShowProductApi::class.java))
            }
            return apiHelper!!
        }
    }
}