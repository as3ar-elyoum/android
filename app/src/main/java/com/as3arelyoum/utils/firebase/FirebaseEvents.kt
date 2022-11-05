package com.as3arelyoum.utils.firebase

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class FirebaseEvents {
    companion object {
        fun sendFirebaseEvent(event: String, data: String) {
            val eventName = cleanEventName(event)
            Log.d("Event", eventName)
            val firebaseAnalytics = Firebase.analytics
            firebaseAnalytics.logEvent(eventName) {
                param("data", data)
            }
        }

        fun sendScreenName(screenName: String) {
            val firebaseAnalytics = Firebase.analytics
            Log.d("Analytics", screenName)
            Log.d("Analytics", screenName)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                param(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
            }
        }

        private fun cleanEventName(eventName: String): String {
            val spaceRegex = """ +""".toRegex()
            val dotRegex = """\.+""".toRegex()
            val slashRegex = """/+""".toRegex()

            var text = dotRegex.replace(eventName, " ").trim()
            text = spaceRegex.replace(text, "_")
            text = slashRegex.replace(text, "_")
            return text
        }
    }
}