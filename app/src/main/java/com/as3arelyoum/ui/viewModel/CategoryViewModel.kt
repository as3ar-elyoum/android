package com.as3arelyoum.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.as3arelyoum.R
import com.as3arelyoum.data.model.Category

class CategoryViewModel : ViewModel() {
    val categoryLiveData = MutableLiveData<List<Category>>()
    private var items: ArrayList<Category> = ArrayList()


    fun fakeData() {
        for (i in 1..20) {
            categoryLiveData.value = items.apply {
                add(Category(randomId(), "أمازون", R.mipmap.light_logo))
            }
        }
    }

    private fun randomId(): Int {
        return (1..100).random()
    }
}