package greenway_myanmar.org.features.fishfarmrecord.data.repository

import greenway_myanmar.org.features.fishfarmrecord.data.source.network.FishFarmRecordNetworkDataSource
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.NetworkFarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.data.source.network.model.asDomainModel
import greenway_myanmar.org.features.fishfarmrecord.domain.model.FarmInputProduct
import greenway_myanmar.org.features.fishfarmrecord.domain.repository.FarmInputProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultFarmInputProductRepository @Inject constructor(
    private val network: FishFarmRecordNetworkDataSource
) : FarmInputProductRepository {

    override fun getFarmInputProductsStream(
        query: String,
        categoryId: String
    ): Flow<List<FarmInputProduct>> {
        return flow {
            emit(
                network.getFarmInputProducts(
                    query = query,
                    categoryId = categoryId
                ).map(NetworkFarmInputProduct::asDomainModel)
            )
        }
    }
}