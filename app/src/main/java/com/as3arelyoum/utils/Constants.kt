package com.as3arelyoum.utils

object Constants {
    const val BASE_URL = "http://price-index.magdi.work:8080/api/"
    const val CATEGORIES = "categories"
    const val PRODUCTS = "products"


    fun displayProductDetails(str: String, source: String): String {
        return "$str $source"
    }
}