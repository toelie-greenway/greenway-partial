package greenway_myanmar.org.features.fishfarmrecord.data.repository

import com.greenwaymyanmar.common.result.Result
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProductCategory
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmInputProductCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultFarmInputProductCategoryRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource
) : FarmInputProductCategoryRepository {

    override fun getCategoriesStream(): Flow<Result<List<FarmInputProductCategory>>> {
        return flow {
            emit(
                Result.Success(
                    network.getFarmInputProductCategories()
                        .map(NetworkFarmInputProductCategory::asDomainModel)
                )
            )
        }
    }
}