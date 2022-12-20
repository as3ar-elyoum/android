package com.as3arelyoum.domain.usecase.product

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.domain.mapper.product.toProductModel
import com.as3arelyoum.domain.model.product.ProductModel
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(private val repository: AssarRepository) {
    suspend operator fun invoke(category_id: Int, deviceId: String): List<ProductModel> {
        return repository.getAllProducts(category_id, deviceId).map {
            it.toProductModel()
        }
    }
}