package com.as3arelyoum.data.remote.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryDTO(
    var id: Int,
    var name: String,
    var icon: String
) : Parcelable
