package com.as3arelyoum.utils.helper

import android.content.Context
import android.view.View
import androidx.core.widget.NestedScrollView
import com.google.firebase.messaging.FirebaseMessaging

object Constants {
    const val BASE_URL = "https://price-index.magdi.work/api/"
    const val CATEGORIES = "categories"
    const val PRODUCTS = "products"
    const val PRODUCT_DETAILS = "products/{product_id}"
    const val CHANNEL_ID = "firebase_channel_id"
    const val PREF_FILE = "pref_file"
    const val SEARCH = "search"
    val statusList = listOf("inactive", "active", "disabled", "duplicate")

    fun displayProductDetails(str: String, source: String): String {
        return "$str $source"
    }

    fun displayProductPrice(
        buyFrom: String,
        source: String,
        b: String,
        price: String,
        egp: String
    ): String {
        return "$buyFrom $source $b $price $egp"
    }

    fun toggleArrow(view: View): Boolean {
        return if (view.rotation == 0f) {
            view.animate().setDuration(200).rotation(180f)
            true
        } else {
            view.animate().setDuration(200).rotation(0f)
            false
        }
    }

    fun nestedScrollTo(nested: NestedScrollView, targetView: View) {
        nested.post { nested.scrollTo(500, targetView.top) }
    }

    fun getToken(context: Context) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            PrefUtil.initPrefUtil(context)
            PrefUtil.saveData("token", token)
        }
    }
}
