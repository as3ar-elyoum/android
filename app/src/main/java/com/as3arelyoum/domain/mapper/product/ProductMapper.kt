package com.as3arelyoum.domain.mapper.product

import com.as3arelyoum.data.remote.response.dto.ProductDTO
import com.as3arelyoum.domain.model.product.ProductModel

fun ProductDTO.toProductModel(): ProductModel {
    return ProductModel(
        id = this.id,
        name = this.name,
        description = this.description,
        image_url = this.image_url,
        price = this.price,
        url = this.url,
        source = this.source,
        source_page = this.source_page,
        created_at = this.created_at,
        updated_at = this.updated_at,
        status = this.status,
        category_id = this.category_id,
        prices = this.prices
    )
}