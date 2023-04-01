package com.greenwaymyanmar.common.feature.tag.domain.usecases

import greenway_myanmar.org.repository.ProductRepository
import greenway_myanmar.org.vo.Listing
import greenway_myanmar.org.vo.Product
import javax.inject.Inject

class GetTagProductsListingUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Listing<Product> {
        return repository.getProductsByTag()
    }
}