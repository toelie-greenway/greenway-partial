package greenway_myanmar.org.features.fishfarmrecord.data.repository

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

    override fun getFarmInputProductCategoriesStream(): Flow<List<FarmInputProductCategory>> {
        return flow {
            emit(
                network.getFarmInputProductCategories()
                    .map(NetworkFarmInputProductCategory::asDomainModel)
            )
        }
    }
}