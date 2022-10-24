package com.as3arelyoum.utils

import android.content.Context
import java.util.*

object Constants {
    const val BASE_URL = "http://price-index.magdi.work:8080/api/"
    const val CATEGORIES = "categories"
    const val PRODUCTS = "products"

    fun Context.setAppLocale(language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }
}