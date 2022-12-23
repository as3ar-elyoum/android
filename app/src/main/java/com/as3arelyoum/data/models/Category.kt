package com.as3arelyoum.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var id: Int,
    var name: String,
    var icon: String
) : Parcelable
