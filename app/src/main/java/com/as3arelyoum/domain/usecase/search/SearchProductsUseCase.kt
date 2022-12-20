package com.as3arelyoum.domain.usecase.search

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.domain.mapper.product.toProductModel
import com.as3arelyoum.domain.model.product.ProductModel
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(private val repository: AssarRepository) {
    suspend operator fun invoke(query: String, deviceId: String): List<ProductModel> {
        return repository.search(query, deviceId).map {
            it.toProductModel()
        }
    }
}