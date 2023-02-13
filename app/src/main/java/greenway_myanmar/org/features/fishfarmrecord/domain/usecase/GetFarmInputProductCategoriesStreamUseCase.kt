package greenway_myanmar.org.features.fishfarmrecord.domain.usecase

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmInputProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFarmInputProductCategoriesStreamUseCase @Inject constructor(
    private val repository: FarmInputProductCategoryRepository
) {

    operator fun invoke(): Flow<Result<List<FarmInputProductCategory>>> {
        return repository.getCategoriesStream()
    }
}