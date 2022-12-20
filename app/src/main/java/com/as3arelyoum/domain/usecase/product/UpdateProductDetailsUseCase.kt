package com.as3arelyoum.domain.usecase.product

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.domain.mapper.product.toProductModel
import com.as3arelyoum.domain.model.product.ProductModel
import com.google.gson.JsonObject
import javax.inject.Inject

class UpdateProductDetailsUseCase @Inject constructor(private val repository: AssarRepository) {
    suspend operator fun invoke(
        product_id: Int,
        params: JsonObject,
        deviceId: String
    ): ProductModel {
        return repository.updateProductDetails(product_id, params, deviceId).toProductModel()
    }
}