package com.as3arelyoum.data.resources.product

class Repo(private val apiHelper: Helper) {
    suspend fun getAllComments(postId: Int) = apiHelper.getAllComments(postId)
}