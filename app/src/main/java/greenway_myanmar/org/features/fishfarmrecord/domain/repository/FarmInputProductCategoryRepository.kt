package greenway_myanmar.org.features.fishfarmrecord.domain.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import kotlinx.coroutines.flow.Flow

interface FarmInputProductCategoryRepository {
    fun getCategoriesStream(): Flow<Result<List<FarmInputProductCategory>>>
}