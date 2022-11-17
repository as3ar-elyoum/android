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
    var status: List<String>,
    var category_id: List<Int>,
    var prices: Array<Array<String>>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (image_url != other.image_url) return false
        if (price != other.price) return false
        if (url != other.url) return false
        if (source != other.source) return false
        if (source_page != other.source_page) return false
        if (created_at != other.created_at) return false
        if (updated_at != other.updated_at) return false
        if (!prices.contentDeepEquals(other.prices)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + image_url.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + source.hashCode()
        result = 31 * result + source_page.hashCode()
        result = 31 * result + created_at.hashCode()
        result = 31 * result + updated_at.hashCode()
        result = 31 * result + prices.contentDeepHashCode()
        return result
    }
}
