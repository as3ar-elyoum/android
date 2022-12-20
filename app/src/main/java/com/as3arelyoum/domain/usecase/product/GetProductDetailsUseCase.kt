package com.as3arelyoum.domain.usecase.product

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.domain.mapper.product.toProductModel
import com.as3arelyoum.domain.model.product.ProductModel
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(private val repository: AssarRepository) {
    suspend operator fun invoke(product_id: Int, deviceId: String): ProductModel {
        return repository.getProductDetails(product_id, deviceId).toProductModel()
    }
}