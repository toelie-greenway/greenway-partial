package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import kotlinx.coroutines.flow.Flow

interface FarmInputProductCategoryRepository {
    fun getFarmInputProductCategoriesStream(): Flow<List<FarmInputProductCategory>>
}