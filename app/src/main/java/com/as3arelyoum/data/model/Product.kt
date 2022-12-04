package com.as3arelyoum.data.model

data class Product(
    var id: Int,
    var name: String,
    var description: String,
    var image_url: String,
    var price: String,
    var url: String,
    var source: String,
    var source_page: String,
    var created_at: String,
    var updated_at: String,
    var status: String,
    var category_id: Int,
    var prices: List<List<String>>,
)
