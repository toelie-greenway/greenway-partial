package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProduct
import kotlinx.coroutines.flow.Flow

interface FarmInputProductRepository {
    fun getFarmInputProductsStream(
        query: String,
        categoryId: String
    ): Flow<List<FarmInputProduct>>
}