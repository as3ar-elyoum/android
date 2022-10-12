package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Category
import com.as3arelyoum.data.model.Product

class ProductViewModel : ViewModel() {
    val productLiveData = MutableLiveData<List<Product>>()
    private var items: ArrayList<Product> = ArrayList()


    fun fakeData() {
        for (i in 1..20) {
            productLiveData.value = items.apply {
                add(Product("إالكترونيات", R.mipmap.light_logo))
            }
        }
    }
}